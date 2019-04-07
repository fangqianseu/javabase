package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 使用 pool 获取 jedis 客户端
 */
public class JedisPoolDemo {
    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(50);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "localhost",6379);

        for (int i = 0; i < 10; i++) {
            Jedis jedis = jedisPool.getResource();
            jedis.select(10);

            jedis.set("k" + i, "asdasd");

            jedis.close();
        }

        jedisPool.close();

    }
}
