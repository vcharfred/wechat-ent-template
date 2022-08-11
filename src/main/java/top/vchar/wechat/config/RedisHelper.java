package top.vchar.wechat.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p> redis工具类 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2021/1/27
 */
@Component
public class RedisHelper {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisHelper(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置值
     *
     * @param key       缓存key
     * @param value     值
     * @param validTime 缓存时间，单位秒
     * @param <T>       Class
     * @return 返回操作结果
     */
    public <T> boolean set(String key, T value, long validTime) {
        if (value instanceof String) {
            return set(key, (String) value, validTime);
        }
        return set(key, JSON.toJSONString(value), validTime);
    }

    /**
     * 设置值
     *
     * @param key       缓存key
     * @param value     值
     * @param validTime 缓存时间，单位秒
     * @return 返回操作结果
     */
    private boolean set(String key, String value, long validTime) {
        Boolean res = this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
            byte[] keyByte = Objects.requireNonNull(serializer.serialize(key));
            byte[] valueByte = Objects.requireNonNull(serializer.serialize(value));
            connection.set(keyByte, valueByte);
            connection.expire(keyByte, validTime);
            return true;
        });
        return res != null && res;
    }



    /**
     * 获取值
     *
     * @param key   缓存key
     * @param clazz 反序列的Class
     * @param <T>   T
     * @return 返回单个结果
     */
    public <T> T get(String key, Class<T> clazz) {
        return JSON.parseObject(getValue(key), clazz);
    }

    /**
     * 获取值
     *
     * @param key   缓存key
     * @param clazz 反序列的Class
     * @param <T>   T
     * @return 返回List
     */
    public <T> List<T> getList(String key, Class<T> clazz) {
        return JSONArray.parseArray(getValue(key), clazz);
    }

    /**
     * 获取值
     *
     * @param key 缓存key
     * @return 返回单个结果
     */
    public String get(String key) {
        return getValue(key);
    }

    /**
     * 获取值
     *
     * @param key 缓存key
     * @return 返回单个结果
     */
    private String getValue(String key) {
        return this.redisTemplate.execute((RedisCallback<String>) connection -> {
            RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
            byte[] value = connection.get(Objects.requireNonNull(serializer.serialize(key)));
            return serializer.deserialize(value);
        });
    }

    /**
     * 删除值
     *
     * @param key 缓存key
     */
    public void del(String key) {
        this.redisTemplate.delete(key);
    }

    /**
     * 判断值缓存key是否存在
     *
     * @param key 缓存key
     */
    public boolean exist(String key) {
        Boolean res = this.redisTemplate.hasKey(key);
        return res != null && res;
    }

    /**
     * 如果key不存在则设置，此方法使用了redis的原子性
     *
     * @param key       key
     * @param value     value
     * @param validTime 缓存时间; 若要是使用锁，建议设置有效时间，避免死锁
     * @return key不存在设置成功返回true; 否则返回false
     */
    public boolean setNx(String key, String value, long validTime) {
        return setNx(key, value, validTime, TimeUnit.SECONDS);
    }

    /**
     * 如果key不存在则设置，此方法使用了redis的原子性
     *
     * @param key       key
     * @param value     value
     * @param validTime 缓存时间; 若要是使用锁，建议设置有效时间，避免死锁
     * @param timeUnit  时间单位
     * @return key不存在设置成功返回true; 否则返回false
     */
    public boolean setNx(String key, String value, long validTime, TimeUnit timeUnit) {
        try {
            ValueOperations<String, String> operations = this.redisTemplate.opsForValue();
            Boolean lock = operations.setIfAbsent(key, value, validTime, timeUnit);
            return lock != null && lock;
        } catch (Exception e) {
            this.del(key);
            e.printStackTrace();
        }
        return false;
    }

    public boolean lock(String key, long lockTime){
        return setNx(key, "1", lockTime);
    }

}
