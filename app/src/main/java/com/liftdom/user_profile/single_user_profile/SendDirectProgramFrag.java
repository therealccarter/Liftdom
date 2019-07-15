package com.liftdom.user_profile.single_user_profile;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.notifications_bell.NotificationModelClass;
import com.liftdom.template_editor.TemplateModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendDirectProgramFrag extends Fragment {


    public SendDirectProgramFrag() {
        // Required empty public constructor
    }

    public String uidFromOutside;
    String userNameFromOutside;
    public String templateName;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.sendTemplateTitleView) TextView sendTemplateTitleView;
    @BindView(R.id.sendTemplateButton) Button sendTemplateButton;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_direct_program, container, false);

        ButterKnife.bind(this, view);

        DatabaseReference userNameRef = FirebaseDatabase.getInstance().getReference().child("user").child
                (uidFromOutside).child("userName");
        userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    userNameFromOutside = dataSnapshot.getValue(String.class);
                    String cat = "Send " + templateName + " to " + userNameFromOutside + "?";
                    sendTemplateTitleView.setText(cat);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView.setVisibility(View.VISIBLE);
                sendTemplateTitleView.setVisibility(View.GONE);
                sendTemplateButton.setVisibility(View.GONE);
                sendTemplate();
            }
        });

        return view;
    }

    private void sendTemplate(){
        DatabaseReference templateRef = FirebaseDatabase.getInstance().getReference().child("templates").child(uid)
                .child(templateName);
        templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TemplateModelClass templateModelClass = dataSnapshot.getValue(TemplateModelClass.class);
                templateModelClass.setUserId2(uidFromOutside);
                templateModelClass.setUserName2(userNameFromOutside);
                DateTime dateTime = new DateTime(DateTimeZone.UTC);
                final String dateUpdated = dateTime.toString();
                templateModelClass.setDateUpdated(dateUpdated);
                templateModelClass.setAlgorithmDateMap(null);

                DatabaseReference inboxRef = FirebaseDatabase.getInstance().getReference().child("templatesInbox")
                        .child(uidFromOutside).child(templateName);
                inboxRef.setValue(templateModelClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadingView.setVisibility(View.GONE);
                        sendTemplateTitleView.setText("Program Sent!");
                        sendTemplateTitleView.setVisibility(View.VISIBLE);

                        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                .child("user").child(uidFromOutside).child("notificationCount");
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference()
                                        .child("notifications").child(uidFromOutside);

                                NotificationModelClass notificationModelClass = new NotificationModelClass(
                                        "programSent", uid, templateName, dateUpdated, null
                                );

                                notificationRef.push().setValue(notificationModelClass);

                                if(dataSnapshot.exists()){
                                    int count = Integer.parseInt(dataSnapshot.getValue(String.class));
                                    count++;
                                    userRef.setValue(String.valueOf(count));
                                }else{
                                    userRef.setValue("1");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingView.setVisibility(View.GONE);
                        sendTemplateTitleView.setText("Something went wrong, try again later.");
                        sendTemplateTitleView.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
