package org.xiaoheshan.hallo.boxing.client.dal;

import org.xiaoheshan.hallo.boxing.client.bean.UserDO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 14-04-2018
 */
public interface UserDAO {

    String URL_PREFIX = "user";

    @POST( URL_PREFIX + "/login")
    Call<RestResult<UserDO>> login(@Query("mobile") String mobile,
                                          @Query("password") String password);

    @POST( URL_PREFIX + "/register")
    Call<RestResult<Void>> register(@Body UserDO userDO);
}
