package org.xiaoheshan.hallo.boxing.client.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品
 *
 * @author : _Chf
 * @since : 07-04-2018
 */
@Data
public class GoodDO {

    private int userId;
    private int catId;
    private int catId1;
    private int catId2;
    private String catIdPath;
    private String createTime;
    private int dataFlag;
    private String desc;
    private String gallery;
    private int id;
    private String img;
    private int marketPrice;
    private String name;
    private int rentNum;
    private int rentPrice;
    private String rentTime;
    private int status;

}
