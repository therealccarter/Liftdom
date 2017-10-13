package com.liftdom.template_editor;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;

public class SaveTemplateDialog extends AppCompatActivity {


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    String templateName1;
    Boolean activeTemplateCheckBool;
    //Boolean algBool;
    boolean isPublic;
    boolean isEditBool;
    boolean isFromPublicSelected;
    private long delay = 50;
    private long lastTextEdit = 0;
    Handler handler = new Handler();
    List<String> templateNameList = new ArrayList<>();
    //String descriptionString;

    @BindView(R.id.saveButtonCancel) Button cancel;
    @BindView(R.id.saveButtonSave) Button save;
    @BindView(R.id.templateName) EditText templateName;
    @BindView(R.id.programNameTakenView) TextView programNameTakenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_save_template_dialog);

        ButterKnife.bind(this);

        getTemplateNames();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        String userName = sharedPref.getString("userName", "loading...");


        activeTemplateCheckBool = getIntent().getExtras().getBoolean("isActiveTemplate");

        if(getIntent().getExtras().getString("isFromPublicSelected") != null){
            if(getIntent().getExtras().getString("isFromPublicSelected").equals("yes")){
                isFromPublicSelected = true;
                templateName1 = getIntent().getExtras().getString("templateName");
                templateName.setText(templateName1);
            }
        }

        if(getIntent().getExtras().getString("isEdit") != null){
            if(getIntent().getExtras().getString("isEdit").equals("yes")){
                templateName1 = getIntent().getExtras().getString("templateName");
                templateName.setText(templateName1);
                isPublic = getIntent().getBooleanExtra("isPublic", false);
                isEditBool = true;
                //descriptionString = getIntent().getExtras().getString("description");
            }else{
                isPublic = getIntent().getBooleanExtra("isPublic", false);
                TemplateEditorSingleton.getInstance().mUserId = uid;
                TemplateEditorSingleton.getInstance().mUserName = userName;
            }
        }

        if(!isEditBool){
            templateName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    handler.removeCallbacks(inputFinishChecker);
                    programNameTakenView.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() > 0){
                        lastTextEdit = System.currentTimeMillis();
                        handler.postDelayed(inputFinishChecker, delay);
                    }
                }
            });
        }

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){

                if(isFromPublicSelected){
                    if(templateName != null && !templateName.getText().toString().equals("")){
                        Intent intent = new Intent();
                        intent.putExtra("templateName", templateName.getText().toString());
                        setResult(3, intent);
                        finish();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(SaveTemplateDialog.this);

                        // set title
                        builder.setTitle("Error");

                        // set dialog message
                        builder.setMessage("You must enter a valid template name")
                                .setCancelable(false)
                                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        dialog.dismiss();

                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = builder.create();

                        // show it
                        alertDialog.show();
                    }

                }else{
                    DateTime dateTime = new DateTime(DateTimeZone.UTC);
                    String dateTimeString = dateTime.toString();

                    if(templateName != null && !templateName.getText().toString().equals("")){
                        String isEdit = "yes";
                        Intent intent = new Intent(v.getContext(), TemplateSavedActivity.class);
                        intent.putExtra("key1", templateName.getText().toString());
                        intent.putExtra("isActiveTemplate", activeTemplateCheckBool);
                        intent.putExtra("isFromEditor", true);
                        intent.putExtra("isEdit", isEdit);
                        //intent.putExtra("isAlgorithm", algBool);

                        if(!isEditBool){
                            TemplateEditorSingleton.getInstance().mDateCreated = dateTimeString;
                        }

                        TemplateEditorSingleton.getInstance().mIsPublic = isPublic;
                        //TemplateEditorSingleton.getInstance().mDescription = descriptionString;
                        TemplateEditorSingleton.getInstance().mTemplateName = templateName.getText().toString();

                        startActivity(intent);

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(SaveTemplateDialog.this);

                        // set title
                        builder.setTitle("Error");

                        // set dialog message
                        builder.setMessage("You must enter a valid template name")
                                .setCancelable(false)
                                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        dialog.dismiss();

                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = builder.create();

                        // show it
                        alertDialog.show();
                    }
                }
            }
        });
    }

    private Runnable inputFinishChecker = new Runnable() {
        @Override
        public void run() {
            if(System.currentTimeMillis() > (lastTextEdit + delay - 500)){
                String editTextString = templateName.getText().toString();
                if(templateNameList.contains(editTextString)){
                    programNameTakenView.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private void getTemplateNames(){
        DatabaseReference templatesRef = mRootRef.child("templates").child(uid);
        templatesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    templateNameList.add(dataSnapshot1.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}














