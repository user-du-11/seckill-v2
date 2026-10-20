package single.seckillv2.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import single.seckillv2.Commen.Result;
import single.seckillv2.Entity.VO.OrderVO;
import single.seckillv2.Service.SeckillService;

@Tag(name = "秒杀管理",description = "实现秒杀功能接口")
@RestController
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    private SeckillService seckillService;

    @Operation(summary = "秒杀订单",description = "根据用户id和商品id秒杀")
    @PostMapping("/order")
    public Result<OrderVO> doSeckill(@RequestParam Integer userId,@RequestParam Integer productId){
        return Result.success(seckillService.doSeckill(userId,productId));
    }
}
