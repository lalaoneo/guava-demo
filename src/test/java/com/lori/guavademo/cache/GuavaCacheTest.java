package com.lori.guavademo.cache;

import com.google.common.cache.CacheStats;
import com.lori.guavademo.cache.common.Student;
import com.lori.guavademo.cache.utils.GuavaCache;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GuavaCacheTest {

    @Test
    public void testGuavaCacheForExpire() throws ExecutionException, InterruptedException {

        /**
         * expireAfterWrite设置写缓存后过期时间
         * 此机制是一个延迟过期机制:
         * put操作时,会根据此次的操作时间去判断缓存数据那些已经过期,并进行移除
         * get操作时,会根据此次的操作时间去判断缓存数据那些已经过期,并进行移除
         */

        for (int i=0;i<20;i++){
            Student student = GuavaCache.cache.get(1);
            System.out.println(student);

            TimeUnit.SECONDS.sleep(1);
        }
        GuavaCache.cache.get(1);

        GuavaCache.cache.get(1);

        //最后打印缓存的命中率等 情况
        CacheStats stats = GuavaCache.cache.stats();
        System.out.println(stats.toString());
        // 命中率
        Assert.assertEquals(19,stats.hitCount());
        // miss率
        Assert.assertEquals(3,stats.missCount());
    }

    @Test
    public void testGuavaCacheForMaximumSize() throws ExecutionException {

        /**
         * maximumSize缓存最大容量,超过100之后就会按照LRU最近虽少使用算法来移除缓存项
         * 下面的例子:key为1的使用最频繁,其他未使用;插入key为6的数据时,缓存满了,会根据recencyQueue引用队列排序,
         * 移除时间最早并且引用最少的数据,此案例会移除2
         */

        Student case1 = new Student();
        case1.setClassName("P10");
        case1.setName("lori");
        case1.setAge(30);
        GuavaCache.cache.put(1,case1);
        GuavaCache.cache.put(2,case1);
        GuavaCache.cache.put(3,case1);
        GuavaCache.cache.put(4,case1);
        GuavaCache.cache.put(5,case1);
        GuavaCache.cache.get(1);

        GuavaCache.cache.put(6,case1);

        // 2已经被移除，再获取活从新load进来,从而移除3,这个操作是为了统计缓存命中率
        GuavaCache.cache.get(2);

        //最后打印缓存的命中率等 情况
        CacheStats stats = GuavaCache.cache.stats();
        System.out.println(stats.toString());
        // 命中率
        Assert.assertEquals(1,stats.hitCount());
        // miss率
        Assert.assertEquals(1,stats.missCount());
    }
}
