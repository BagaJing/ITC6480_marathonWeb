package com.jing.blogs.orderQueue;

import java.util.HashMap;
import java.util.Map;

public class resultMap {
    public Map<String,String> map;

    public resultMap() {
        this.map = new HashMap<>();
        this.map.put("INDEX","client/index");
        this.map.put("BLOGS","client/goodstuff");
        this.map.put("COACH","client/coach");
        this.map.put("TRAIN","client/training");
        this.map.put("CONTACT","client/contact");
        this.map.put("GALLERY","client/gallery");
        this.map.put("ARTICLE","client/blog");
    }

    public Map<String, String> getMap() {
        return map;
    }
}
