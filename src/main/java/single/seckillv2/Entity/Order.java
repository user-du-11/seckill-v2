package single.seckillv2.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer orderId;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer status;
}
