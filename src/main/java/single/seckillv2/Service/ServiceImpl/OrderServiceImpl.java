package single.seckillv2.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import single.seckillv2.Commen.BusinessException;
import single.seckillv2.Entity.Order;
import single.seckillv2.Entity.Product;
import single.seckillv2.Entity.VO.OrderVO;
import single.seckillv2.Mapper.OrderMapper;
import single.seckillv2.Mapper.ProductMapper;
import single.seckillv2.Service.OrderService;
import single.seckillv2.Service.RedisService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private ProductMapper productMapper;
    private static final String STOCK_KEY_PREFIX = "seckill:stock:";
    @Autowired
    private RedisService redisService;

    //普通下单
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO CreateOrder(Integer userId, Integer productId,Integer quantity){
        //参数校验
        if(userId == null || productId == null || quantity == null || quantity <= 0){
            throw  new BusinessException("参数不合法");
        }
        //检查商品存在和是否下架
        Product product = productMapper.selectById(productId);
        if(product == null){
            throw new BusinessException("商品不存在");
        }
        if(product.getStock() < quantity){
            throw  new BusinessException("库存不足");
        }
        //原子扣减库存防止超买
        int row = productMapper.update(null,new LambdaUpdateWrapper<Product>()
                .setSql("stock = stock -"+ quantity)
                .eq(Product::getProductId,productId)
                .ge(Product::getStock,quantity)
        );
        if(row == 0){
            throw new BusinessException("库存不足");
        }
        //计算总价
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        //创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setTotalPrice(totalPrice);
        order.setQuantity(quantity);
        order.setCreateTime(LocalDateTime.now());
        order.setStatus(0);
        save(order);

        //转化为VO
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order,vo);
        vo.setProductName(product.getName());
        return vo;
    }

    //查订单
    @Override
    public List<OrderVO> SelectOrderByUserId(Integer userId){
        //查询用户订单，根据userId,按创建时间降序
        List<Order> list = lambdaQuery()
                .eq(Order::getUserId,userId)
                .orderByDesc(Order::getCreateTime)
                .list();
        //转VO
        return list.stream().map(p ->{
                OrderVO vo = new OrderVO();
                BeanUtils.copyProperties(p,vo);
                Product product = productMapper.selectById(p.getProductId());
                if(product != null){
                    vo.setProductName(product.getName());
                }
                return vo;
                }).toList();
    }
    //取消订单逻辑
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelTimeoutOrder(Integer orderId){
        //查询订单
        Order order = getById(orderId);
        if(order == null){
            System.out.println("【取消订单】订单不存在："+orderId);
            return false;
        }
        //更新状态
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getOrderId,orderId)
                .eq(Order::getStatus,0)
                .set(Order::getStatus,2);
        boolean updated = update(wrapper);

        if(!updated){
            System.out.println("【取消订单】订单状态已变更，无需取消："+orderId);
            return false;
        }
        //回补数据库库存
        LambdaUpdateWrapper<Product> productWrapper = new LambdaUpdateWrapper<>();
        productWrapper.eq(Product::getProductId,order.getProductId())
                .setSql("stock = stock +"+order.getQuantity());
        productMapper.update(null,productWrapper);

        //回补Redis库存
        String stockKey = STOCK_KEY_PREFIX + order.getProductId();
        redisService.incr(stockKey);

        System.out.println("【取消订单成功】订单ID:"+orderId+",已回补库存");
        return true;
    }

}
