package com.example.sharpeyeprice.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import javax.persistence.*;

@Entity    //标记User类是一个JPA实体类，意味着该类将映射到数据库中的一张表，如果表还不存在就自动生成一张表
@Data     //Lombok提供的注解，自动为User类生成常见的Java方法
@Builder    //生成一个建造者模式，使构造函数更易读
//@AllArgsConstructor     //为User类自动创建一个构造函数，用来初始化所有的字段
//@NoArgsConstructor      //为类自动生成一个无参数的构造函数，以便在进行数据加载时能够实例化该类


public class User {
    private Long id;
    private String account;
    private String password;
    private String email;

    // 默认构造函数和带参构造函数会自动生成
}
