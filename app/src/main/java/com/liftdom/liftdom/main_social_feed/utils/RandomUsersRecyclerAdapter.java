package com.liftdom.liftdom.main_social_feed.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brodin on 1/28/2018.
 */

public class RandomUsersRecyclerAdapter extends RecyclerView.Adapter<RandomUsersBannerViewHolder>{

    private HashMap<String, String> mUserMap;
    private HashMap<String, List<String>> mFormattedMap = new HashMap<>();
    private Context mContext;
    private String mUid;
    public FragmentActivity mActivity;


    public RandomUsersRecyclerAdapter(HashMap<String, String> userMap, Context context, String uid){
        mContext = context;
        mUserMap = userMap;
        mUid = uid;
    }

    public void formatMap(){
        int i = 0;
        for(Map.Entry<String, String> entry : mUserMap.entrySet()){
            List<String> newList = new ArrayList<>();
            newList.add(entry.getKey());
            newList.add(entry.getValue());
            mFormattedMap.put(i + "_key", newList);
            i++;
        }
    }

    @Override
    public RandomUsersBannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.random_users_item_view, parent, false);
        RandomUsersBannerViewHolder holder = new RandomUsersBannerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RandomUsersBannerViewHolder viewHolder, int position){
        viewHolder.setUid(mUid);
        viewHolder.setxUid(mFormattedMap.get(position + "_key").get(0));
        viewHolder.setxUserName(mFormattedMap.get(position + "_key").get(1));
        viewHolder.setContext(mContext);
        viewHolder.setFragmentActivity(mActivity);
    }

    @Override
    public int getItemCount(){
        return mUserMap.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
}
