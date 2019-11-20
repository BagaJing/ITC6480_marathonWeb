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

    public static String getHtmlContent(String customerName,
                                        String TrainName,
                                        long trainId,
                                        String orderId,
                                        String customerEmail){
        String res = "<html>\n" +
                "<body>\n" +
                "    <h3>hello Coach ! You got a new order!</h3>\n" +
                "   <table>\n"+
                "       <thead>\n"+
                "       <tr>\n"+
                "           <th>Customer</th>\n"+
                "           <th>Chosen Training</th>\n"+
                "           <th>Training ID</th>\n"+
                "           <th>Order ID</th>\n"+
                "           <th>Customer Email</th>\n"+
                "       </tr>\n"+
                "       </thead>\n"+
                "       <tbody>\n"+
                "       <tr>\n"+
                "           <td>"+customerName+"</th>\n"+
                "           <td>"+TrainName+"</th>\n"+
                "           <td>"+trainId+"</th>\n"+
                "           <td>"+orderId+"</th>\n"+
                "           <td>"+customerEmail+"</th>\n"+
                "       </tr>\n"+
                "       </tbody>\n"+
                "   </table>\n"+
                "</body>\n" +
                "</html>";
        return  res;
    }
}
