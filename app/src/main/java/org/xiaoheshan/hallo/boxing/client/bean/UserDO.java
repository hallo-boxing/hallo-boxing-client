package org.xiaoheshan.hallo.boxing.client.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 14-04-2018
 */
@NoArgsConstructor
@Data
@ToString
public class UserDO {

    private String birthday;
    private String createTime;
    private int dataFlag;
    private String email;
    private int id;
    private String lastIp;
    private String lastTime;
    private String loginName;
    private String loginPwd;
    private String name;
    private String payPwd;
    private String phone;
    private String photo;
    private String qq;
    private int sex;
    private String trueName;
}
