package com.cctv.common;

/**
 * ThreadLocal工具类
 */
public class BaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置id值
     * @param id
     */
    public static void setCurrentId(Long id){
         threadLocal.set(id);
    }

    /**
     * 获取id
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
