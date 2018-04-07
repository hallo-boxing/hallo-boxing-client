package org.xiaoheshan.hallo.boxing.client.dal;

import org.xiaoheshan.hallo.boxing.client.bean.CabinetDO;
import org.xiaoheshan.hallo.boxing.client.bean.GoodDO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 共享柜DAO
 *
 * @author : _Chf
 * @since : 07-04-2018
 */
public interface CabinetDAO {

    String URL_PREFIX = "cabinet";

    @POST( URL_PREFIX + "/{cabinet_id}")
    Call<RestResult<CabinetDO>> getCabinet(@Path("cabinet_id") Integer cabinetId);

    @POST( URL_PREFIX + "/{cabinet_id}/close-door")
    Call<RestResult<Void>> closeDoor(@Path("cabinet_id") Integer cabinetId);

    @POST( URL_PREFIX + "/{cabinet_id}/open-door")
    Call<RestResult<Void>> openDoor(@Path("cabinet_id") Integer cabinetId);

    @POST( URL_PREFIX + "/{cabinet_id}/entry/{nfc_code}")
    Call<RestResult<GoodDO>> entryGood(@Path("cabinet_id") Integer cabinetId,
                                       @Path("nfc_code") Integer nfcCode);

    @POST( URL_PREFIX + "/{cabinet_id}/get-nfc-code")
    Call<RestResult<Integer>> getNfcCode(@Path("get-nfc-code") Integer cabinetId);

    @POST( URL_PREFIX + "/{cabinet_id}/take-photo")
    Call<RestResult<Void>> takePhoto(@Path("cabinet_id") Integer cabinetId);

}
