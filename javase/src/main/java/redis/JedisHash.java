/*
Date: 04/07,2019, 17:00
*/
package redis;

import redis.clients.jedis.Jedis;

public class JedisHash {
    public static void main(String[] args){
        Jedis jedis = new Jedis();

        String hashKey = "hashkey";

        jedis.hset(hashKey,"k1","v1");
        jedis.hset(hashKey,"k2","v2");
        jedis.hset(hashKey,"k3","v3");

        jedis.hexists(hashKey,"k1");
        jedis.hget(hashKey,"k1");
        jedis.hgetAll(hashKey);

        jedis.hdel(hashKey,"k1");

        jedis.hsetnx(hashKey,"k1", "v4");  // 没有属性 则设置值

        jedis.hlen(hashKey); //

        jedis.hkeys(hashKey);
        jedis.hvals(hashKey);

        jedis.close();
    }
}
