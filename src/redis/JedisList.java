/*
Date: 04/07,2019, 16:44
*/
package redis;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

public class JedisList {
    public static void main(String[] args){
        Jedis jedis = new Jedis();

        jedis.lpush("list","v1","v2");
        jedis.rpush("list","v3","v4");

        jedis.brpop("list",0);  // 右侧阻塞弹出 0为等待时间 s，常用于阻塞队列
        jedis.blpop("list",0);  // 左侧阻塞弹出
        
        jedis.lpop("list");
        jedis.rpop("list");

        jedis.lindex("list",1);

        jedis.lset("list",1,"k3");

        jedis.lrange("list",0,-1);  // 获取全部元素
        jedis.llen("list");

        jedis.linsert("list",BinaryClient.LIST_POSITION.BEFORE,"k1","before");
        jedis.linsert("list",BinaryClient.LIST_POSITION.AFTER,"k1","after");

        //不新建list，存在才添加
        jedis.lpushx("list","v1");
        jedis.rpushx("list","v2");

        jedis.close();
    }
}
