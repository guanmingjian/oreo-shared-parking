package cn.oreo.server.order.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 指定缓存失效时间
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time){
        try {
            if(time > 0){
                redisTemplate.expire(key,time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据key获得过期时间
     * @param key
     * @return
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }


    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除缓存
     * @param key   可以传多个key
     */
    public void del(String... key){
        if(key!=null && key.length>0){
            if(key.length == 1){
                redisTemplate.delete(key[0]);
            } else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    //===============================String==============================================
    /**
     * 普通缓存获取
     * @param key
     * @return 值
     */
    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }


    /**
     * 普通缓存放入
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value){
        try {
            redisTemplate.opsForValue().set(key,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param key
     * @param value
     * @param time
     * @return true:成功  false:失败
     */
    public boolean set(String key, Object value, long time){
        try {
            if(time > 0){
                redisTemplate.opsForValue().set(key,value,time,TimeUnit.SECONDS);
            } else{
                set(key,value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key       键
     * @param delta     需要增加几？
     * @return
     */
    public long incr(String key,long delta){
        if(delta < 0){
            throw new RuntimeException("递增因子必须大于0");
        }else{
            return redisTemplate.opsForValue().increment(key,delta);
        }
    }

    /**
     * 递减
     * @param key
     * @param delta
     * @return
     */
    public long decr(String key,long delta){
        if(delta < 0){
            throw new RuntimeException("递减因子必须大于0");
        } else {
            return redisTemplate.opsForValue().decrement(key,delta);
        }
    }

    //======================Map========================================

    /**
     *  HashGet
     * @param key   key
     * @param item  hashKey
     * @return
     */
    public Object hget(String key, String item){
        return redisTemplate.opsForHash().get(key,item);
    }

    /**
     * 获取HashKey对应的所有键值
     * @param key
     * @return  对应的多个键值
     */
    public Map<Object,Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key
     * @param map
     * @return
     */
    public boolean hmset(String key,Map<String, Object> map){
        try {
            redisTemplate.opsForHash().putAll(key,map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * HashSet并设置时间
     * @param key
     * @param map
     * @param time
     * @return
     */
    public boolean hmset(String key, Map<String,Object> map,long time){
        try {
            redisTemplate.opsForHash().putAll(key,map);
            if(time > 0){
                expire(key,time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入一个数据，如不存在则创建
     * @param key       键
     * @param item      HashKey
     * @param value     HashValue
     * @return
     */
    public boolean hset(String key, String item,Object value){
        try {
            redisTemplate.opsForHash().put(key,item,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 并设置时间
     * @param key
     * @param item
     * @param value
     * @param time
     * @return
     */
    public boolean hset(String key, String item,Object value,long time){
        try {
            redisTemplate.opsForHash().put(key,item,value);
            if(time > 0 ){
                expire(key,time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的 key
     * @param key
     * @param item      HashKey
     */
    public void hdel(String key,Object... item){
        redisTemplate.opsForHash().delete(key,item);
    }

    /**
     * 判断hash表中是否存在item
     * @param key       key
     * @param item      hashkey
     * @return
     */
    public boolean hHasKey(String key,String item){
        return redisTemplate.opsForHash().hasKey(key,item);
    }

    /**
     * hash递增
     * @param key
     * @param item
     * @param by
     * @return
     */
    public double hincr(String key, String item,double by){
        return redisTemplate.opsForHash().increment(key,item,by);
    }

    /**
     * hash递减
     * @param key
     * @param item
     * @param by
     * @return
     */
    public double hdecr(String key, String item,double by){
        return redisTemplate.opsForHash().increment(key,item,by);
    }

    //===========================set==========================================

    /**
     * 根据key获取Set中所有的值
     * @param key
     * @return
     */
    public Set<Object> sGet(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询是否存在
     * @param key
     * @param value
     * @return
     */
    public boolean sHashKey(String key,Object value){
        try {
            return redisTemplate.opsForSet().isMember(key,value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key
     * @param values
     * @return
     */
    public long sSet(String key,Object... values){
        try {
            return redisTemplate.opsForSet().add(key,values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 并设置时间
     * @param key
     * @param time
     * @param values
     * @return
     */
    public long sSet(String key,long time,Object... values){
        try {
            Long count = redisTemplate.opsForSet().add(key,values);
            if(time > 0){
                expire(key,time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set的大小
     * @param key
     * @return
     */
    public long sGetSetSize(String key){
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key
     * @param values
     * @return
     */
    public long setRemove(String key, Object... values){
        try {
            Long count = redisTemplate.opsForSet().remove(key,values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ============================list======================================

    /**
     * 获取list缓存的内容
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> lGet(String key, long start,long end){
        try {
            return redisTemplate.opsForList().range(key,start,end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的大小
     * @param key
     * @return
     */
    public long lGetListSize(String key){
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引，获取list中的值
     * @param key
     * @param index
     * @return
     */
    public Object lGetIndex(String key,long index){
        try {
            return redisTemplate.opsForList().index(key,index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 往list放入值
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key,Object value){
        try {
            redisTemplate.opsForList().rightPush(key,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 并设置过期时间
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean lSet(String key,Object value,long time){
        try {
            redisTemplate.opsForList().rightPush(key,value);
            if(time > 0){
                expire(key,time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入list
     * @param key
     * @param values
     * @return
     */
    public boolean lSet(String key,List<Object> values){
        try {
            redisTemplate.opsForList().rightPushAll(key,values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 并设置时间
     * @param key
     * @param values
     * @param time
     * @return
     */
    public boolean lSet(String key,List<Object> values,long time){
        try {
            redisTemplate.opsForList().rightPushAll(key,values);
            if(time > 0){
                expire(key,time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据index修改值
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean lUpdateIndex(String key,long index, Object value){
        try {
            redisTemplate.opsForList().set(key,index,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除 n 个值为 value
     * @param key
     * @param count
     * @param value
     * @return
     */
    public long lRemove(String key,long count, Object value){
        try {
            Long remove = redisTemplate.opsForList().remove(key,count,value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }








}
