package com.shyfay.redisdemo.service;

import com.shyfay.redisdemo.bean.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mx
 * @since 2019/9/11
 */
public interface RedisService {
    List<String> stringTest();
    Map<String, Integer> hashTest();
    User objectTest();
    Set<User> setTest();
    List<User> listTest();
}
