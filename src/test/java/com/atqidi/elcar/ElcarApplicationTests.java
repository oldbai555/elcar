package com.atqidi.elcar;

import com.atqidi.elcar.utils.SnowflakeIdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class ElcarApplicationTests {
    @Autowired
    SnowflakeIdWorker snowflakeIdWorker;

    @Test
    void contextLoads() {

         System.out.println(snowflakeIdWorker.nextId());
    }

}
