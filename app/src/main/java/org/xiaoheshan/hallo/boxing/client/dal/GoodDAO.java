package org.xiaoheshan.hallo.boxing.client.dal;

import org.xiaoheshan.hallo.boxing.client.bean.GoodDO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 07-04-2018
 */
public interface GoodDAO {

    String URL_PREFIX = "good";

    @POST( URL_PREFIX + "/get-by-cabinet/{cabinet_id}")
    Call<RestResult<GoodDO>> getByCabinet(@Path("cabinet_id") Integer cabinetId);

    @POST( URL_PREFIX + "/get-by-user/{user_id}")
    Call<RestResult<List<GoodDO>>> getByUser(@Path("user_id") Integer userId);

    @POST( URL_PREFIX + "/get-by-nfc/{nfc_code}")
    Call<RestResult<GoodDO>> getByNfc(@Path("nfc_code") Integer nfcCode);

    @POST( URL_PREFIX + "/{id}")
    Call<RestResult<GoodDO>> getById(@Path("id") Integer id);
}
