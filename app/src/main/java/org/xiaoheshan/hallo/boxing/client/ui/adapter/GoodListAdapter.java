package org.xiaoheshan.hallo.boxing.client.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.xiaoheshan.hallo.boxing.client.R;
import org.xiaoheshan.hallo.boxing.client.bean.GoodDO;
import org.xiaoheshan.hallo.boxing.client.state.SystemConstant;
import org.xiaoheshan.hallo.boxing.client.ui.activity.GoodActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 30-04-2018
 */
public class GoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ReactiveAdapter<List<GoodDO>>{

    private Context context;
    private List<GoodDO> goodDOS;

    private static final int ITEM_TYPE_NORMAL = 0x464;
    private static final int ITEM_TYPE_END = 0x915;

    private int callType;

    private GoodListAdapter() {
    }

    public static GoodListAdapter newInstance(Context context, List<GoodDO> goodDOS, int callType) {
        GoodListAdapter adapter = new GoodListAdapter();
        adapter.goodDOS = goodDOS;
        adapter.context = context;
        adapter.callType = callType;
        return adapter;
    }

    @Override
    public int getItemViewType(int position) {
        if (goodDOS == null || position >= goodDOS.size()) {
            return ITEM_TYPE_END;
        }
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_END) {
            return new SimpleViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_end, parent, false));
        }
        return new GoodItemViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_good, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_NORMAL) {
            GoodDO goodDO = goodDOS.get(position);
            ((GoodItemViewHolder) holder).bindData(goodDO);
        }
    }

    @Override
    public int getItemCount() {
        return goodDOS == null ? 1 : goodDOS.size() + 1;
    }

    @Override
    public void refreshData(List<GoodDO> data) {
        this.goodDOS.clear();
        this.goodDOS.addAll(data);
        notifyDataSetChanged();
    }

    class GoodItemViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        @BindView(R.id.good_image_view)
        ImageView goodImageView;
        @BindView(R.id.name_text_view)
        TextView nameTextView;
        @BindView(R.id.price_text_view)
        TextView priceTextView;
        @BindView(R.id.rent_info_text_view)
        TextView rentInfoTextView;
        @BindView(R.id.desc_text_view)
        TextView descTextView;

        GoodItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        void bindData(final GoodDO goodDO) {
            nameTextView.setText(goodDO.getName());
            priceTextView.setText("出租 ￥" + goodDO.getRentPrice() + "  |  " + "市场 ￥" + goodDO.getMarketPrice());
            rentInfoTextView.setText("已出租 " +goodDO.getRentNum() + " 次");
            descTextView.setText(goodDO.getDesc());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GoodListAdapter.this.context, GoodActivity.class);
                    intent.putExtra(GoodActivity.MODE_NAME, callType);
                    intent.putExtra(GoodActivity.GOOD_ID_NAME, goodDO.getId());
                    GoodListAdapter.this.context.startActivity(intent);
                }
            });
            if (goodDO.getImg() != null) {
                Picasso.get().load(SystemConstant.IMAGE_URL_PREFIX + goodDO.getImg()).into(goodImageView);
            }
        }
    }
}
