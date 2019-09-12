package com.shyfay.redisdemo;

import com.shyfay.redisdemo.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDemoApplicationTests {

    @Autowired
    RedisService redisService;

    @Test
    public void contextLoads() {
        System.out.println(redisService.setTest());
    }

}
