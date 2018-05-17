package org.xiaoheshan.hallo.boxing.client.state;

import org.xiaoheshan.hallo.boxing.client.bean.UserDO;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 01-05-2018
 */
public class StateHolder {

    private static boolean isLogin = false;

    private static UserDO userDO;

    public static boolean isIsLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        StateHolder.isLogin = isLogin;
    }

    public static UserDO getUserDO() {
        return userDO;
    }

    public static void setUserDO(UserDO userDO) {
        StateHolder.userDO = userDO;
    }
}
