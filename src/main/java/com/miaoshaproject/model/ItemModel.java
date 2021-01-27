package com.miaoshaproject.model;

import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemModel {
    private Integer id;
    @NotBlank(message = "商品名称不为空")
    private String title;
    @NotNull(message = "商品价格不为空")
    @Min(value = 0,message = "商品价格必须大于0")
    /*
    BigDecimal，用来对超过16位有效位的数进行精确的运算。
    双精度浮点型变量double可以处理16位有效数，
    但在实际应用中，可能需要对更大或者更小的数进行运算和处理
     */
    private BigDecimal price;
    @NotNull(message = "商品库存不为空")
    private Integer stock;
    @NotBlank(message = "商品描述不为空")
    private String description;
    private Integer sales;
    @NotBlank(message = "商品图片不为空")
    private String imgUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
