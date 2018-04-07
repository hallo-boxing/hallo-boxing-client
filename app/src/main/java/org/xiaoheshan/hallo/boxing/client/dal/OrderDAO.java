package org.xiaoheshan.hallo.boxing.client.dal;

import org.xiaoheshan.hallo.boxing.client.bean.GoodDO;
import org.xiaoheshan.hallo.boxing.client.bean.OrderDO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestPageResult;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 订单DAO
 *
 * @author : _Chf
 * @since : 07-04-2018
 */
public interface OrderDAO {

    String URL_PREFIX = "order";

    @POST( URL_PREFIX + "/create")
    Call<RestResult<OrderDO>> create(@Body OrderDO orderDO);

    @POST( URL_PREFIX + "/{order_id}/cancel")
    Call<RestResult<Void>> cancel(@Path("order_id") Integer orderId);

    @POST( URL_PREFIX + "/{order_id}/pay")
    Call<RestResult<Void>> pay(@Path("order_id") Integer orderId);

    @POST( URL_PREFIX + "/list/{user_id}")
    Call<RestPageResult<OrderDO>> list(@Path("user_id") Integer userId);

    @POST( URL_PREFIX + "/return/{user_id}/{good_id}")
    Call<RestPageResult<OrderDO>> retur(@Path("user_id") Integer userId,
                                        @Path("good_id") Integer goodId);

}
