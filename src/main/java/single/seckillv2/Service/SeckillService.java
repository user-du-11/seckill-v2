package single.seckillv2.Service;


import single.seckillv2.Entity.VO.OrderVO;

public interface SeckillService {
    OrderVO doSeckill(Integer userId, Integer productId);

}