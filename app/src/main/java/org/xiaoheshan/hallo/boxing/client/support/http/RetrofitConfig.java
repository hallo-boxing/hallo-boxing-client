package org.xiaoheshan.hallo.boxing.client.support.http;

import org.xiaoheshan.hallo.boxing.client.dal.CabinetDAO;
import org.xiaoheshan.hallo.boxing.client.dal.GoodDAO;
import org.xiaoheshan.hallo.boxing.client.dal.OrderDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit配置
 *
 * @author : _Chf
 * @since : 07-04-2018
 */
@Module
public class RetrofitConfig {

    @Provides
    @Singleton
    private Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://localhost:8080/hallo-boxing/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public CabinetDAO provideCabinetDAO() {
        return provideRetrofit().create(CabinetDAO.class);
    }

    @Provides
    @Singleton
    public GoodDAO provideGoodDAO() {
        return provideRetrofit().create(GoodDAO.class);
    }

    @Provides
    @Singleton
    public OrderDAO provideOrderDAO() {
        return provideRetrofit().create(OrderDAO.class);
    }

}
