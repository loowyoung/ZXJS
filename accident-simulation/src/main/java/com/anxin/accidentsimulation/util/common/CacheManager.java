package com.anxin.accidentsimulation.util.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * com.anxin.dataFileTransfer.Cache
 *
 * @author Lyu
 * @date 2018/7/10 14:21
 */
public class CacheManager {

    private ConcurrentHashMap<String,Object> cache = null;
    
    private static CacheManager instance =null;
    
    public static synchronized CacheManager getInstance(){
        if(instance==null){
            instance = new CacheManager();
        }
        return instance;
    }

    public CacheManager(){
        cache = new ConcurrentHashMap<String,Object>();
    }

    public static Object get(String key){
        return getInstance().getCache().get(key);
    }

    public boolean put(String key, Object obj){
        if (key==null ||"".equals(key)){
            return false;
        }
        cache.put(key,obj);
        return true;
    }

    public boolean isExitObject(Object obj){
        return cache.containsValue(obj);
    }

    public boolean isExitKey(String key){
        return cache.containsKey(key);
    }

    //获取整个缓存，不建议直接操作
    public Map getCache(){
        return this.cache;
    }
    
    public void clear() {
    	this.cache.clear();
    }
}
