package org.xiaoheshan.hallo.boxing.client.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.xiaoheshan.hallo.boxing.client.R;
import org.xiaoheshan.hallo.boxing.client.bean.GoodDO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestRetCodeEnum;
import org.xiaoheshan.hallo.boxing.client.dal.GoodDAO;
import org.xiaoheshan.hallo.boxing.client.state.StateHolder;
import org.xiaoheshan.hallo.boxing.client.support.http.HttpClient;
import org.xiaoheshan.hallo.boxing.client.ui.adapter.GoodListAdapter;
import org.xiaoheshan.hallo.boxing.client.ui.util.ViewInitUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private GoodListAdapter adapter;

    private GoodDAO goodDAO = HttpClient.get().getDAO(GoodDAO.class);

    @BindView(R.id.parent_layout)
    View parentLayout;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    private boolean isRefreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        ViewInitUtils.initSwipeRefreshLayout(refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        adapter = GoodListAdapter.newInstance(this, new ArrayList<GoodDO>(), GoodActivity.LEASE_MODE);
        ViewInitUtils.initRecyclerView(this, recyclerView, adapter);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        refreshLayout.setRefreshing(true);
        if (StateHolder.isIsLogin()) {
            int userId = StateHolder.getUserDO().getId();
            Call<RestResult<List<GoodDO>>> call = goodDAO.getByUser(userId);
            call.enqueue(new Callback<RestResult<List<GoodDO>>>() {
                @Override
                public void onResponse(Call<RestResult<List<GoodDO>>> call,
                                       Response<RestResult<List<GoodDO>>> response) {
                    RestResult<List<GoodDO>> result = response.body();
                    Log.e("test", "1");
                    if (RestRetCodeEnum.SUCCESS.is(result.getCode())) {
                        Log.e("test", result.getData().get(0).getName());
                        adapter.refreshData(result.getData());
                    } else {
                        Snackbar.make(parentLayout, result.getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                    refreshOver();
                }

                @Override
                public void onFailure(Call<RestResult<List<GoodDO>>> call, Throwable t) {
                    Snackbar.make(parentLayout, "获取租借列表失败", Snackbar.LENGTH_LONG).show();
                    refreshOver();
                }
            });
        }
    }

    private void refreshOver() {
        isRefreshing = false;
        refreshLayout.setRefreshing(false);
    }
}
