package com.lori.guavademo.cache.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.lori.guavademo.cache.common.Student;

import java.util.concurrent.TimeUnit;

public class GuavaCache {

    public static final LoadingCache<Integer,Student> cache;

    static {
        cache = CacheBuilder.newBuilder()
                /**
                 * concurrencyLevel并发级别设置为2,表示可以同时写缓存的线程数
                 * 两个线程同时写入同一个value怎么处理?Key相同会进行覆盖;如果有移除回调监听,会进行通知
                 * 多个线程同时写入时怎么控制?并发级别是表示segment(段)的数量;LocalCache继承了ConcurrentMap,分段锁,提高并发度;
                 * 例如:并发级别为2,即segment为2,同时只有两个线程进行写入操作,并加锁,其他线程只能等待获取锁;
                 * 并发级别的不好测试,看源码了解
                 */
                .concurrencyLevel(2)
                //设置写缓存后8秒钟过期
                .expireAfterWrite(8, TimeUnit.SECONDS)
                /**
                 * initialCapacity容器初始化大小,比如容器初始化大小为10,那么加载因子threshold=10*0.75
                 * 超过加载因子,会进行扩容,扩大为原大小的两倍;
                 */
                .initialCapacity(10)
                //设置缓存最大容量为5，超过5之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(5)
                /**
                 * hitCount缓存的命中率
                 * 从缓存中获取数据时,如果不存在,进行加载,这是不算命中的,算miss,
                 * 存在,判断数据是否到期,如果到期也算miss,否则算hit
                 */
                .recordStats()
                /**
                 * removalListener缓存的移除监听
                 * 机制:
                 * put操作时,如果有过期的数据或者删除已存在的Key,创建RemovalNotification并丢入removalNotificationQueue队列,回调监听函数;
                 * get操作时,如果有过期的数据,创建RemovalNotification并丢入removalNotificationQueue队列,回调监听函数;
                 */
                .removalListener((notification)->  System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause()))
                //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(
                        /**
                         * 缓存不存在时通过CacheLoader的实现自动加载缓存
                         * 机制:当从缓存获取数据时,如果不存在,通过自己设置的加载策略进行加载数据new CacheLoader
                         */
                        new CacheLoader<Integer, Student>() {
                            @Override
                            public Student load(Integer key) throws Exception {
                                System.out.println("load student " + key);
                                Student student = new Student();
                                student.setAge((20+key));
                                student.setName("lori");
                                student.setClassName("P10");
                                return student;
                            }
                        });
    }
}
