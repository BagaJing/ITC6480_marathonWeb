package com.jing.blogs.clientQueue;

import com.jing.blogs.orderQueue.resultMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class clientListener implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private clientAction queue;
    @Autowired
    private clientResultHolder resultHolder;
    private resultMap resultsMap = new resultMap();
    @Override
    @Async("taskAsyncPool")
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        while (true){
            if(!queue.getQueue().isEmpty()){
                String orderNum = queue.getQueue().poll();
                String prefix = orderNum.substring(0,orderNum.indexOf("-"));
                String asyncValue = resultsMap.getMap().getOrDefault(prefix,"client/index");
                resultHolder.getClientMap().get(orderNum).setResult(asyncValue);
                logger.info("Order "+orderNum+" Placed");
                //do something
            }else{
                try {
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
