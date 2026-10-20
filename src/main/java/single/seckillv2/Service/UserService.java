package single.seckillv2.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import single.seckillv2.Entity.User;
import single.seckillv2.Entity.VO.UserVO;

public interface UserService  extends IService<User> {
    UserVO register(String username, String password);
    UserVO Login(String username,String password);
    UserVO SelectUserById(Integer userId);

}
