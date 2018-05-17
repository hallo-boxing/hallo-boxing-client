package org.xiaoheshan.hallo.boxing.client.ui.util;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.xiaoheshan.hallo.boxing.client.R;

import java.util.Objects;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 15-04-2018
 */
public abstract class ViewInitUtils {

    public static void initSwipeRefreshLayout(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);
    }

    public static void initRecyclerView(Context context, RecyclerView recyclerView,
                                        RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public static void initToolBar(final AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
