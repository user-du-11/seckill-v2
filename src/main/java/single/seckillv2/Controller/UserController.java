package single.seckillv2.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import single.seckillv2.Commen.Result;
import single.seckillv2.Entity.VO.UserVO;
import single.seckillv2.Service.UserService;

@Tag(name = "用户管理",description = "用户注册,登录等接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //注册
    @Operation(summary = "注册",description = "根据用户名和密码注册")
    @PostMapping("/register")
    public Result<UserVO> register(@RequestParam String username, @RequestParam String password){
        UserVO Vo = userService.register(username, password);
        return Result.success(Vo);
    }
    //登录
    @Operation(summary = "登录",description = "根据用户名和密码登录")
    @PostMapping("/login")
    public Result<UserVO> Login(@RequestParam String username,@RequestParam String password){
        UserVO Vo = userService.Login(username, password);
        return Result.success(Vo);
    }
    //查用户
    @Operation(summary = "查用户",description = "根据id查询用户")
    @GetMapping("/{id}")
    public Result<UserVO> SelectUserById(@PathVariable Integer id){
        UserVO Vo = userService.SelectUserById(id);
        return Result.success(Vo);
    }
}
