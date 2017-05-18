package com.liftdom.template_editor;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

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
    //String descriptionString;

    @BindView(R.id.saveButtonCancel) Button cancel;
    @BindView(R.id.saveButtonSave) Button save;
    @BindView(R.id.templateName) EditText templateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_save_template_dialog);

        ButterKnife.bind(this);

        activeTemplateCheckBool = getIntent().getExtras().getBoolean("isActiveTemplate");
        //algBool = getIntent().getExtras().getBoolean("isAlgorithm");

        if(getIntent().getExtras().getString("isEdit").equals("yes")){
            templateName1 = getIntent().getExtras().getString("templateName");
            templateName.setText(templateName1);
            isPublic = getIntent().getBooleanExtra("isPublic", false);
            //descriptionString = getIntent().getExtras().getString("description");
        }else{
            isPublic = getIntent().getBooleanExtra("isPublic", false);
        }

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        final String userName = mFirebaseUser.getDisplayName();

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){

                DateTime dateTime = new DateTime(DateTimeZone.UTC);
                String dateTimeString = dateTime.toString();

                if(templateName != null && !templateName.getText().toString().equals("")){
                    String isEdit = "yes";
                    Intent intent = new Intent(v.getContext(), TemplateSavedActivity.class);
                    intent.putExtra("key1", templateName.getText().toString());
                    intent.putExtra("isActiveTemplate", activeTemplateCheckBool);
                    intent.putExtra("isFromEditor", true);
                    intent.putExtra("isEdit", isEdit );
                    //intent.putExtra("isAlgorithm", algBool);

                    TemplateEditorSingleton.getInstance().mDateCreated = dateTimeString;
                    TemplateEditorSingleton.getInstance().mIsPublic = isPublic;
                    //TemplateEditorSingleton.getInstance().mDescription = descriptionString;
                    TemplateEditorSingleton.getInstance().mTemplateName = templateName.getText().toString();
                    TemplateEditorSingleton.getInstance().mUserId = uid;
                    TemplateEditorSingleton.getInstance().mUserName = userName;
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
        });
    }
}














