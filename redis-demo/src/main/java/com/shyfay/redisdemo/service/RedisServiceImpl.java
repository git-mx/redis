package com.shyfay.redisdemo.service;

import com.shyfay.redisdemo.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author mx
 * @since 2019/9/11
 */
@Service
public class RedisServiceImpl implements RedisService {

    //使用StringRedisTemplate而不要使用RedisTemplate
    //因为使用RedisTemplate会导致.opsForHash().put或者putAll的哈希键带有""
    //比如哈希键本来应该是scores但是通过redisTemplate.opsForHash().put或者putAll之后，在Redis数据库里面存的哈希键就是"scores"
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<String> stringTest() {
        Map<String, String> keyValue = new HashMap<>();
        keyValue.put("AAA", "111");
        keyValue.put("BBB", "222");
        keyValue.put("CCC", "333");
        keyValue.put("DDD", "444");
        Iterator<String> iterator = keyValue.keySet().iterator();
        List<String> keyList = new ArrayList<>();
        while(iterator.hasNext()){
            keyList.add(iterator.next());
        }
        redisTemplate.opsForValue().multiSet(keyValue);
        return (List<String>)(List)redisTemplate.opsForValue().multiGet(keyList);
    }

    @Override
    public Map<String, Integer> hashTest() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("AAA:1", "1");
        hashMap.put("AAA:2", "2");
        hashMap.put("AAA:3", "3");
        hashMap.put("BBB:4", "4");
        hashMap.put("BBB:5", "5");
        redisTemplate.opsForHash().putAll("scores1", hashMap);
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan("scores1", ScanOptions.scanOptions().match("AAA:*").build());
        Map<String, Integer> resultMap = new HashMap<>();
        while(cursor.hasNext()){
            Map.Entry<Object, Object> entry = cursor.next();
            resultMap.put(entry.getKey().toString(), Integer.valueOf(entry.getValue().toString()));
        }
        return resultMap;
    }


    @Override
    public User objectTest() {
        User user = new User();
        user.setUserId(1L);
        user.setUserName("逍遥子");
        user.setLoginName("xiaoyaozi");
        user.setLoginPassword("123456");
        //能直接这样转换的条件是，redisTemplate的ValueSerializer是JdkSerializationRedisSerializer
        redisTemplate.opsForValue().set("user", user);
        return (User)redisTemplate.opsForValue().get("user");
    }

    @Override
    public Set<User> setTest() {
        List<User> userList = new ArrayList<>();
        User user;
        for(int i=0; i<5; i++){
            user = new User();
            user.setUserId(Long.valueOf(i));
            user.setUserName("user" + i);
            user.setLoginName("loginName" + i);
            user.setLoginPassword("loginPassword" + i);
            userList.add(user);
        }
        redisTemplate.opsForSet().add("userSet", userList);
        //Set<User> resultSet = redisTemplate.opsForSet().members("userSet");
        //一般针对哈希键、集合键和有序集合键都要直接使用HGETALL，SMEMBERS等命令，最好使用HSCAN、SSCAN和ZSCAN来渐进式操作
        Set<User> resultSet = new HashSet<User>();
        Cursor<Set<Object>> cursor = redisTemplate.opsForSet().scan("userSet", ScanOptions.NONE);
        while(cursor.hasNext()){
            System.out.println(cursor.next());
//            resultSet.add((User)cursor.next());
        }
        return resultSet;
    }

    @Override
    public List<User> listTest() {
        List<User> userList = new ArrayList<>();
        User user;
        for(int i=0; i<5; i++){
            user = new User();
            user.setUserId(Long.valueOf(i));
            user.setUserName("user" + i);
            user.setLoginName("loginName" + i);
            user.setLoginPassword("loginPassword" + i);
            userList.add(user);
        }
        redisTemplate.opsForList().leftPushAll("userList", userList);
        List<User> resultList = redisTemplate.opsForList().range("userList",0, userList.size() -1);
        return resultList;
    }
}
