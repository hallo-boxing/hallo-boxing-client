package org.xiaoheshan.hallo.boxing.client.bean;

import lombok.Data;

/**
 * 订单
 *
 * @author : _Chf
 * @since : 07-04-2018
 */
@Data
public class OrderDO {

    private String createTime;
    private int dataFlag;
    private int deliveryCabinetId;
    private String deliveryTime;
    private int goodId;
    private int id;
    private int isClosed;
    private int isPay;
    private int receiveCabinetId;
    private String receiveTime;
    private String remarks;
    private int status;
    private int userId;

}
