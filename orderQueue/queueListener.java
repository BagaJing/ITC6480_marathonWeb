package com.jing.blogs.orderQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class queueListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private actionQueue actionQueue;
    @Autowired
    private DeferredResultHolder resultHolder;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static String GALLERY_REDIRECT = "redirect:/admin/gallery";
    private static String GALLERY = "admin/gallery";
    private static String PODCAST = "admin/podcast-post";
    private static String PODCAST_LIST = "admin/podcasts";
    private static String PODCAST_REDIRECT = "redirect:/admin/podcasts";
    @Override
    @Async("taskAsyncPool")
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        while (true) {
            if (!actionQueue.getActionQueue().isEmpty()) {
                String orderNumber = actionQueue.getActionQueue().poll();
                String redirectValue = "";
                if (orderNumber.startsWith("GA")) redirectValue = GALLERY_REDIRECT;
                else if (orderNumber.startsWith("SGA")) redirectValue = GALLERY;
                else if (orderNumber.startsWith("PR")) redirectValue = PODCAST_REDIRECT;
                else if(orderNumber.startsWith("PL")) redirectValue = PODCAST_LIST;
                else if (orderNumber.startsWith("P")) redirectValue = PODCAST;
                logger.info("Retrun Order Status: " + orderNumber);
                resultHolder.getMap().get(orderNumber).setResult(redirectValue);
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}