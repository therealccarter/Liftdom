package com.liftdom.liftdom.notifications_bell;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.BaseActivity;
import com.liftdom.liftdom.R;
import com.wang.avi.AVLoadingIndicatorView;

public class NotificationsActivity extends BaseActivity {

    String uid;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseAdapter;

    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.noNotificationsView) TextView noNotificationsView;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ButterKnife.bind(this);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");

        title.setTypeface(lobster);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("notifications").child(uid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    setUpFirebaseAdapter(databaseReference);
                }else{
                    loadingView.setVisibility(View.GONE);
                    noNotificationsView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference()
                .child("user").child(uid).child("notificationCount");
        notificationsRef.setValue("0");

        setUpNavDrawer(NotificationsActivity.this, toolbar);

    }

    private void setUpFirebaseAdapter(DatabaseReference databaseReference){

        linearLayoutManager = new LinearLayoutManager(NotificationsActivity.this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(10);

        Query query = databaseReference.orderByChild("dateTime");

        firebaseAdapter = new FirebaseRecyclerAdapter<NotificationModelClass, NotificationViewHolder>
                (NotificationModelClass.class, R.layout.notifications_list_item,
                        NotificationViewHolder.class, query) {
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

                try{
                    if(position != firebaseAdapter.getItemCount() - 1){
                        NotificationModelClass modelClass2 = (NotificationModelClass) firebaseAdapter.getItem
                                (position + 1);
                        if(!model.getFormattedDateTime().equals(modelClass2.getFormattedDateTime())){
                            viewHolder.showDividerAbove();
                        }
                    }
                }catch (NullPointerException e){

                }

            }
        };

        recyclerView.setAdapter(firebaseAdapter);
    }
}
