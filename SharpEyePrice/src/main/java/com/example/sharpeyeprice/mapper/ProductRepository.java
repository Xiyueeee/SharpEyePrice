package com.example.sharpeyeprice.mapper;

import com.example.sharpeyeprice.model.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductRepository {

    // 修改插入语句，添加平台和品类字段
    @Insert("INSERT INTO product(name, price, img_guid, href, plat, category) " +
            "VALUES(#{name}, #{price}, #{img_guid}, #{href}, #{plat}, #{category})")
    void addProduct(Product product);


    @Select("SELECT * FROM product WHERE id = #{id}")
    Product findById(Long id);

    @Select("SELECT * FROM product")
    List<Product> findAll();

    @Select("SELECT * FROM product WHERE category = #{category}")
    List<Product> findByCategory(String category);
}
