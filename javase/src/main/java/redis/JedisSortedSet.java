/*
Date: 04/07,2019, 17:06
*/
package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class JedisSortedSet {
    public static void main(String[] args) {
        Jedis jedis = new Jedis();

        String key = "SortedKey";

        jedis.zadd(key, 10, "k1");
        jedis.zadd(key, 20, "k2");

        jedis.zscore(key, "k1");
        jedis.zincrby(key, 10, "k1"); // k1 score 增加 10


        jedis.zrange(key, 0, 100);  // score 在 0 到 100 中的元素, 由小到大
        jedis.zrevrange(key, 0, 100); // score 在 0 到 100 中的元素, 由大到小

        // score 在 60 到 100 之间 的元素 包含 value 和 score
        for (Tuple tuple : jedis.zrangeByScoreWithScores(key, 60, 100)) {
            tuple.getElement();
            tuple.getScore();
        }

        // 删除元素相关
        jedis.zrem(key,"k1");
        jedis.zremrangeByScore(key,10,20);

        jedis.zrank(key,"k1");  // 由小到大 位次
        jedis.zrevrank(key,"k1");

        jedis.zcard(key);  // set 中的 所有元素个数
        jedis.zcount(key, 1, 100);  // score 在 min 和 max 之间的 数目

        jedis.close();
    }
}
