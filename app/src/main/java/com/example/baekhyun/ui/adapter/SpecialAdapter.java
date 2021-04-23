package com.example.baekhyun.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baekhyun.R;
import com.example.baekhyun.model.domain.SpecialContent;
import com.example.baekhyun.utils.LogUtils;
import com.example.baekhyun.utils.UrlUtiles;

import java.util.ArrayList;
import java.util.List;

public class SpecialAdapter extends RecyclerView.Adapter<SpecialAdapter.hodler> {
    private List<SpecialContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> data = new ArrayList<>();
    private onSpecialContent mListener = null;

    @NonNull
    @Override
    public hodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spacial, parent, false);
        return new hodler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull hodler holder, int position) {
        SpecialContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean = data.get(position);
        holder.setItemData(mapDataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.Specialclick(mapDataBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        //LogUtils.d(this,data.size()+"");
        return data.size();
    }

    public void setDate(SpecialContent content) {
        this.data.clear();
        this.data.addAll(content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        notifyDataSetChanged();
    }

    public void onMoreLoaded(SpecialContent content) {
        List<SpecialContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> map_data = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        int old = data.size();
        this.data.addAll(map_data);
        notifyItemRangeChanged(old, map_data.size());
    }

    public class hodler extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;
        private TextView mprice;

        public hodler(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.on_sell_cover);
            mTextView = itemView.findViewById(R.id.on_sell_content_title_tv);
            mprice = itemView.findViewById(R.id.on_sell_off_prise_tv);

        }

        public void setItemData(SpecialContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean) {
            mTextView.setText(mapDataBean.getTitle());
            //LogUtils.d(SpecialAdapter.this,mapDataBean.getPict_url());
            String coverPath = UrlUtiles.getCoverPath(mapDataBean.getPict_url());
            Glide.with(mImageView.getContext()).load(coverPath).into(mImageView);
            float v = Float.parseFloat(mapDataBean.getZk_final_price());
            float v1 = (float) v - mapDataBean.getCoupon_amount();
            mprice.setText("券后价：" + String.format("%.2f", v1));
        }
    }

    public interface onSpecialContent {
        void Specialclick(SpecialContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean);
    }

    public void setSpecialListener(onSpecialContent listener) {
        this.mListener = listener;
    }
}
