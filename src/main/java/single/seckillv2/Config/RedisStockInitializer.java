package single.seckillv2.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import single.seckillv2.Entity.Product;
import single.seckillv2.Service.ProductService;

import java.util.List;

@Component
public class RedisStockInitializer implements CommandLineRunner {
    @Autowired
    private ProductService productService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private static final String STOCK_KEY_PREFIX = "seckill:stock:";
    private  static final Logger log = LoggerFactory.getLogger(RedisStockInitializer.class);
    @Override
    public void run(String ...args){
        List<Product> products = productService.list();
        for(Product product : products) {
            //预热库存
            String key = STOCK_KEY_PREFIX + product.getProductId();
            redisTemplate.opsForValue().set(key, product.getStock());
            log.info("库存预热：{}={}", key, product.getStock());
            //预热活动时间信息
            if (product.getStartTime() != null && product.getEndTime() != null) {
                String infoKey = "seckill:info:" + product.getProductId();
                redisTemplate.opsForHash().put(infoKey, "startTime", product.getStartTime().toString());
                redisTemplate.opsForHash().put(infoKey, "endTime", product.getEndTime().toString());
                log.info("活动预热成功：{}",infoKey);
            }else{
                log.error("警告：商品{}缺少秒杀时间，跳过预热", product.getProductId());
            }
        }
    }
}
