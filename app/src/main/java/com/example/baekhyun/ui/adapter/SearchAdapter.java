package com.example.baekhyun.ui.adapter;

import android.graphics.Paint;
import android.graphics.Picture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baekhyun.R;
import com.example.baekhyun.model.domain.SearchResult;
import com.example.baekhyun.utils.LogUtils;
import com.example.baekhyun.utils.UrlUtiles;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.hodler> {

    List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> mDataBeans = new ArrayList<>();
    private OnListclickListener mListener = null;

    @NonNull
    @Override
    public hodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_page_content, parent, false);
        return new hodler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull hodler holder, int position) {
        SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean data = mDataBeans.get(position);
        holder.setData(data);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean item = mDataBeans.get(position);
                    mListener.OnitemClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataBeans.size();
    }

    public void setData(SearchResult result) {
        List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> map_data = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mDataBeans.clear();
        mDataBeans.addAll(map_data);
        notifyDataSetChanged();
    }

    public void addData(SearchResult result) {
        int oldSize = mDataBeans.size();
        mDataBeans.addAll(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
        notifyItemRangeChanged(oldSize, result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size());
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

        public void setData(SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean dataBean) {
            title.setText(dataBean.getTitle());
            String price = dataBean.getZk_final_price();
            String couponAmount = dataBean.getCoupon_amount();
            float pricee = Float.parseFloat(price);
            if (couponAmount != null) {
                float coupon = Float.parseFloat(couponAmount);
                float result = pricee - coupon;
                finalPrice.setText(String.format("%.2f", result));
                after.setText("省" + couponAmount + "元");
            } else {
                after.setText("啊哦 来晚了~");
                finalPrice.setText(price);
            }
            Glide.with(itemView.getContext()).load(dataBean.getPict_url()).into(cover);
            originalPrise.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPrise.setText("￥" + price);
            sellcount.setText(dataBean.getVolume() + "人已购买");
        }
    }

    public void setOnListclickListener(OnListclickListener listener) {
        this.mListener = listener;
    }

    public interface OnListclickListener {
        void OnitemClick(SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean item);
    }
}
