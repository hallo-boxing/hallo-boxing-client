package org.xiaoheshan.hallo.boxing.client.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 01-05-2018
 */
@NoArgsConstructor
@Data
public class OrderVO {

    private int catId;
    private int catId1;
    private int catId2;
    private String catIdPath;
    private String createTime;
    private int dataFlag;
    private int deliveryCabinetId;
    private String deliveryTime;
    private String desc;
    private int fromUserId;
    private String gallery;
    private int goodId;
    private int id;
    private String img;
    private int isClosed;
    private int isPay;
    private int marketPrice;
    private String name;
    private int receiveCabinetId;
    private String receiveTime;
    private String remarks;
    private int rentNum;
    private int rentPrice;
    private int rentTime;
    private int status;
    private int toUserId;
    private String toUsername;
}
