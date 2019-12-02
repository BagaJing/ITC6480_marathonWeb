package com.jing.blogs.clientQueue;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

@Component
public class clientResultHolder {
    private Map<String, DeferredResult> clientMap = new HashMap<>();

    public Map<String, DeferredResult> getClientMap() {
        return clientMap;
    }

    public void setClientMap(Map<String, DeferredResult> clientMap) {
        this.clientMap = clientMap;
    }
}
