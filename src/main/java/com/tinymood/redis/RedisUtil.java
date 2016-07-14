package com.tinymood.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis连接池
 */
public class RedisUtil {

    // 服务器
    private static String host = "127.0.0.1";

    // redis端口号
    private static int port = 6379;

    //最大连接数数目，默认值为8
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_TOTAL = 32;

    //最大空闲连接数, 默认8个
    private static int MAX_IDLE = 8;

    //最少空闲连接数, 默认8个
    private static int MIN_IDLE = 0;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，小于零:阻塞不确定的时间。如果超过等待时间，则直接抛出JedisConnectionException
    private static int MAX_WAIT_MILLIS = -1;

    //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
    private static int minEvictableIdleTimeMillis = 1800000; // 30mins

    // 每次逐出检查时 逐出的最大数目
    private static int numTestsPerEvictionRun = 3;

    //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲连接数时直接逐出
    // 不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
    private static int softMinEvictableIdleTimeMillis = 1800000;

    //在获取连接的时候检查有效性, 默认false
    private static boolean TEST_ON_BORROW = false;

    // 在return给pool时，是否提前进行validate操作
    private static boolean TEST_ON_RETURN = false;

    //在空闲时检查有效性, 默认false
    private static boolean TEST_WHILE_IDLE = false;

    //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
    private static int timeBetweenEvictionRunsMillis = -1;

    private static int timeout = 3000;

    // 当“连接池”中active数量达到阀值时，即“链接”资源耗尽时，连接池需要采取的手段, 默认为1：
    // 0 : 抛出异常，
    // 1 : 阻塞，直到有可用链接资源
    // 2: 强制创建新的链接资源

    //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
    private static boolean blockWhenExhausted = true;

    private static JedisPool jedisPool = null;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAX_TOTAL);
        config.setMaxIdle(MAX_IDLE);
        config.setMinIdle(MIN_IDLE);
        config.setMaxWaitMillis(MAX_WAIT_MILLIS);
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, host, port, timeout);
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public synchronized static Jedis getJedis() {
        if (null != jedisPool) {
            Jedis resource = jedisPool.getResource();
            return resource;
        } else {
            return null;
        }
    }

    /**
     * 释放Jedis资源
     *
     * @param jedis
     */
    public static void returnResources(final Jedis jedis) {
        if (null != jedis) {
            jedisPool.returnResource(jedis);
        }
    }
}
