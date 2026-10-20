package single.seckillv2.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import single.seckillv2.Commen.Result;
import single.seckillv2.Entity.VO.OrderVO;
import single.seckillv2.Service.OrderService;

import java.util.List;
@Tag(name = "订单管理",description = "订单创建，订单搜索等接口")
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Operation(summary = "订单创建",description = "根据用户id,商品id和购买数量创建订单")
    @PostMapping("/create")
    public Result<OrderVO> CreateOrder(@RequestParam Integer userId, @RequestParam Integer productId, @RequestParam Integer quantity){
        return Result.success(orderService.CreateOrder(userId,productId,quantity));
    }

    @Operation(summary = "查询订单",description = "根据用户id查询订单")
    @GetMapping("/list")
    public Result<List<OrderVO>> SelectOrderByUserId(@RequestParam Integer userId){
        return Result.success(orderService.SelectOrderByUserId(userId));
    }
}
