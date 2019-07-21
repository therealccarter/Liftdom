package com.liftdom.user_profile.single_user_profile;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.template_housing.SavedTemplatesFrag;

public class ProfileActionsDialogActivity extends AppCompatActivity {

    String uidFromOutside;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.profileActionsHolder) LinearLayout profileActionsHolder;
    @BindView(R.id.closeButton) Button closeButton;
    @BindView(R.id.noSavedTemplatesView) TextView noSavedTemplatesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_actions_dialog);
        //setFinishOnTouchOutside(true);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        uidFromOutside = getIntent().getStringExtra("uidFromOutside");

        if(getIntent().getStringExtra("action") != null){
            if(getIntent().getStringExtra("action").equals("1")){
                addSendMessageFrag();
            }else{
                DatabaseReference templateRef = FirebaseDatabase.getInstance().getReference().child("templates")
                        .child(uid);
                templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            noSavedTemplatesView.setVisibility(View.GONE);
                            addSendProgramFrag();
                        }else{
                            noSavedTemplatesView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addSendMessageFrag(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SendDirectMessageFrag sendDirectMessageFrag = new SendDirectMessageFrag();
        sendDirectMessageFrag.uidFromOutside = uidFromOutside;
        fragmentTransaction.replace(profileActionsHolder.getId(), sendDirectMessageFrag);
        fragmentTransaction.commit();
    }

    private void addSendProgramFrag(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SavedTemplatesFrag savedTemplatesFrag = new SavedTemplatesFrag();
        savedTemplatesFrag.isFromSendProgram = true;
        savedTemplatesFrag.uidFromOutside = uidFromOutside;
        fragmentTransaction.replace(profileActionsHolder.getId(), savedTemplatesFrag);
        fragmentTransaction.commit();
    }

}
