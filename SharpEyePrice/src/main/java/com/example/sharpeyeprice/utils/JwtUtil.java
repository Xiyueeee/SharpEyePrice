package com.example.sharpeyeprice.utils;

import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import com.alibaba.fastjson.JSONObject;

import com.example.sharpeyeprice.SpringBootApp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.SpringApplication;

public class JwtUtil {
    //一些常量的定义
    static final String security = "sharpeyeprice";   //JWT签名的密钥
    static final long expire_time = 600000;   //Token过期时间（毫秒）

    public static Claims paraJWT(String jsonWebToken) {   //解析JWT并验证其签名
        return Jwts.parser().setSigningKey(Base64.getDecoder().decode(security))   //解析JWT字符串，指定密钥将密钥从Base65解码成字节数组
                .parseClaimsJws(jsonWebToken).getBody();   //解析JWT并验证签名，如果签名无效，抛出异常
        //成功解析后，通过getBody()方法获取JWT的载荷（Claims)，就是存储在JWT中的实际数据
    }
    //解析JWT
    public static String paraJWT2code(String jsonWebToken) {
        return JSONObject.parseObject(paraJWT(jsonWebToken).get("sub").toString()).get("code").toString();
    }

    public static String paraJWT2email(String jsonWebToken) {
        return JSONObject.parseObject(paraJWT(jsonWebToken).get("sub").toString()).get("email").toString();
    }

    public static String paraJWT2account(String jsonWebToken) {
        return JSONObject.parseObject(paraJWT(jsonWebToken).get("sub").toString()).get("account").toString();
    }

    public static String paraJWT2id(String jsonWebToken) {
        return JSONObject.parseObject(paraJWT(jsonWebToken).get("sub").toString()).get("id").toString();
    }

    public static String createJWT(String type, String args1, String args2) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥 就是一个base64加密后的字符串
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JSONObject jsonObject = new JSONObject();
        if (type.equals("login")) {
            jsonObject.put("account", args1);
            jsonObject.put("id", args2);
        } else if (type.equals("email")) {
            jsonObject.put("email", args1);
            jsonObject.put("code", args2);
        }

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ","JWT").setIssuedAt(now)  //创建时间
                .setSubject(jsonObject.toString())  //主题，个人信息
                .signWith(signatureAlgorithm, signingKey);        //第三段密钥
        //添加Token过期时间
        if (expire_time >= 0) {
            //过期时间
            long expMillis = nowMillis + expire_time;
            //现在的时间
            Date exp = new Date(expMillis);
            //系统时间之前的token都不能被承认
            builder.setExpiration(exp).setNotBefore(now);
        }
        //生成JWT
        return builder.compact();
    }
}
