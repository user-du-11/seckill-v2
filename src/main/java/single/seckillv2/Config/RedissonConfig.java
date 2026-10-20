package single.seckillv2.Config;

import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import single.seckillv2.Entity.OrderTimeoutTask;

@Configuration
public class RedissonConfig {
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                .setConnectionPoolSize(64)
                .setConnectionMinimumIdleSize(10);
        config.setLockWatchdogTimeout(30*1000);
        return Redisson.create(config);
    }
    //阻塞队列
    @Bean
    public RBlockingQueue<OrderTimeoutTask> orderTimeoutTaskRBlockingQueue(RedissonClient redissonClient){
        return redissonClient.getBlockingQueue("order:timeout:blocking");
    }
    //延迟队列
    @Bean
    public RDelayedQueue<OrderTimeoutTask> orderTimeoutTaskRDelayedQueue(RedissonClient redissonClient,RBlockingQueue<OrderTimeoutTask> orderTimeoutTaskRBlockingQueue){
        //从Redis里面获取一个延迟队列，把延迟队列和阻塞队列绑定（意思是：延迟队列里面到期的任务，会自动转移到这个阻塞队列里）。
        return redissonClient.getDelayedQueue(orderTimeoutTaskRBlockingQueue);
    }
}