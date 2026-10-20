package single.seckillv2.Service.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import single.seckillv2.Commen.BusinessException;
import single.seckillv2.Entity.Order;
import single.seckillv2.Entity.OrderTimeoutTask;
import single.seckillv2.Entity.Product;
import single.seckillv2.Entity.VO.OrderVO;
import single.seckillv2.Service.OrderService;
import single.seckillv2.Service.ProductService;
import single.seckillv2.Service.RedisService;
import single.seckillv2.Service.SeckillService;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisService redisService;

    @Autowired
    private RDelayedQueue<OrderTimeoutTask> orderTimeoutTaskRDelayedQueue;

    private static final String STOCK_KEY_PREFIX = "seckill:stock:";
    private static final String Lock_KEY_PREFIX = "lock:seckill:";

    //订单超时时间：30秒
    private static  final long ORDER_TIMEOUT_SECONDS = 30;

    public OrderVO doSeckill(Integer userId, Integer productId){
        //参数校验
        if(userId == null || productId == null){
            throw new BusinessException("参数不合法");
        }
        //时间校验
        String infoKey = "seckill:info:" + productId;
        Map<Object,Object> info = redisTemplate.opsForHash().entries(infoKey);
        if(info.isEmpty()){
            throw new BusinessException("活动不存在");
        }
        LocalDateTime startTime = LocalDateTime.parse((String) info.get("startTime"));
        LocalDateTime endTime = LocalDateTime.parse((String) info.get("endTime"));
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(startTime)){
            throw new BusinessException("秒杀尚未开始");
        }
        if(now.isAfter(endTime)){
            throw  new BusinessException("秒杀已结束");
        }

        //限流
        String limitKey = "seckill:limit:"+userId+":"+productId;
        Boolean allowed = redisTemplate.opsForValue()
                .setIfAbsent(limitKey,"1",1, TimeUnit.SECONDS);
        if(Boolean.FALSE.equals(allowed)){
            throw  new BusinessException("请求过于频繁，请稍后再试");
        }

        //预减库存
        String stockKey = STOCK_KEY_PREFIX + productId;
        Long stock = redisService.decr(stockKey);
        log.info("【LUA返回值】:{}", stock);
        if (stock == -1){
            throw  new BusinessException("商品不存在");
        }
        if(stock == 0){
            throw new BusinessException("商品已售罄");
        }
        log.info("【剩余库存:】{}", stock);
        //分布式锁
        String lockKey = Lock_KEY_PREFIX + productId;
        RLock lock = redissonClient.getLock(lockKey);
        try{
            lock.lock();
            Product product = productService.getById(productId);
            if(product == null){
                throw new BusinessException("商品不存在");
            }
            if(product.getStock() <= 0){
                redisService.incr(stockKey);
                throw new BusinessException("商品库存不足");
            }
            //扣减库存
            product.setStock(product.getStock()-1);
            productService.updateById(product);
            //创建订单
            Order order = new Order();
            order.setUserId(userId);
            order.setProductId(productId);
            order.setStatus(0);
            order.setQuantity(1);
            order.setTotalPrice(product.getSeckillPrice());
            order.setCreateTime(LocalDateTime.now());
            orderService.save(order);

            //投放延迟任务
            Integer orderId = order.getOrderId();
            //判断当前线程有没有开启事务
            if(TransactionSynchronizationManager.isSynchronizationActive()){
                //有事务，注册"事务提交后"的回调
                TransactionSynchronizationManager.registerSynchronization(
                        new TransactionSynchronization() {
                            @Override
                            public void afterCommit() {
                                OrderTimeoutTask task = new OrderTimeoutTask(orderId);
                                orderTimeoutTaskRDelayedQueue.offer(task,ORDER_TIMEOUT_SECONDS,TimeUnit.SECONDS);
                                System.out.println("【延迟任务已投放】订单 " + orderId + " 将在 " + ORDER_TIMEOUT_SECONDS + " 秒后检查是否超时");
                            }
                        }
                );
            }else {
                //没有开启事务
                OrderTimeoutTask task = new OrderTimeoutTask(orderId);
                orderTimeoutTaskRDelayedQueue.offer(task,ORDER_TIMEOUT_SECONDS,TimeUnit.SECONDS);
            }

            //转VO
            OrderVO vo = new OrderVO();
            BeanUtils.copyProperties(order,vo);
            vo.setProductName(product.getName());
            return vo;
        }finally {
            lock.unlock();
        }


    }
}
