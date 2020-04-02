package com.gj.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyCommandRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
//        run("http://localhost:8080/index.html");
        log.info("测试");
    }
}
