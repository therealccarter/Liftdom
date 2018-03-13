package com.liftdom.liftdom.notifications_bell;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.wang.avi.AVLoadingIndicatorView;

public class NotificationsActivity extends AppCompatActivity {

    String uid;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseAdapter;

    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.noNotificationsView) TextView noNotificationsView;
    @BindView(R.id.notificationsTitle) TextView notificationsTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ButterKnife.bind(this);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");

        notificationsTitleView.setTypeface(lobster);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("notifications").child(uid);
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference()
                .child("user").child(uid).child("notificationCount");
        notificationsRef.setValue("0");

        setUpFirebaseAdapter(databaseReference);

    }

    private void setUpFirebaseAdapter(DatabaseReference databaseReference){

        linearLayoutManager = new LinearLayoutManager(NotificationsActivity.this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(10);

        firebaseAdapter = new FirebaseRecyclerAdapter<NotificationModelClass, NotificationViewHolder>
                (NotificationModelClass.class, R.layout.notifications_list_item,
                        NotificationViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(NotificationViewHolder viewHolder, NotificationModelClass model, int position) {

                if(loadingView.getVisibility() == View.VISIBLE){
                    loadingView.setVisibility(View.GONE);
                }
                viewHolder.setActivity(NotificationsActivity.this);
                viewHolder.setCurrentUserId(uid);
                viewHolder.setType(model.getType());
                viewHolder.setOtherUserId(model.getUidFromOutside());
                viewHolder.setDateTime(model.getDateTime());
                if(model.getRefKey() != null){
                    viewHolder.setRefKey(model.getRefKey());
                }


            }
        };

        recyclerView.setAdapter(firebaseAdapter);
    }
}
