package single.seckillv2.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import single.seckillv2.Commen.PageVO;
import single.seckillv2.Commen.Result;
import single.seckillv2.Entity.VO.ProductVO;
import single.seckillv2.Service.ProductService;

@Tag(name = "商品管理",description = "商品列表，搜索商品等接口")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    //商品列表
    @Operation(summary = "商品列表",description = "展示商品列表")
    @GetMapping("/list")
    public Result<PageVO<ProductVO>> ProductList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10")int pageSize){
        PageVO<ProductVO> page = productService.ProductList(pageNum, pageSize);
        return Result.success(page);

    }

    //商品详情
    @Operation(summary = "搜索商品",description = "根据商品id搜索")
    @GetMapping("/list/{productId}")
    public Result<ProductVO> SelectProductByID(@PathVariable Integer productId){
        ProductVO vo = productService.SelectProductByID(productId);
        return Result.success(vo);
    }
}
