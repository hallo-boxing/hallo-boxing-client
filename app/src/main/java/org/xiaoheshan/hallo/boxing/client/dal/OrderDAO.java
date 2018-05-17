package org.xiaoheshan.hallo.boxing.client.dal;

import org.xiaoheshan.hallo.boxing.client.bean.OrderDO;
import org.xiaoheshan.hallo.boxing.client.bean.OrderVO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestPageResult;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;

import java.util.List;

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

    @POST( URL_PREFIX + "/list-from/{user_id}")
    Call<RestResult<List<OrderVO>>> listByFromUserId(@Path("user_id") Integer userId);

    @POST( URL_PREFIX + "/list-to/{user_id}")
    Call<RestResult<List<OrderVO>>> listByToUserId(@Path("user_id") Integer userId);

    @POST( URL_PREFIX + "/return/{user_id}/{order_id}/{cabinet_id}")
    Call<RestPageResult<OrderDO>> retur(@Path("user_id") Integer userId,
                                        @Path("order_id") Integer orderId,
                                        @Path("cabinet_id") Integer cabinetId);

}
