package single.seckillv2.Commen;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private String message;
    private Integer code;
    private T data;

    public static <T> Result<T> success(T data){
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }
    public static  <T> Result<T> error(String message){
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMessage(message);
        return r;
    }
}
