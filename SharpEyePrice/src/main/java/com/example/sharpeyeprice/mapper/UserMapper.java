package com.example.sharpeyeprice.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Options;
import com.example.sharpeyeprice.model.User;

@Mapper   //mapper组件

public interface UserMapper {
    //登录接口
    @Select("select password from User where account = #{account}")   //根据用户的账号查找密码
    String findPasswordByAccount(@Param("account") String account);
    //@Param注解：用于指定方法参数的名称，确保MyBatis能正确映射参数，返回值是string,就是密码

    //注册接口
    @Select("select account from User where account = #{account}")    //根据账号查询是否已经存在此账号
    String findAccountByAccount(@Param("account") String account);

    @Select("select * from User where account = #{account}")          //根据账号查找并返回对应的用户信息，返回一个User对象
    User findUserByAccount(@Param("account") String account);

    @Select("select email from User where email = #{email}")          //根据邮箱查询邮箱是否存在
    String findEmailByEmail(@Param("email") String email);

    //插入新用户
    @Insert("insert into User(account, password, email) values(#{account}, #{password}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertNewUser(User user);

    //重置密码
    @Select("select * from User where email = #{email}")                //根据email查询用户信息
    User findUserByEmail(@Param("email") String email);

    @Update("update User set password = #{password} where account = #{account}")     //将方法参数传入SQL语句中
    void UpdatePasswordByAccount(@Param("account") String account, @Param("password") String password);
}
