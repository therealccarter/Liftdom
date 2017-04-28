package com.liftdom.liftdom.main_social_feed.user_search;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.template_housing.TemplateMenuFrag;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private ArrayList<String> fullUserList = new ArrayList<>();
    private HashMap<String, String> fullUserHashMap = new HashMap<>();
    private ArrayList<String> resultsUserList = new ArrayList<>();

    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.noResultsView) TextView noResultsView;
    @BindView(R.id.resultsHolder) LinearLayout resultsHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_search, container, false);

        ButterKnife.bind(this, view);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if(toolbar.getVisibility() == View.GONE){
            toolbar.setVisibility(View.VISIBLE);
        }

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
                        // Populate full user list

                        // YOU'RE GONNA NEED TO UNDERSTAND HASHMAPS BOY

                        String xUid = dataSnapshot1.getKey();
                        String userName = dataSnapshot1.getValue(String.class);
                        fullUserHashMap.put(xUid, userName);

                        ++inc;
                        if(inc == dataSnapshot.getChildrenCount()){
                            // now we have the full user list, time to compare

                            HashMap<String, String> matchingUsers = getMatchingUsers(searchString);

                            if(matchingUsers.size() == 0){
                                loadingView.setVisibility(View.GONE);
                                noResultsView.setVisibility(View.VISIBLE);
                            } else{

                                loadingView.setVisibility(View.GONE);

                                for(Map.Entry<String, String> entry : matchingUsers.entrySet()){
                                    String key = entry.getKey();
                                    String value = entry.getValue();

                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                    UserSearchResultFrag searchResultFrag = new UserSearchResultFrag();
                                    searchResultFrag.xUid = key;
                                    searchResultFrag.userName = value;

                                    fragmentTransaction.add(R.id.resultsHolder, searchResultFrag);
                                    fragmentTransaction.commit();
                                }
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

    private HashMap<String, String> getMatchingUsers(String query){
        HashMap<String, String> matchingUsers = new HashMap<>();

        for(Map.Entry<String, String> entry : fullUserHashMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();

            String[] valueSplit = value.split("\\s+");
            for(int i = 0; i < valueSplit.length; i++){
                if(valueSplit[i].equalsIgnoreCase(query)){
                    matchingUsers.put(key, value);
                    break;
                }
            }

        }

        // string.equalsIgnoreCase(userName
        return matchingUsers;
    }

}
