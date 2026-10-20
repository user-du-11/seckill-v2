package single.seckillv2.Entity.VO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品视图对象")
public class ProductVO {
    private Integer productId;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private BigDecimal seckillPrice;
    private Integer status;
}
