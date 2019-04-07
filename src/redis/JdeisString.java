/*
Date: 04/07,2019, 16:35
*/
package redis;

import redis.clients.jedis.Jedis;

public class JdeisString {
    public static void main(String[] args) {
        Jedis jedis = new Jedis();

        jedis.set("key", "velues");
        jedis.rename("key", "key1");
        jedis.get("key1");

        // set if not exist
        jedis.setnx("key", "v");
        // 过期时间 单位 s
        jedis.setex("k2", 15, "v2");

        // 自增长
        jedis.set("pv", "100");
        jedis.incr("pv");
        jedis.incrBy("pv",5);

        // 自减少
        jedis.decr("pv");
        jedis.decrBy("pv",5);

        jedis.append("key","v");

        // 获取指定长度的 value
        jedis.getrange("key",0,1);
        // 将 从 index 开始 的value 值替换
        jedis.setrange("key",1,"hh");

        jedis.close();
    }
}
