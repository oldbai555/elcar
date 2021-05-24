package com.atqidi.elcar;

import com.atqidi.elcar.utils.SnowflakeIdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 启动类
 *
 * @author 老白
 */
@SpringBootApplication
public class ElcarApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElcarApplication.class, args);
    }



    @Bean
    public SnowflakeIdWorker createSnowflakeIdWorker(){
        return new SnowflakeIdWorker(13,14);
    }
}
