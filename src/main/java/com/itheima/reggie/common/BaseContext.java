package com.itheima.reggie.common;

import org.springframework.stereotype.Component;

@Component
public class BaseContext extends ThreadLocal{
    private static ThreadLocal threadLocal = new ThreadLocal();

    public static void setId(Long id) {
        threadLocal.set(id);
    }

    public static Long getId() {
        return (Long) threadLocal.get();
    }
}
