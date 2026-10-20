package single.seckillv2.Service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import single.seckillv2.Commen.BusinessException;
import single.seckillv2.Entity.User;
import single.seckillv2.Entity.VO.UserVO;
import single.seckillv2.Mapper.UserMapper;
import single.seckillv2.Service.UserService;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    //注册
    @Override
    public UserVO register(String username, String password){
        if(username == null || username.trim().isEmpty()){
            throw new BusinessException("用户名不能为空");
        }
        if(password == null || password.length() < 6){
            throw new BusinessException("密码长度不能小于6位");
        }
        //判断用户名是否重复
        boolean exits = lambdaQuery().eq(User::getUsername,username).exists();
        if(exits){
            throw new BusinessException("用户名已存在");
        }
        //为密码加密
        String encryptedPassword = passwordEncoder.encode(password);
        //创建
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setCreateTime(LocalDateTime.now());
        try{
            save(user);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("用户名重复");
        }
        UserVO Vo = new UserVO();
        BeanUtils.copyProperties(user,Vo);
        return Vo;
    }

    //登录
    @Override
    public UserVO Login(String username,String password){
        //验证用户
        User user = lambdaQuery()
                .eq(User::getUsername,username)
                .one();
        if(user == null){
            throw new BusinessException("用户名或者密码错误");
        }
        //验证密码
        boolean match = passwordEncoder.matches(password,user.getPassword());
        if(!match){
            throw new BusinessException("用户名或者密码错误");
        }
        UserVO Vo = new UserVO();
        BeanUtils.copyProperties(user,Vo);
        return Vo;
    }

    //根据id查用户信息（不返回密码）
    @Override
    public UserVO SelectUserById(Integer userId){
        User user = getById(userId);
        if(user == null){
            throw new BusinessException("用户不存在");
        }
        UserVO Vo = new UserVO();
        BeanUtils.copyProperties(user,Vo);
        return Vo;
    }

}
