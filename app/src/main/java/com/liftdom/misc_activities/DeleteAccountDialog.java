package com.liftdom.misc_activities;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class DeleteAccountDialog extends AppCompatActivity {

    @BindView(R.id.cancelButton) Button cancelButton;
    @BindView(R.id.deleteButton) Button deleteButton;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.deleteAccountText) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account_dialog);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                // need to delete user node and then throughout app add contingencies for a dead xUid.
            }
        });
    }
}
