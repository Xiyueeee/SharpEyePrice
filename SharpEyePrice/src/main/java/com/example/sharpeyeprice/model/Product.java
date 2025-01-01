package com.example.sharpeyeprice.model;

import javax.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private String img_guid;
    private String href;
    private String plat;  //平台
    private String category;   //品类

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", img_guid='" + img_guid + '\'' +
                ", href='" + href + '\'' +
                ", plat='" + plat + '\'' +  // 新增字段输出
                ", category='" + category + '\'' +  // 新增字段输出
                '}';
    }
    // Getter and Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getImg_guid() { return img_guid; }
    public void setImg_guid(String img_guid) { this.img_guid = img_guid; }
    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }
    public String getPlat() { return plat; }
    public void setPlat(String plat) { this.plat = plat; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

}