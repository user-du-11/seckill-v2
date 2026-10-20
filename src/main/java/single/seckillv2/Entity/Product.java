package single.seckillv2.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Integer productId;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private BigDecimal seckillPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
}
