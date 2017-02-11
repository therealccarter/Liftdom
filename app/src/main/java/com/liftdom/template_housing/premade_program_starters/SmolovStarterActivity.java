package com.liftdom.template_housing.premade_program_starters;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.ExercisePickerActivity;
import com.liftdom.template_editor.TemplateSavedActivity;

public class SmolovStarterActivity extends AppCompatActivity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.activeTemplateCheckbox) CheckBox activeTemplateCheckbox;
    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.oneRepMaxEditText) EditText oneRepMaxEditText;
    @BindView(R.id.movementName) Button movementName;
    @BindView(R.id.smolovTitle) TextView smolovTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smolov_starter);

        ButterKnife.bind(this);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        smolovTitle.setTypeface(lobster);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //SmolovSavedActivity smolovSavedActivity = new SmolovSavedActivity(oneRepMaxEditText.getText()
                //        .toString(), movementName.getText().toString());

                Intent intent;
                intent = new Intent(SmolovStarterActivity.this, SmolovSavedActivity.class);
                intent.putExtra("1rm", oneRepMaxEditText.getText()
                        .toString());
                intent.putExtra("exName", movementName.getText().toString());
                if(activeTemplateCheckbox.isChecked()){
                    intent.putExtra("isActive", true);
                }else{
                    intent.putExtra("isActive", false);
                }
                startActivity(intent);
            }
        });

        movementName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExercisePickerActivity.class);
                int exID = movementName.getId();
                intent.putExtra("exID", exID);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode == 2)
        {
            if(data.getStringExtra("MESSAGE") != null && data != null ) {
                String message = data.getStringExtra("MESSAGE");
                movementName.setText(message);
            }
        }
    }
}
