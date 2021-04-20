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
import com.example.baekhyun.model.domain.SelectCategory;
import com.example.baekhyun.utils.Constant;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RightAdapter extends RecyclerView.Adapter<RightAdapter.hodler> {

    private List<SelectCategory.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mlist=new ArrayList<>();
    private onItemClick mListener=null;


    @NonNull
    @Override
    public hodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_item, parent, false);
        return new hodler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull hodler holder, int position) {
        SelectCategory.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean contntDate = mlist.get(position);
        holder.setDate(contntDate);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null&&contntDate.getCoupon_click_url()!=null) {
                    mListener.onTicketClick(contntDate);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public void setDate(SelectCategory content) {
        if(content.getCode()== Constant.SUCCESS_CODE){
            //content.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item();
            List<SelectCategory.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> map_data = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
            this.mlist.clear();
            this.mlist.addAll(map_data);
            notifyDataSetChanged();
        }
    }

    public class hodler extends RecyclerView.ViewHolder {
        @BindView(R.id.selected_cover)
        public ImageView mcover;

        @BindView(R.id.selected_title)
        public TextView mtitle;

        @BindView(R.id.selected_buy_btn)
        public TextView mbutton;

        @BindView(R.id.selected_original_prise)
        public TextView mprice;

        @BindView(R.id.selected_off_prise)
        public TextView moffprice;

        public hodler(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setDate(SelectCategory.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean contntDate) {
            mtitle.setText(contntDate.getTitle());
            //LogUtils.d(RightAdapter.this,contntDate.getPict_url());
            Glide.with(itemView.getContext()).load("https:" + contntDate.getPict_url()).into(mcover);
            if (contntDate.getCoupon_amount() == 0) {
                moffprice.setText("啊喔 你来晚啦~");
                mbutton.setVisibility(View.GONE);
                mprice.setVisibility(View.GONE);
            } else {
                moffprice.setText(contntDate.getCoupon_info());
                mbutton.setVisibility(View.VISIBLE);
                mprice.setText("原价 " + contntDate.getZk_final_price() + "元");
                mprice.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setListener(onItemClick click){
        this.mListener=click;
    }
    public interface onItemClick{
        void onTicketClick(SelectCategory.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean);
    }
}
