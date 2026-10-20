package single.seckillv2.Entity.VO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单视图对象")
public class OrderVO {
    private Integer orderId;
    private Integer userId;
    private Integer productId;
    private Integer status;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime createTime;
    private String productName;
}
