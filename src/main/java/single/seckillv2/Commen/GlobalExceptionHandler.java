package single.seckillv2.Commen;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(
            BusinessException e) {
        return Result.error(e.getMessage());
    }
    //数据库唯一索引异常
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<Void> handleDuplicateKeyException(
            DuplicateKeyException e) {
        return Result.error("用户名已存在");
    }
    //其他
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        e.printStackTrace();
        return Result.error("系统错误");
    }
}
