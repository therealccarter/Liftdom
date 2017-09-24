package com.liftdom.template_housing;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.user_profile.UserModelClass;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;

public class RenameTemplateDialog extends AppCompatActivity {


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String templateName1 = "fail";

    ArrayList<String> arrayList1 = new ArrayList<>();
    String arrayListHeader1 = "fail";

    ArrayList<String> arrayList2 = new ArrayList<>();
    String arrayListHeader2 = "fail";

    ArrayList<String> arrayList3 = new ArrayList<>();
    String arrayListHeader3 = "fail";

    ArrayList<String> arrayList4 = new ArrayList<>();
    String arrayListHeader4 = "fail";

    ArrayList<String> arrayList5 = new ArrayList<>();
    String arrayListHeader5 = "fail";

    ArrayList<String> arrayList6 = new ArrayList<>();
    String arrayListHeader6 = "fail";

    ArrayList<String> arrayList7 = new ArrayList<>();
    String arrayListHeader7 = "fail";

    ArrayList<String> algorithmArrayList = new ArrayList<>();
    ArrayList<String> algorithmExList = new ArrayList<>();
    TemplateModelClass templateModelClass;
    boolean isFromPublic;

    @BindView(R.id.templateNameEditText) EditText templateNameEditText;
    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.cancelButton) Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rename_template_dialog);

        ButterKnife.bind(this);

        templateName1 = getIntent().getExtras().getString("templateName");
        templateNameEditText.setText(templateName1);

        final DatabaseReference templateRef = mRootRef.child("templates").child(uid).child(templateName1);

        templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        templateModelClass = dataSnapshot.getValue(TemplateModelClass.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });



        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                templateRef.setValue(null);

                final String templateNameNew = templateNameEditText.getText().toString();

                final DatabaseReference activeTemplateRef = mRootRef.child("user").child(uid);
                activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                        if(userModelClass.getActiveTemplate() != null){
                            if(userModelClass.getActiveTemplate().equals(templateName1)){
                                userModelClass.setActiveTemplate(templateNameNew);
                                activeTemplateRef.setValue(userModelClass);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                DateTime dateTime = new DateTime(DateTimeZone.UTC);
                String dateUpdated = dateTime.toString();

                DatabaseReference newTemplateRef = mRootRef.child("templates").child(uid).child(templateNameNew);
                DatabaseReference publicTemplateRef = mRootRef.child("public_templates").child("my_public").child
                        (uid).child(templateNameNew);
                templateModelClass.setTemplateName(templateNameNew);
                templateModelClass.setDateUpdated(dateUpdated);

                if(isFromPublic){
                    publicTemplateRef.setValue(templateModelClass);
                }else{
                    newTemplateRef.setValue(templateModelClass);
                }


                setResult(1);

                finish();

                }
            });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });

    }



    @Override
    public void onStart() {
        super.onStart();



    }


}
