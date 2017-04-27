package com.liftdom.liftdom.main_social_feed.user_search;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.pitt.loadingview.library.LoadingView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSearchFrag extends Fragment {


    public UserSearchFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public String searchString;
    private ArrayList<String> userList = new ArrayList<>();

    @BindView(R.id.loadingView) LoadingView loadingView;
    @BindView(R.id.noResultsView) TextView noResultsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_search, container, false);

        ButterKnife.bind(this, view);

        if(searchString.equals("")){
            loadingView.setVisibility(View.GONE);
            noResultsView.setVisibility(View.VISIBLE);
        }else{
            DatabaseReference userListRef = mRootRef.child("userList");
            userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int inc = 0;

                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        String userName = dataSnapshot1.getValue(String.class);
                        userList.add(userName);

                        ++inc;
                        if(inc == dataSnapshot.getChildrenCount()){
                            if(containsCaseInsensitive(searchString)){
                                loadingView.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(getView(), "Success!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }else{
                                loadingView.setVisibility(View.GONE);
                                noResultsView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return view;
    }

    private boolean containsCaseInsensitive(String userName){

        for(String string : userList){
            if(string.equalsIgnoreCase(userName)){
                return true;
            }
        }

        return false;
    }

}
