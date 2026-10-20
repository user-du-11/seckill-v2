package single.seckillv2.Service.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import single.seckillv2.Service.RedisService;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    //创建Redis操作
    //1.SET key value
    public void set(String key,Object value){
        redisTemplate.opsForValue().set(key,value);
    }
    //2.GET key
    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }
    //3.INCR key
    public Long incr(String key){
        return redisTemplate.opsForValue().increment(key);
    }
    //4.delete key
    public void delete(String key){
        redisTemplate.delete(key);
    }
    //5.SET key value EX timeoutSeconds
    public void set(String key,Object value,Long timeoutSeconds){
        redisTemplate.opsForValue().set(key,value,timeoutSeconds, TimeUnit.SECONDS);
    }
    //6.Lua原子预减库存
    public Long decr(String stockKey){
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("scripts/seckill-deduct.lua")
        ));
        script.setResultType(Long.class);
        //执行Lua脚本
        return redisTemplate.execute(script, Collections.singletonList(stockKey));
    }
}
