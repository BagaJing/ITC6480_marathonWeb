package com.jing.blogs.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class asyncTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Async("taskAsyncPool")
    public void doTask(int i) throws InterruptedException{
        logger.info("Task "+i+" Started");
    }
}
