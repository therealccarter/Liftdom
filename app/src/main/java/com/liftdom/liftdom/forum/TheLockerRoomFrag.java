package com.liftdom.liftdom.forum;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.liftdom.liftdom.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TheLockerRoomFrag extends Fragment {


    public TheLockerRoomFrag() {
        // Required empty public constructor
    }

    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseAdapter;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_the_locker_room, container, false);

        ButterKnife.bind(this, view);


        return view;
    }

    private void setUpRecycler(DatabaseReference databaseReference){
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(10);

        Query query = databaseReference.orderByChild("dateTime");

        FirebaseRecyclerOptions<ThreadPostModelClass> options = new FirebaseRecyclerOptions
                .Builder<ThreadPostModelClass>()
                .setQuery(query, ThreadPostModelClass.class)
                .build();

        firebaseAdapter = new FirebaseRecyclerAdapter<ThreadPostModelClass, ThreadPostViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ThreadPostViewHolder holder, int position,
                                            @NonNull ThreadPostModelClass model) {


            }

            @Override
            public ThreadPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.thread_post_list_item,
                                parent, false);

                return new ThreadPostViewHolder(view);
            }
        };

        //firebaseAdapter.setHasStableIds(true);
        loadingView.setVisibility(View.GONE);
        firebaseAdapter.startListening();
        recyclerView.setAdapter(firebaseAdapter);
    }

}
