package com.liftdom.workout_assistor;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.liftdom.liftdom.R;

public class SaveAssistorDialog extends AppCompatActivity {

    boolean isRestDay = false;

    @BindView(R.id.saveButtonSave) Button saveButton;
    @BindView(R.id.saveButtonCancel) Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_assistor_dialog);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        //  Caused by: java.lang.NullPointerException: Attempt to invoke virtual method
        // 'java.lang.String android.os.Bundle.getString(java.lang.String)' on a null object reference
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){

                Appodeal.show(SaveAssistorDialog.this, Appodeal.INTERSTITIAL);
                Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
                    @Override
                    public void onInterstitialLoaded(boolean b) {

                    }

                    @Override
                    public void onInterstitialFailedToLoad() {

                    }

                    @Override
                    public void onInterstitialShown() {

                    }

                    @Override
                    public void onInterstitialShowFailed() {

                    }

                    @Override
                    public void onInterstitialClicked() {
                        Log.i("appodeal", "clicked");
                        setResult(1);
                        finish();
                    }

                    @Override
                    public void onInterstitialClosed() {
                        Log.i("appodeal", "closed");
                        setResult(1);
                        finish();
                    }

                    @Override
                    public void onInterstitialExpired() {

                    }
                });
                //Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
                //    @Override
                //    public void onInterstitialLoaded(boolean b) {
                //        Log.i("appodeal", "loaded");
                //    }
//
                //    @Override
                //    public void onInterstitialFailedToLoad() {
                //        Log.i("appodeal", "failed");
                //    }
//
                //    @Override
                //    public void onInterstitialShown() {
                //        Log.i("appodeal", "shown");
                //    }
//
                //    @Override
                //    public void onInterstitialClicked() {
                //        Log.i("appodeal", "clicked");
                //        setResult(1);
                //        finish();
                //    }
//
                //    @Override
                //    public void onInterstitialClosed() {
                //        Log.i("appodeal", "closed");
                //        setResult(1);
                //        finish();
                //    }
                //});

            }
        });

    }

}
