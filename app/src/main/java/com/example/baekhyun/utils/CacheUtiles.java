package com.example.baekhyun.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.constraintlayout.solver.GoalRow;

import com.example.baekhyun.base.BaseApplication;
import com.example.baekhyun.model.domain.Cache;
import com.google.gson.Gson;

public class CacheUtiles {
    public static final String BAEKHYUN = "baekhyun";
    private final SharedPreferences mSharedPreferences;
    private final Gson mGson;

    private CacheUtiles() {
        mSharedPreferences = BaseApplication.getAppContext().getSharedPreferences(BAEKHYUN, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public void saveCache(String key, Object value) {
        this.saveCache(key, value, -1L);
    }

    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String valueStr = mGson.toJson(value);
        if (duration != -1L) {
            //当前的时间
            duration += System.currentTimeMillis();
        }
        //保存一个有数据有时间的内容
        Cache cacheWithDuration = new Cache(valueStr, duration);
        String cacheWithTime = mGson.toJson(cacheWithDuration);
        edit.putString(key, cacheWithTime);
        edit.apply();
    }


    public void delCache(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String valueWithDuration = mSharedPreferences.getString(key, null);
        if (valueWithDuration == null) {
            return null;
        }
        Cache cacheWithDuration = mGson.fromJson(valueWithDuration, Cache.class);
        //对时间进行判断
        long duration = cacheWithDuration.getDuration();
        if (duration != -1 && duration - System.currentTimeMillis() <= 0) {
            //判断是否过期了
            //过期了
            return null;
        } else {
            //没过期
            String cache = cacheWithDuration.getCache();
            LogUtils.d(this, "cache -- > " + cache);
            T result = mGson.fromJson(cache, clazz);
            return result;
        }
    }

    private static CacheUtiles sJsonCacheUtil = null;

    public static CacheUtiles getInstance() {
        if (sJsonCacheUtil == null) {
            sJsonCacheUtil = new CacheUtiles();
        }
        return sJsonCacheUtil;
    }

}