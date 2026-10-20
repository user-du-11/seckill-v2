package single.seckillv2.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import single.seckillv2.Entity.Order;
import single.seckillv2.Entity.VO.OrderVO;

import java.util.List;

public interface OrderService extends IService<Order> {
    OrderVO CreateOrder(Integer userId, Integer productId,Integer quantity);
    List<OrderVO> SelectOrderByUserId(Integer userId);
    boolean cancelTimeoutOrder(Integer orderId);
}
