package com.lisihong.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public record RedisUtils(JedisPool jedisPool) {
    public Long hSet(String key, String field, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long hLen(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hlen(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String hGet(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long hDel(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hdel(key, field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}