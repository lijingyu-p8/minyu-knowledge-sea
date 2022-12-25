package com.ruoyi.common.core.cache.local;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ruoyi.common.core.cache.MinYuCache;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 本地缓存
 * @Author: lijingyu
 * @CreateTime: 2022-12-25  21:20
 */
@Component
@ConditionalOnProperty(prefix = "minyu.redis", name = "enabled", havingValue = "false")
public class LocalCache implements MinYuCache {
    private final Cache<String, Object> localcCache;

    public LocalCache() {
        this.localcCache = initLocalCache();
    }

    private Cache<String, Object> initLocalCache() {
        Cache<String, Object> cache = Caffeine.newBuilder()
                .softValues()
                .maximumSize(50000)
                .expireAfterAccess(Duration.ofMinutes(10))
                .build();
        return cache;
    }


    @Override
    public <T> void setCacheObject(String key, T value) {
        localcCache.put(key, value);
    }

    @Override
    public <T> void setCacheObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
        localcCache.put(key, value);
    }

    @Override
    public boolean expire(String key, long timeout) {
        return false;
    }

    @Override
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public long getExpire(String key) {
        return 0;
    }

    @Override
    public Boolean hasKey(String key) {
        return localcCache.getIfPresent(key) == null;
    }

    @Override
    public <T> T getCacheObject(String key) {
        Object data = localcCache.getIfPresent(key);
        if (data == null) {
            return null;
        }
        return (T) data;
    }

    @Override
    public boolean deleteObject(String key) {
        localcCache.invalidate(key);
        return true;
    }

    @Override
    public boolean deleteObject(Collection collection) {
        localcCache.invalidateAll(collection);
        return true;
    }

    @Override
    public <T> long setCacheList(String key, List<T> dataList) {
        return 0;
    }

    @Override
    public <T> List<T> getCacheList(String key) {
        return null;
    }

    @Override
    public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
        return null;
    }

    @Override
    public <T> Set<T> getCacheSet(String key) {
        return null;
    }

    @Override
    public <T> void setCacheMap(String key, Map<String, T> dataMap) {

    }

    @Override
    public <T> Map<String, T> getCacheMap(String key) {
        return null;
    }

    @Override
    public <T> void setCacheMapValue(String key, String hKey, T value) {

    }

    @Override
    public <T> T getCacheMapValue(String key, String hKey) {
        return null;
    }

    @Override
    public <T> List<T> getMultiCacheMapValue(String key, Collection<Object> hKeys) {
        return null;
    }

    @Override
    public boolean deleteCacheMapValue(String key, String hKey) {
        return false;
    }

    @Override
    public Collection<String> keys(String pattern) {
        Set<@NonNull String> keys = localcCache.asMap().keySet();
        List<String> results = new ArrayList<>();
        keys.stream().filter(str -> str.startsWith(pattern)).forEach(key -> results.add(key));
        return results;
    }
}