package com.jing.blogs.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyBeanUtils {


    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
        List<String> nullPropertyNames = new ArrayList<>();
        for (PropertyDescriptor pd : pds){
            String propertyName = pd.getName();
            if (beanWrapper.getPropertyValue(propertyName) == null) {
                nullPropertyNames.add(propertyName);
            }
        }
        return nullPropertyNames.toArray(new String[nullPropertyNames.size()]);
    }

    public static List<Long> converttoList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids)&&ids!=null) {
            String[] idarray = ids.split(",");
            for (int i=0; i<idarray.length;i++) {
                list.add(Long.valueOf(idarray[i]));
            }
        }
        //System.out.println(list);
        return list;
    }
    public static String getRandomOrderNum(int num){
        String result = "";
        Random ran = new Random();
        for (int i = 0 ; i < num ; i++){
            result += String.valueOf(ran.nextInt(10));
        }
        return result;
    }
}
