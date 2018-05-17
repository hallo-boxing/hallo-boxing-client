package org.xiaoheshan.hallo.boxing.client.support.http;

import org.xiaoheshan.hallo.boxing.client.dal.CabinetDAO;
import org.xiaoheshan.hallo.boxing.client.dal.GoodDAO;
import org.xiaoheshan.hallo.boxing.client.dal.ImageDAO;
import org.xiaoheshan.hallo.boxing.client.dal.OrderDAO;
import org.xiaoheshan.hallo.boxing.client.dal.UserDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 14-04-2018
 */
public class HttpClient {

    private static final HttpClient INSTANCE;
    private Map<Class<?>, Object> daos = new HashMap<>();

    static {
        INSTANCE = new HttpClient();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://192.168.253.1:8080/hallo-boxing/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        INSTANCE.daos.put(CabinetDAO.class, retrofit.create(CabinetDAO.class));
        INSTANCE.daos.put(GoodDAO.class, retrofit.create(GoodDAO.class));
        INSTANCE.daos.put(OrderDAO.class, retrofit.create(OrderDAO.class));
        INSTANCE.daos.put(UserDAO.class, retrofit.create(UserDAO.class));
        INSTANCE.daos.put(ImageDAO.class, retrofit.create(ImageDAO.class));
    }

    public static HttpClient get() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public <T> T getDAO(Class<T> clazz) {
        return (T) daos.get(clazz);
    }

}
