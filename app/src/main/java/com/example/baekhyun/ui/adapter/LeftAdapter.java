package com.example.baekhyun.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baekhyun.R;
import com.example.baekhyun.model.domain.Selectid;

import java.util.ArrayList;
import java.util.List;

public class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.hodler> {
    private List<Selectid.DataBean> mDataBeans = new ArrayList<>();
    private int mcurrent = 0;
    private onLeftListener itemLeftClik = null;

    @NonNull
    @Override
    public hodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_item, parent, false);
        return new hodler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull hodler holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.left_category_tv);
        if (mcurrent == position) {
            textView.setBackgroundColor(Color.parseColor("#EFEEEE"));
        } else {
            textView.setBackgroundColor(Color.WHITE);
        }
        Selectid.DataBean dataBean = mDataBeans.get(position);
        textView.setText(dataBean.getFavorites_title());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemLeftClik != null && mcurrent != position) {
                    mcurrent = position;
                    itemLeftClik.onleftClick(dataBean);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataBeans.size();
    }

    public void getDate(Selectid selectid) {
        List<Selectid.DataBean> data = selectid.getData().subList(1, selectid.getData().size() - 2);
        data.get(4).setFavorites_title("秋冬必备");
        if (data != null) {
            this.mDataBeans.clear();
            mDataBeans.addAll(data);
            notifyDataSetChanged();
        }
        if (mDataBeans != null) {
            itemLeftClik.onleftClick(mDataBeans.get(mcurrent));
        }
    }

    public class hodler extends RecyclerView.ViewHolder {
        public hodler(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void onLeftClick(onLeftListener left) {
        this.itemLeftClik = left;
    }

    public interface onLeftListener {
        void onleftClick(Selectid.DataBean item);
    }
}
