/*
Date: 04/07,2019, 16:53
*/
package redis;

import redis.clients.jedis.Jedis;

public class JedisSet {

    public static void main(String[] args) {
        Jedis jedis = new Jedis();

        String k1 = "Set1";
        String k2 = "Set2";

        jedis.sadd(k1, "k1");
        jedis.sismember(k1, "v1");
        jedis.srem(k1, "v1"); // delete
        jedis.smembers(k1);   // return all members
        jedis.scard(k1);  // return number of members

        jedis.smove(k1,k2,"v1");  // move v1 in k1 to k2

        // 集合运算
        jedis.sunion(k1,k2);  // 并
        jedis.sinter(k1,k2);  // 交
        jedis.sdiff(k1,k2);   // 差  针对 k1


        jedis.close();
    }
}
