package org.xiaoheshan.hallo.boxing.client.dal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xiaoheshan.hallo.boxing.client.bean.UserDO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;
import org.xiaoheshan.hallo.boxing.client.support.http.HttpClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 14-04-2018
 */
public class UserDAOTest {

    UserDAO userDAO;

    @Before
    public void setUp() throws Exception {
        userDAO = HttpClient.get().getDAO(UserDAO.class);
    }

    @Test
    public void login() throws IOException {
        Call<RestResult<UserDO>> call = userDAO.login("13800138000", "123456");
        Response<RestResult<UserDO>> response = call.execute();
        Assert.assertTrue(response.message(), response.isSuccessful());
        if (response.isSuccessful() && response.body() != null) {
            UserDO userDO = response.body().getData();
            System.out.println(userDO);
        }
    }
}