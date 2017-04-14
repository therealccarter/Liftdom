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
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;

public class SaveTemplateDialog extends AppCompatActivity {


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.saveButtonCancel) Button cancel;
    @BindView(R.id.saveButtonSave) Button save;
    @BindView(R.id.templateName) EditText templateName;

    String templateName1;
    Boolean activeTemplateCheckBool;
    Boolean algBool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_save_template_dialog);

        //TODO: input validation, don't allow no-name templates

        ButterKnife.bind(this);

        activeTemplateCheckBool = getIntent().getExtras().getBoolean("isActiveTemplate");
        algBool = getIntent().getExtras().getBoolean("isAlgorithm");

        if(getIntent().getExtras().getString("isEdit").equals("yes")){
            templateName1 = getIntent().getExtras().getString("templateName");
            templateName.setText(templateName1);
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

                if(templateName != null && !templateName.getText().toString().equals("")){
                    String isEdit = "yes";
                    Intent intent = new Intent(v.getContext(), TemplateSavedActivity.class);
                    intent.putExtra("key1", templateName.getText().toString());
                    intent.putExtra("isActiveTemplate", activeTemplateCheckBool);
                    intent.putExtra("isFromEditor", true);
                    intent.putExtra("isEdit", isEdit );
                    intent.putExtra("isAlgorithm", algBool);
                    startActivity(intent);
                    EditTemplateAssemblerClass.getInstance().assembleMasterList();
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














