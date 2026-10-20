package single.seckillv2.Entity.VO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户视图对象")
public class UserVO {
    private Integer userId;
    private String username;
    private LocalDateTime createTime;
}
