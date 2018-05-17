package org.xiaoheshan.hallo.boxing.client.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xiaoheshan.hallo.boxing.client.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 07-05-2018
 */
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ReactiveAdapter<Uri> {

    private Context context;

    private List<Uri> data;

    private static final int ITEM_TYPE_NORMAL = 0x464;
    private static final int ITEM_TYPE_END = 0x915;

    private ImageAdapter() {
    }

    public static ImageAdapter newInstance(Context context, List<Uri> data) {
        ImageAdapter adapter = new ImageAdapter();
        adapter.data = data;
        adapter.context = context;
        return adapter;
    }

    @Override
    public int getItemViewType(int position) {
        if (data == null || position >= data.size()) {
            return ITEM_TYPE_END;
        }
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_TYPE_NORMAL:
                ((ImageViewHolder)holder).bindData(data.get(position));
                break;
            case ITEM_TYPE_END:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 1 : data.size() + 1;
    }

    @Override
    public void refreshData(Uri data) {
        this.data.add(data);
        notifyDataSetChanged();
    }

    public List<Uri> getData() {
        return data;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.good_image_view)
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(Uri uri) {
            imageView.setImageURI(uri);
        }
    }
}
