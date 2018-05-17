package org.xiaoheshan.hallo.boxing.client.dal;

import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 11-05-2018
 */
public interface ImageDAO {

    String URL_PREFIX = "image";

    @Multipart
    @POST(URL_PREFIX + "/upload")
    Call<RestResult<Integer>> upload(@Part List<MultipartBody.Part> partList);
}
