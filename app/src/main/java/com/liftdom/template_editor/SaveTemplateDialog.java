package com.liftdom.template_editor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class SaveTemplateDialog extends Activity {

    @BindView(R.id.saveButtonCancel) Button cancel;
    @BindView(R.id.saveButtonSave) Button save;
    @BindView(R.id.templateName) EditText templateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_save_template_dialog);

        ButterKnife.bind(this);

        if(getIntent().getExtras().getString("isEdit").equals("yes")){
            String templateName1 = getIntent().getExtras().getString("templateName");
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
                Intent intent = new Intent(v.getContext(), TemplateSavedActivity.class);
                intent.putExtra("key1", templateName.getText().toString());
                startActivity(intent);

                EditTemplateAssemblerClass.getInstance().assembleMasterList();
            }
        });

    }
}
