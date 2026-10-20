package single.seckillv2.Service;

public interface RedisService {
    void set(String key,Object value);
    Object get(String key);
    Long incr(String key);
    void delete(String key);
    void set(String key,Object value,Long timeoutSeconds);
    Long decr(String stockKey);


}
