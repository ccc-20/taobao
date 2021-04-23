package com.example.baekhyun.ui.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baekhyun.R;
import com.example.baekhyun.model.domain.HomePagerContent;
import com.example.baekhyun.utils.UrlUtiles;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.hodler> {
    List<HomePagerContent.DataBean> data = new ArrayList<>();
    private OnListclickListener mListener = null;

    @NonNull
    @Override
    public HomePagerContentAdapter.hodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_page_content, parent, false);
        return new hodler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePagerContentAdapter.hodler holder, int position) {
        HomePagerContent.DataBean dataBean = data.get(position);
        holder.setData(dataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    HomePagerContent.DataBean item = data.get(position);
                    mListener.OnitemClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    public void addDate(List<HomePagerContent.DataBean> contents) {
        int oldSize = data.size();
        data.addAll(contents);
        notifyItemRangeChanged(oldSize, contents.size());
    }

    public class hodler extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        public ImageView cover;
        @BindView(R.id.goods_title)
        public TextView title;
        @BindView(R.id.goods_off_prise)
        public TextView after;
        @BindView(R.id.goods_after_off_prise)
        public TextView finalPrice;
        @BindView(R.id.goods_original_prise)
        public TextView originalPrise;
        @BindView(R.id.goods_sell_count)
        public TextView sellcount;

        public hodler(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(HomePagerContent.DataBean dataBean) {
            title.setText(dataBean.getTitle());
            String price = dataBean.getZk_final_price();
            long couponAmount = dataBean.getCoupon_amount();
            float result = Float.parseFloat(price) - couponAmount;
            Glide.with(itemView.getContext()).load(UrlUtiles.getCoverPath(dataBean.getPict_url())).into(cover);
            after.setText(String.format(itemView.getContext().getString(R.string.goods_price_off), couponAmount));
            finalPrice.setText(String.format("%.2f", result));
            originalPrise.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPrise.setText("￥" + price);
            sellcount.setText(dataBean.getVolume() + "人已购买");
        }
    }

    public void setOnListclickListener(OnListclickListener listener) {
        this.mListener = listener;
    }

    public interface OnListclickListener {
        void OnitemClick(HomePagerContent.DataBean item);
    }
}
