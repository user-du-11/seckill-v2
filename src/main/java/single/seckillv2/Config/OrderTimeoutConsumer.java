package single.seckillv2.Config;

import org.redisson.api.RBlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import single.seckillv2.Entity.OrderTimeoutTask;
import single.seckillv2.Service.OrderService;

@Component
public class OrderTimeoutConsumer implements CommandLineRunner {
    @Autowired
    private RBlockingQueue<OrderTimeoutTask> orderTimeoutTaskRBlockingQueue;

    @Autowired
    private OrderService orderService;

    @Override
    public void run(String...args){
        //启动一个独立的后台线程
        Thread consumer = new Thread(()->{
            System.out.println("【订单超时消费者】已启动，等待超时任务");
            while(!Thread.currentThread().isInterrupted()){
                try {
                    OrderTimeoutTask task = orderTimeoutTaskRBlockingQueue.take();
                    System.out.println("【收到超时任务】订单ID:"+task.getOrderId());

                    //取消逻辑
                    orderService.cancelTimeoutOrder(task.getOrderId());
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    System.out.println("【订单超时消费者】线程被中断，退出");
                    break;
                }catch (Exception e){
                    System.err.println("【订单超时处理异常】" + e.getMessage());
                    e.printStackTrace();
                }
            }
        },"order-timeout-consumer");
        consumer.setDaemon(true);
        consumer.start();
    }
}
