package com.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName address_book
 */
@TableName(value ="address_book")
@Data
public class AddressBook implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 通讯人
     */
    private String contactPerson;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 
     */
    private String city;

    /**
     * 工作地点
     */
    private String workplace;

    /**
     * 备注
     */
    private String note;

    /**
     * 
     */
    private Integer userId;

    private String tag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}