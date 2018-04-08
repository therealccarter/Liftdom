package com.liftdom.liftdom.main_social_feed.utils;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.workout_assistor.RepsWeightWAFrag;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.LocalDate;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class RandomUsersBannerFrag extends Fragment {


    public RandomUsersBannerFrag() {
        // Required empty public constructor
    }


    public interface removeFragCallback {
        void removeRandomUsersBanner();
    }

    private removeFragCallback removeFrag;

    public boolean isShowAllTheTime;
    private HashMap<String, String> mUserMap = new HashMap<>();

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.followFellowLiftersRecycler) RecyclerView fellowLiftersRecyclerView;
    @BindView(R.id.fellowLiftersCloseButton) ImageView closeButton;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.closeButtonLL) LinearLayout closeButtonLL;
    @BindView(R.id.viewMoreUsers) TextView viewMoreUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_random_users_banner, container, false);

        ButterKnife.bind(this, view);

        if(!isShowAllTheTime){
            removeFrag = (removeFragCallback) getParentFragment();
        }else{
            closeButtonLL.setVisibility(View.GONE);
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFrag.removeRandomUsersBanner();
            }
        });

        String day = LocalDate.now().toString("dd");
        double dayDouble = Double.parseDouble(day);

        if(!isShowAllTheTime){
            if(dayDouble % (double) 6 == 0.0){
                initializeRecyclerForRandom10();
            }else{
                initializeRecyclerForLast10();
            }
        }else{
            initializeRecyclerForLast30();
        }

        viewMoreUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragID", 3);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setUpRecyclerView(){
        loadingView.setVisibility(View.GONE);
        fellowLiftersRecyclerView.setVisibility(View.VISIBLE);
        fellowLiftersRecyclerView.setItemViewCacheSize(10);
        RandomUsersRecyclerAdapter adapter = new RandomUsersRecyclerAdapter(mUserMap, getContext(), uid);
        adapter.formatMap();
        adapter.mActivity = getActivity();
        fellowLiftersRecyclerView.setAdapter(adapter);
        fellowLiftersRecyclerView.setHasFixedSize(false);
        fellowLiftersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager
                .HORIZONTAL, false));
    }

    private void initializeRecyclerForLast10(){
        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("userList");
        userListRef.limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int inc = 0;
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    inc++;
                    mUserMap.put(dataSnapshot1.getKey(), dataSnapshot1.getValue(String.class));
                    if(inc == 10){
                        setUpRecyclerView();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initializeRecyclerForRandom10(){
        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("userList");
        userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int inc = 0;
                int inc2 = 0;
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    inc++;
                    if((double) inc > ((double) dataSnapshot.getChildrenCount() / (double) 2)){
                        if(inc2 < 10){
                            mUserMap.put(dataSnapshot1.getKey(), dataSnapshot1.getValue(String.class));
                        }else if(inc2 == 10){
                            setUpRecyclerView();
                        }
                        inc2++;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initializeRecyclerForLast30(){
        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("userList");
        userListRef.limitToLast(30).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    mUserMap.put(dataSnapshot1.getKey(), dataSnapshot1.getValue(String.class));
                    setUpRecyclerViewGrid();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpRecyclerViewGrid(){
        loadingView.setVisibility(View.GONE);
        fellowLiftersRecyclerView.setVisibility(View.VISIBLE);
        fellowLiftersRecyclerView.setItemViewCacheSize(10);
        RandomUsersRecyclerAdapter adapter = new RandomUsersRecyclerAdapter(mUserMap, getContext(), uid);
        adapter.formatMap();
        adapter.mActivity = getActivity();
        fellowLiftersRecyclerView.setAdapter(adapter);
        fellowLiftersRecyclerView.setHasFixedSize(false);
        fellowLiftersRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
    }

}
