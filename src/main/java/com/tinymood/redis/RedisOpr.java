package com.tinymood.redis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Redis的基本使用
 *
 * @author hztaoran
 */
public class RedisOpr {

    private Jedis jedis;

    @Before
    public void setup() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    /**
     * redis操作字符串
     */
    @Test
    public void testString() {
        System.out.println("==========testString==========");
        jedis.del("name", "age", "qq");
        jedis.set("name", "saber");
        System.out.println(jedis.get("name"));

        jedis.append("name", " is my love");
        System.out.println(jedis.get("name"));

        jedis.del("name");
        System.out.println(jedis.get("name"));

        //设置多个键值对
        jedis.mset("name", "saber", "age", "20", "qq", "12345");
        jedis.incr("age"); //进行加1操作
        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
    }

    /**
     * redis操作List
     */
    @Test
    public void testList() {
        System.out.println("==========testList==========");
        jedis.del("java framework");
        System.out.println(jedis.lrange("java framework", 0, -1));
        jedis.lpush("java framework", "spring");
        jedis.lpush("java framework", "struts");
        jedis.lpush("java framework", "hibernate");
        System.out.println(jedis.lrange("java framework", 0, -1));

        jedis.del("java framework");
        jedis.rpush("java framework", "spring");
        jedis.rpush("java framework", "struts");
        jedis.rpush("java framework", "hibernate");
        System.out.println(jedis.lrange("java framework", 0, -1));
    }

    /**
     * 头插尾插混合
     * @throws InterruptedException
     */
    @Test
    public void testList2() throws InterruptedException {
        System.out.println("===========testList2=========");
        //jedis 排序
        //注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）
        jedis.del("a");//先清除数据，再加入数据进行测试
        jedis.rpush("a", "1");
        jedis.lpush("a", "6");
        jedis.lpush("a", "3");
        jedis.lpush("a", "9");
        System.out.println(jedis.lrange("a", 0, -1)); // [9, 3, 6, 1]
        System.out.println(jedis.sort("a")); //[1, 3, 6, 9]  //输入排序后结果
        System.out.println(jedis.lrange("a", 0, -1)); // [9, 3, 6, 1]
    }

    /**
     * redis操作Set
     */
    @Test
    public void testSet() {
        System.out.println("==========testSet==========");
        jedis.del("servant");
        // 添加
        jedis.sadd("servant", "saber");
        jedis.sadd("servant", "archer");
        jedis.sadd("servant", "assassin");
        jedis.sadd("servant", "noname");
        jedis.srem("servant", "noname");

        System.out.println(jedis.smembers("servant")); //获取所有加入的value
        System.out.println(jedis.sismember("servant", "noname"));//判断 who 是否是user集合的元素
        System.out.println(jedis.srandmember("servant"));
        System.out.println(jedis.scard("servant")); //返回集合的元素个数
    }

    /**
     * redis操作SortedSet
     */
    @Test
    public void testSortedSet() {
        System.out.println("==========testSortedSet==========");
        jedis.del("servant");
        // 添加
        jedis.zadd("servant", 1.0, "saber");
        jedis.zadd("servant", 2.0, "archer");
        jedis.zadd("servant", 3.0, "assassin");
        jedis.zadd("servant", 4.0, "noname");
        jedis.zrem("servant", "noname");

        System.out.println(jedis.zscore("servant", "saber"));
        System.out.println(jedis.zscore("servant", "archer"));
        System.out.println(jedis.zscore("servant", "assassin"));
        System.out.println(jedis.zscore("servant", "noname"));
        System.out.println(jedis.zrank("servant", "saber"));
        System.out.println(jedis.zrank("servant", "archer"));
        System.out.println(jedis.zrank("servant", "assassin"));
        System.out.println(jedis.zrank("servant", "noname"));

        System.out.println(jedis.zrange("servant", 0 , -1));
        System.out.println(jedis.zrevrange("servant", 0, -1));
        System.out.println(jedis.zrangeByScore("servant", 1.0, 2.0));
        System.out.println(jedis.zrangeByScore("servant", Double.MIN_VALUE, Double.MAX_VALUE));
        jedis.zscore("servant", "archer");
        System.out.println(jedis.zrevrangeByScore("servant", 5.0, 3.0));
        System.out.println(jedis.zcard("servant"));
    }

    /**
     * redis操作Map
     */
    @Test
    public void testMap() {
        System.out.println("==========testMap==========");
        jedis.del("servant");
        // ----- 添加数据 -----
        Map<String, String> map = new HashMap<>();
        map.put("name", "archer");
        map.put("age", "22");
        map.put("qq", "23456");
        jedis.hmset("servant", map);

        List<String> res = jedis.hmget("servant", "name", "age", "qq");
        System.out.println(res);

        // ----- 删除map中的键 -----
        jedis.hdel("servant", "age");
        System.out.println(jedis.hmget("user", "age")); //因为删除了，所以返回的是null
        System.out.println(jedis.hlen("servant")); //获取该Key所包含的Field的数量。
        System.out.println(jedis.exists("servant"));//是否存在指定key，返回true
        System.out.println(jedis.hkeys("servant"));//指定key的所有field
        System.out.println(jedis.hvals("servant"));//指定key的所有value

        Iterator<String> it = jedis.hkeys("servant").iterator();
        while (it.hasNext()) {
            String field = it.next();
            System.out.println(field + ":" + jedis.hget("servant", field));
        }
    }

    @Test
    public void testRedisPool() {
        RedisUtil.getJedis().set("newname", "中文测试");
        System.out.println(RedisUtil.getJedis().get("newname"));
    }
}
