package com.abc.cx.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ChangXuan
 * @Decription: Redis 工具类
 * @Date: 22:16 2021/11/6
 **/
@Component
@Slf4j
public class RedisUtil {

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean setIfAbsent(String key, String value, int second) {
        Boolean ret = false;
        try {
            ret = redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(second));
        } catch (Exception e) {
            log.error("设置key: " + key + ",value: " + value, e);
        }
        return ret;
    }

    /**
     * 查找 key 是否存在
     * @param key key
     * @return 是否存在
     */
    public Boolean hasKey(String key) {
        Boolean ret = false;
        try {
            ret = redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("查找key：" + key, e);
        }
        return ret;
    }

    /**
     * 设置 String
     * @param key
     * @param value
     * @return
     */
    public boolean setString(String key, String value){
        try{
            redisTemplate.opsForValue().set(key, value);
        }catch (Exception e){
            log.error("设置key: " + key + ",value: " + value, e);
            return false;
        }
        return true;
    }

    /**
     * 设置 过期时间
     *
     * @param key
     * @param seconds 以秒为单位
     * @param value
     */
    public boolean setString(String key, String value, long seconds) {
        try {
            redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error( "设置key: " + key + ",value: " + value + ",seconds: " + seconds,e);
            return false;
        }
        return true;
    }

    /**
     * 获取 String 值
     * @param key
     * @return
     */
    public String getString(String key){
        String value = null;
        try{
            Object obj = redisTemplate.opsForValue().get(key);
            value = obj != null ? obj.toString() : null;
        }catch (Exception e){
            log.error( "获取key值: " + key ,e);
        }
        return value;
    }


    /**
     * 缓存对象
     * @param key
     * @param obj
     * @param <T>
     */
    public <T> void setObject(String key, T obj){
        try{
            redisTemplate.opsForValue().set(key, obj);
        }catch (Exception e){
            log.error("设置key: " + key + ",value: " + obj, e);
        }
    }

    /**
     * 设置缓存有效期
     * @param key
     * @param obj
     * @param expireSec
     * @param <T>
     */
    public <T> void setObject(String key, T obj, int expireSec){
        try{
            redisTemplate.opsForValue().set(key, obj, expireSec, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error("设置key: " + key + ",value: " + obj, e);
        }
    }

    /**
     * 根据 key 值获取对象
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getObject(String key){
        try {
            return (T)redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            log.error( "获取key值: " + key ,e);
        }
        return null;
    }

    /**
     * 删除对象
     * @param key
     */
    public void deleteObject(String key){
        try {
            redisTemplate.delete(key);
        }catch (Exception e){
            log.error("删除key值: " + key ,e);
        }
    }


    /**
     * redis获取所有key
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern){
        Set<String> result = null;
        try{
            result = redisTemplate.keys(pattern);
        }catch (Exception e){
            log.error("获取keys信息失败,pattern:" + pattern + "失败原因:", e);
        }
        return result;
    }

    /**
     * redis 删除 key
     * @param key
     * @return
     */
    public Long del(String key){
        Long result = null;
        Set<String> keys = new HashSet<String>();
        keys.add(key);
        try{
            result = redisTemplate.delete(keys);
        }catch (Exception e){
            log.error("删除key信息失败,key:" + key + "失败原因:", e);
        }
        return result;
    }

    /**
     * redis 重命名key
     * 注：原来返回String值
     * @param oldKey
     * @param newKey
     * @return
     */
    public void del(String oldKey, String newKey){
        String result = null;
        try {
            redisTemplate.rename(oldKey, newKey);
        }catch (Exception e){
            log.error("更新key信息失败,oldkey:" + oldKey + "失败原因:",e);
        }
    }

    /**
     * 批量插入
     * @param key
     * @param map
     * @param <T>
     */
    public <T> void hmsetObject(String key, Map<String, T> map){
        try {
            redisTemplate.opsForHash().putAll(key, map);
        }catch (Exception e){
            log.error("设置key: " + key + " 失败，失败原因", e);
        }
    }

    /**
     * redis 设置 hash 值
     * @param key
     * @param map
     */
    public void hmset(String key, Map<String, String> map){
        try {
            redisTemplate.opsForHash().putAll(key, map);
        }catch (Exception e){
            log.error("设置key: " + key + " 失败，失败原因", e);
        }
    }

    /**
     * redis-hset
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key, String field, String value){
        try {
            redisTemplate.opsForHash().put(key, field, value);
        }catch (Exception e){
            log.error("设置key: " + key + ",field:"+ field +",value:"+value+" 失败，失败原因", e);
        }
    }

    /**
     * redis-hget
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field){
        String value = null;
        try {
            Object obj = redisTemplate.opsForHash().get(key, field);
            value = obj != null ? obj.toString() : null;
        }catch (Exception e){
            log.error("获取key: " + key + ",field:"+ field +" 失败，失败原因", e);
        }
        return value;
    }

    public <T> T hgetObject(String key, String field){
        try {
            return (T) redisTemplate.opsForHash().get(key, field);
        } catch (Exception e) {
            log.error("获取key: " + key + ",field:" + field + " 失败，失败原因", e);
            return null;
        }
    }

    public <T> void hsetObject(String key, String field, T value){
        try {
            redisTemplate.opsForHash().put(key, field, value);
        }catch (Exception e){
            log.error("设置key: " + key + ",field:"+ field +",value:"+value+" 失败，失败原因", e);
        }
    }

    public <T> List<T> hmgetObject(String key, String... fields){
        if (fields.length != 0) {
            try {
                List<T> ret = new ArrayList<>();
                for (String itemFields : fields){
                    ret.add((T) redisTemplate.opsForHash().get(key, itemFields));
                }
                return ret;
            }catch (Exception e){
                log.error("获取keys信息失败,key:" + key + "失败原因:", e);
            }
        }
        return null;
    }

    /**
     * redis hmget
     * @param key
     * @param fields
     * @return
     */
    public List<String> hmget(String key, String... fields){
        List<String> values = new ArrayList<>();
        if (fields.length != 0){
            try {
                for (String fieldsItem : fields){
                    Object obj = redisTemplate.opsForHash().get(key, fieldsItem);
                    values.add(obj != null ? obj.toString() : null);
                }
            }catch (Exception e){
                log.error("获取keys信息失败,key:" + key + "失败原因:", e);
            }
        }
        return values;
    }

    /**
     * redis hget all
     * @param key
     * @return
     */
    public Map hgetall(String key){
        Map valueMap = null;
        try{
            valueMap = redisTemplate.opsForHash().entries(key);
        }catch (Exception e){
            log.error("获取keys信息失败,key:" + key + "失败原因:", e);
        }
        return valueMap;
    }

    /**
     * 删除散列中的指定field
     * @param key
     * @param field
     * @return
     */
    public boolean hdel(String key, String field){
        long result = 0;
        try{
            result = redisTemplate.opsForHash().delete(key, field);
        }catch (Exception e){
            log.error("删除keys信息失败,key:" + key + "field:"+field+" 失败原因:", e);
        }
        return result != 0;
    }

    /**
     * redis hvals
     * @param key
     * @return
     */
    public List<String> hvals(String key){
        List<String> result = new ArrayList<>();
        try{
            List<Object> objects =  redisTemplate.opsForHash().values(key);
            for (Object valueItem: objects){
                result.add(valueItem != null ? valueItem.toString() : null);
            }
        }catch (Exception e){
            log.error( "再次hvals信息失败,key:" + key + "失败原因:",e);
        }
        return result;
    }

    public <T> List<T> hvalsObject(String key) {
        List<T> result = new ArrayList<>();
        try{
            List<Object> objects = redisTemplate.opsForHash().values(key);
            for (Object valueItem: objects){
                result.add((T) valueItem);
            }
        }catch (Exception e){
            log.error( "再次hvals信息失败,key:" + key + "失败原因:",e);
        }
        return result;
    }

    public void zadd(String key, double score, String member){
        try {
            redisTemplate.opsForZSet().add(key, member, score);
        }catch (Exception e){
            log.error("sadd设置信息失败,key:" + key + "失败原因:",e);
        }
    }

    /**
     * sadd 命令将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略。
     * 假如集合 key 不存在，则创建一个只包含添加的元素作成员的集合。
     * 当集合 key 不是集合类型时，返回一个错误。
     * @param key
     * @param value
     */
    public void sadd(String key, String value){
        try {
            redisTemplate.opsForSet().add(key, value);
        }catch (Exception e){
            log.error("sadd设置信息失败,key:" + key + "失败原因:",e);
        }
    }

    /**
     * 添加多个值
     * @param key
     * @param values
     */
    public void sadd(String key, String... values){
        try {
            redisTemplate.opsForSet().add(key, values);
        }catch (Exception e){
            log.error("sadd设置信息失败,key:" + key + "失败原因:",e);
        }
    }

    /**
     * Redis Srem 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。
     * 当 key 不是集合类型，返回一个错误。
     * @param key
     * @param value
     */
    public void srem(String key, String value){
        try {
            redisTemplate.opsForSet().remove(key, value);
        }catch (Exception e){
            log.error("srem移除信息失败,key:" + key + "失败原因:",e);
        }
    }

    /**
     *  Redis Zrem 命令用于移除有序集中的一个或多个成员，不存在的成员将被忽略。
     * 当 key 存在但不是有序集类型时，返回一个错误。
     * @param key
     * @param members
     */
    public void zrem(String key, String members){
        try {
            redisTemplate.opsForZSet().remove(key, members);
        }catch (Exception e){
            log.error("zrem移除信息失败,key:" + key + "失败原因:",e);
        }
    }

    /**
     * [startScore,endScore]
     * @param key
     * @param startScore
     * @param endScore
     */
    public void zremrangeByScore(String key, Long startScore, Long endScore) {
        // TODO 测试
        try {
            redisTemplate.opsForZSet().removeRangeByScore(key, startScore, endScore);
        }catch (Exception e){
            log.error("zremrangeByScore移除信息失败,key:" + key + "失败原因:",e);
        }
    }


    public Set<ZSetOperations.TypedTuple> zrangeByScoreWithScores(String key, double min, double max){
        //TODO 测试
        Set result = null;
        try {
            result = redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
        }catch (Exception e){
            log.error("zrangeByScoreWithScores信息失败,key:" + key + "失败原因:", e);
        }
        return result;
    }


    /**
     * [min,max], 非 (min,max)
     * @param key
     * @param min
     * @param max
     * @return
     */
    public long zcount(String key, long min, long max){
        Long count = 0L;
        try {
            count = redisTemplate.opsForZSet().count(key, min, max);
        }catch (Exception e){
            log.error("zcount信息失败,key:" + key + "失败原因:", e);
        }
        return count;
    }

    /**
     *  Smembers 命令返回集合中的所有的成员。 不存在的集合 key 被视为空集合
     * @param key
     * @return
     */
    public Set<String> smembers(String key){
        Set<String> set = new HashSet<>();
        try {
            Set<Object> objects = redisTemplate.opsForSet().members(key);
            for (Object item: objects){
                set.add(item != null ? item.toString(): null);
            }
        }catch (Exception e){
            log.error("smembers获取信息失败,key:" + key + "失败原因:",e);
        }
        return set;
    }

    public  Set<String> sinter(String... keys) {
        Set<String> set = new HashSet<String>();
        Set<String> keySet = new HashSet<String>();
        if (keys.length != 0) {
            for (String keyItem : keys){
                keySet.add(keyItem);
            }
            try {
                Set<Object> objects = redisTemplate.opsForSet().intersect(keySet);
                for (Object setItem : objects){
                    set.add(setItem != null ? setItem.toString():null);
                }
            }catch (Exception e){
                log.error("sinter获取信息失败,key:" + Arrays.asList(keys) + "失败原因:", e);
            }
        }
        return set;
    }

    /**
     * Sismember 命令判断成员元素是否是集合的成员
     * @param key
     * @param value
     * @return
     */
    public Boolean sismember(String key, String value) {
        Boolean ret = null;
        try {
            ret = redisTemplate.opsForSet().isMember(key, value);
        }catch (Exception e){
            log.error("smembers获取信息失败,key:" + key + "失败原因:", e);
        }
        return ret;
    }

    /**
     * 向指定key的list的尾部push进strings
     *
     * @param key
     * @param strings
     * @return Long push进去的数目
     */
    public long rpush(String key, String strings){
        long rpushedNums = 0;
        try {
            rpushedNums = redisTemplate.opsForList().rightPush(key, strings);
        }catch (Exception e){
            log.error("rpush信息失败,key:" + key + " string:" + strings, e);
        }
        return rpushedNums;
    }

    /**
     * 向指定key的list的头部push进strings
     *
     * @param key
     * @param strings
     * @return Long push进去的数目
     */
    public long lpush(String key, String strings){
        long rpushedNums = 0;
        try {
            rpushedNums = redisTemplate.opsForList().leftPush(key, strings);
        }catch (Exception e){
            log.error("rpush信息失败,key:" + key + " string:" + strings, e);
        }
        return rpushedNums;
    }

    /**
     * 从指定key的list中获取并移除第一个元素,如果没有,返回null
     *
     * @param key
     * @return list中的第一个元素, 如果没有返回null
     */
    public String lpop(String key){
        String item = null;
        try {
            Object obj = redisTemplate.opsForList().leftPop(key);
            item = obj == null? null: obj.toString() ;
        }catch (Exception e){
            log.error("lpop信息失败,key:" + key, e);
        }
        return item;
    }

    /**
     * 从指定key的list中获取并移除最后一个元素,如果没有,返回null
     *
     * @param key
     * @return list中的最后一个元素, 如果没有返回null
     */
    public String rpop(String key){
        String item = null;
        try {
            Object obj = redisTemplate.opsForList().rightPop(key);
            item = obj == null? null: obj.toString() ;
        }catch (Exception e){
            log.error("rpop信息失败,key:" + key, e);
        }
        return item;
    }

    /**
     * 保留给定key的值范围内的数据
     *
     * @param key
     * @return 成功信息
     */
    public String ltrim(String key, long start, long end) {
        try {
            redisTemplate.opsForList().trim(key, start, end);
        }catch (Exception e){
            log.error("ltrim信息失败,key:" + key,e);
        }
        return "OK";
    }

    // 锁超时时间
    private static int LOCK_TIMEOUT = 2 * 60 * 1000;

    private void _unlock(String key, String identity){
        Object obj = redisTemplate.opsForValue().get(key);
        String value = obj != null ? obj.toString():null;
        if (value != null){
            if (value.split(",")[0].equals(identity)) {
                redisTemplate.delete(key);
            }
        }
    }

    private boolean _lock(String key, String identity){
        //获取redis时间（不使用本地时间的原因是因为分布式服务器的时间可能不同，而redis时间是一致的）
        Long currentTime = redisTemplate.getRequiredConnectionFactory().getConnection().time();
        //超时时间戳（当前时间+超时时间）
        String expire = String.valueOf(currentTime + LOCK_TIMEOUT);                        // 锁过期时间(毫秒)
        //重新加锁
        if (redisTemplate.opsForValue().setIfAbsent(key,identity + "," + expire).equals(true)) {
            return true;
        }
        //获取缓存中的锁
        Object obj = redisTemplate.opsForValue().get(key);
        String value = obj != null? obj.toString():null;
        if (value != null) {
            // 锁已过期
            if (Long.parseLong(value.split(",")[1]) < currentTime) {
                log.warn("锁过期,释放当前锁-key: " + key);
                unlock(key, identity);
                return _lock(key, identity);
            }
        }
        return false;
    }

    private boolean _lock(ArrayList<String> keys, String identity){
        ArrayList<String> lockedKeys = new ArrayList<String>();
        for (String key : keys) {
            //如果加锁成功
            if (_lock(key, identity)) {
                lockedKeys.add(key);
            } else {//否则将之前成功加锁的数据解锁
                for (String lockedKey : lockedKeys) {
                    _unlock(lockedKey, identity);
                }
                break;
            }
        }
        //如果全部加锁成功，返回true
        if (lockedKeys.size() == keys.size()) {
            return true;
        } else {
            return false;
        }
    }

    public void unlock(ArrayList<String> keys, String identity) {
        try {
            for (String key : keys) {
                _unlock( key, identity);
            }
        } catch (Exception e) {
            log.error( "批量释放锁操作失败,失败原因:",e);
            // 再次尝试
            try {
                for (String key : keys) {
                    _unlock( key, identity);
                }
            } catch (Exception ee) {
                log.error( "再次批量释放锁操作失败,失败原因:",ee);
            }
        }
    }

    public boolean lockb(String key, String identity) {
        return lockb(key, 10, identity);
    }

    /**
     * Jedis锁<br/>
     * 阻塞方法
     *
     * @param key      锁key
     * @param identity 锁标识，用于锁用完后的删除
     * @param sec      超时时间，超过这一时间还没获取锁，则返回false
     * @return
     */
    public boolean lockb(String key, int sec, String identity){
        try {
            int count = 0;
            while (!_lock(key, identity)) {
                Thread.sleep(200);
                count++;
                if (count > sec * 1000L / 200) {
                    if (sec != 0) {
                        log.warn("Redis获取锁超时");
                    }
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error("获取锁操作失败,key:" + key + "失败原因:",e);
            // 再次尝试
            try {
                int count = 0;
                while (!_lock(key, identity)) {
                    Thread.sleep(200);
                    count++;
                    if (count > sec * 1000L / 200) {
                        if (sec != 0) {
                            log.warn("获取锁超时");
                        }
                        return false;
                    }
                }
                return true;
            } catch (Exception ee) {
                log.error("再次获取锁操作失败,key:" + key + "失败原因:",ee);
            }
        }
        return false;
    }

    public void unlock(String key, String identity) {
        try {
            _unlock( key, identity);
        } catch (Exception e) {
            log.error("释放锁操作失败,key:" + key + "失败原因:",e);
            // 再次尝试
            try {
                _unlock( key, identity);
            } catch (Exception ee) {
                log.error( "再次释放锁操作失败,key:" + key + "失败原因:",ee);
            }
        }
    }

    /**
     * 用于分布式操作的锁方法，除了验证锁之外，还要保证一段时间内不会被调用多次<br/>
     * 非阻塞方法<br/>
     * <b>如果在interval时间内，有别人获取过锁，则不会返回锁</b>
     *
     * @param key      用于加锁的key
     * @param identity 唯一标识，用于释放锁
     * @param interval 排他时间（秒），每次获取锁要验证是否满足排他时间，如果不满足，也获取不到锁；<br/>
     *                 排他时间是为了实现异步启动同一个工作，在排他时间内只有一个能够执行<br/>
     *                 <b>排他时间建议设置为大于该工作完成一次的时间，小于真实的轮询时间，由于不同服务器开始轮询的时间不确定，该值越接近真实轮询时间越好</b>
     * @return
     */
    public boolean lock(String key, String identity, int interval) {
        return lockb(key, 0, identity, interval);
    }

    /**
     * 用于分布式操作的锁方法，除了验证锁之外，还要保证一段时间内不会被调用多次<br/>
     * 阻塞方法<br/>
     * <b>如果在interval时间内，有别人获取过锁，则不会返回锁</b>
     *
     * @param key      用于加锁的key
     * @param identity 唯一标识，用于释放锁
     * @param sec      超时时间（秒），超过这一时间还没获取锁，则返回false
     * @param interval 排他时间（秒），每次获取锁要验证是否满足排他时间，如果不满足，也获取不到锁；<br/>
     *                 排他时间是为了实现异步启动同一个工作，在排他时间内只有一个能够执行<br/>
     *                 <b>排他时间建议设置为大于该工作完成一次的时间，小于真实的轮询时间，由于不同服务器开始轮询的时间不确定，该值越接近真实轮询时间越好</b>
     * @return
     */
    public boolean lockb(String key, int sec, String identity, int interval) {
        try {
            int count = 0;
            while (!_lock(key, identity, interval)) {
                Thread.sleep(200);
                count++;
                if (count > sec * 1000L / 200) {
                    if (sec != 0) {
                        log.warn("获取锁超时");
                    }
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error( "获取锁操作失败,key:" + key + "失败原因:",e);
            // 再次尝试
            try {
                int count = 0;
                while (!_lock( key, identity, interval)) {
                    Thread.sleep(200);
                    count++;
                    if (count > sec * 1000L / 200) {
                        if (sec != 0) {
                            log.warn("获取锁超时");
                        }
                        return false;
                    }
                }
                return true;
            } catch (Exception ee) {
                log.error("再次获取操作失败,key:" + key + "失败原因:",ee);
            }
        }
        return false;
    }

    private boolean _lock( String key, String identity, int interval) {
        if (_lock( key, identity)) {
            String preTimeStr = redisTemplate.opsForValue().get(key + "t") != null ? redisTemplate.opsForValue().get(key + "t").toString():null;
            String nowTimeStr = redisTemplate.opsForValue().get(key).toString().split(",")[1];
            if (preTimeStr != null) {
                long preTime = Long.valueOf(preTimeStr);
                long nowTime = Long.valueOf(nowTimeStr);
                if (nowTime - preTime < interval * 1000L) {
                    _unlock(key, identity);
                    return false;
                }
            }
            setString(key + "t", nowTimeStr);
            return true;
        }
        return false;
    }

    /**
     * 为了防止锁过期，对于长时间占用的工作，要定期刷新锁过期时间
     *
     * @return
     */
    public boolean refreshLock(String key, String identity) {
        try {
            return _refreshLock( key, identity);
        } catch (Exception e) {
            log.error("刷新锁操作失败,key:" + key + "失败原因:",e);
            // 再次尝试
            try {
                return _refreshLock( key, identity);
            } catch (Exception ee) {
                log.error( "再次刷新操作失败,key:" + key + "失败原因:",ee);
            }
        }
        return false;
    }
    private boolean _refreshLock( String key, String identity) {
        Object obj = redisTemplate.opsForValue().get(key);
        String value = obj != null ? obj.toString() : null;
        if (value != null && value.split(",")[0].equals(identity)) {
            Long currentTime = redisTemplate.getRequiredConnectionFactory().getConnection().time();
            String expire = String.valueOf(currentTime + LOCK_TIMEOUT);                // 锁过期时间
            Object oldObj = redisTemplate.opsForValue().getAndSet(key, identity + "," + expire);
            String oldValue = oldObj != null ? oldObj.toString():null;
            if (oldValue != null && oldValue.equals(value)) {                        // 获取到了锁
                return true;
            }
        }
        return false;
    }
}
