package com.liftdom.liftdom.feedback;

import android.graphics.Typeface;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.BaseActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.chat.ChatGroup.ChatGroupModelClass;
import com.liftdom.liftdom.chat.ChatSpecific.ChatMessageModelClass;
import com.liftdom.misc_activities.PremiumFeaturesActivity;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FeedbackActivity extends BaseActivity {

    private String masterId = "EtOzrDhD0VSsFOUIAcUW9KbXk353";
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.title) TextView title;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ButterKnife.bind(this);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        title.setTypeface(lobster);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpNavDrawer(FeedbackActivity.this, toolbar);
        setNavDrawerSelection(8);

        if(uid.equals(masterId)){
            addFeedbackMasterFrag();
        }else{
            DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference().child("feedbackChat").child(uid);
            feedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        if(savedInstanceState == null){
                            addFeedbackChatFrag();
                        }
                    }else{
                        createFeedbackChatNode();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    /**
     * So, we need a node for me to be able to view all of the feedback chats.
     * We also need a node for easy access for the users (/uid/).
     * But they need to both update each other.
     * So we'll just have two references. They update to both. But if the user is not me, they only see their version
     * of the chat.
     */

    private void createFeedbackChatNode(){

        // FOR ME
        final DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference().child("feedbackChat").child(uid);
        final DatabaseReference feedbackMasterRef = FirebaseDatabase.getInstance().getReference().child
                ("feedbackChatMaster");

        final String chatName = "feedback";
        final String uniqueID = UUID.randomUUID().toString();
        final String refKey = feedbackMasterRef.push().getKey();
        final HashMap<String, String> userMap = new HashMap<>();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child
                (uid).child("userName");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.getValue(String.class);
                userMap.put(uid, userName);
                //userMap.put("EtOzrDhD0VSsFOUIAcUW9KbXk353", "Brodin");
                ChatGroupModelClass chatGroupModelClass = new ChatGroupModelClass(chatName,
                        "preview text", userMap, uniqueID, refKey);
                String dateUTC = new DateTime(DateTimeZone.UTC).toString();
                chatGroupModelClass.setActiveDate(dateUTC);
                Map fanoutObject = new HashMap<>();
                fanoutObject.put("/feedbackChatMaster/" + uniqueID,
                        chatGroupModelClass);
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                rootRef.updateChildren(fanoutObject);

                // FOR THEM
                String message = "Help me improve this app with whatever feedback you have. Thanks.";

                DateTime dateTime = new DateTime(DateTimeZone.UTC);
                String dateTimeString = dateTime.toString();

                ChatMessageModelClass chatMessageModelClass = new ChatMessageModelClass(message,
                        masterId, "Brodin", dateTimeString, 0, "none");

                feedbackRef.child(uniqueID).push().setValue(chatMessageModelClass);

                addFeedbackChatFrag();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addFeedbackChatFrag(){
        loadingView.setVisibility(View.GONE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FeedbackChatFrag feedbackChatFrag = new FeedbackChatFrag();
        fragmentTransaction.add(R.id.feedbackChatFrameLayout, feedbackChatFrag);
        fragmentTransaction.commit();
    }

    private void addFeedbackMasterFrag(){
        loadingView.setVisibility(View.GONE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FeedbackChatMasterFrag feedbackChatMasterFrag = new FeedbackChatMasterFrag();
        fragmentTransaction.add(R.id.feedbackChatFrameLayout, feedbackChatMasterFrag);
        fragmentTransaction.commit();
    }
}
