package com.example.baekhyun.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.baekhyun.model.domain.CateGoryies;
import com.example.baekhyun.ui.fragment.HomePagerFragment;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private List<CateGoryies.DataBean> mList=new ArrayList<>();
    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        CateGoryies.DataBean dataBean = mList.get(position);
        HomePagerFragment homePagerFragment=HomePagerFragment.newInstance(dataBean);
        return homePagerFragment;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public void setCategories(CateGoryies cateGoryies) {
        mList.clear();
        List<CateGoryies.DataBean> data = cateGoryies.getData();
        mList.addAll(data);
        notifyDataSetChanged();
    }
}
