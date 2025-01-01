package com.example.sharpeyeprice.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  //自动读取这个类
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry register) {
        System.out.println("Setting up CORS configuration...");
        register.addMapping("/**")   //允许所有路径的跨域请求
                .allowedOrigins("*")   //vue3应用的地址
                .allowedMethods("GET","POST","PUT","DELETE")   //允许的方法
                .allowedHeaders("*")    //允许的请求头
                .allowCredentials(false);    //允许携带Cookie
    }
}

//package com.example.sharpeyeprice.utils;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        System.out.println("Setting up CORS configuration...");
//
//        // 获取当前请求的 Origin
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String origin = request.getHeader("Origin");
//
//        // 如果 origin 为空或不在允许的域名列表中，则设置默认的 CORS 配置
//        if (origin != null && origin.matches("http://(.*).example.com")) {
//            registry.addMapping("/**")
//                    .allowedOrigins(origin)
//                    .allowedMethods("GET", "POST", "PUT", "DELETE")
//                    .allowedHeaders("*")
//                    .allowCredentials(true);
//        } else {
//            registry.addMapping("/**")
//                    .allowedOrigins("http://localhost:8081") // 默认的允许地址
//                    .allowedMethods("GET", "POST", "PUT", "DELETE")
//                    .allowedHeaders("*")
//                    .allowCredentials(true);
//        }
//    }
//}
