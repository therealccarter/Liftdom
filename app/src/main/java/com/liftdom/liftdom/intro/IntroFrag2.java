package com.liftdom.liftdom.intro;


import agency.tango.materialintroscreen.SlideFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFrag2 extends SlideFragment {


    public IntroFrag2() {
        // Required empty public constructor
    }

    private EditText displayNameEditText;
    private TextView usernameTaken;
    private TextView usernameAvailable;
    private AVLoadingIndicatorView loadingView;
    private long delay = 1000;
    private long lastTextEdit = 0;
    Handler handler = new Handler();
    private boolean isAvailable;
    private String currentText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_frag2, container, false);

        displayNameEditText = (EditText) view.findViewById(R.id.displayNameEditText);
        usernameAvailable = (TextView) view.findViewById(R.id.usernameAvailableView);
        usernameTaken = (TextView) view.findViewById(R.id.usernameTakenView);
        loadingView = (AVLoadingIndicatorView) view.findViewById(R.id.loadingView);

        HideKey.initialize(getActivity(), view);

        displayNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isAvailable = false;
                handler.removeCallbacks(inputFinishChecker);
                loadingView.setVisibility(View.VISIBLE);
                usernameTaken.setVisibility(View.GONE);
                usernameAvailable.setVisibility(View.GONE);
                if(s.length() == 0){
                    loadingView.setVisibility(View.GONE);
                    usernameTaken.setVisibility(View.GONE);
                    usernameAvailable.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    lastTextEdit = System.currentTimeMillis();
                    handler.postDelayed(inputFinishChecker, delay);
                }
            }
        });

        return view;
    }

    private Runnable inputFinishChecker = new Runnable(){
        public void run(){
            if(System.currentTimeMillis() > (lastTextEdit + delay - 500)){
                DatabaseReference userNameRef = FirebaseDatabase.getInstance().getReference().child("userNames")
                        .child(displayNameEditText.getText().toString());
                userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            loadingView.setVisibility(View.GONE);
                            usernameTaken.setVisibility(View.VISIBLE);
                            usernameAvailable.setVisibility(View.GONE);
                            isAvailable = false;
                        }else{
                            loadingView.setVisibility(View.GONE);
                            usernameTaken.setVisibility(View.GONE);
                            usernameAvailable.setVisibility(View.VISIBLE);
                            isAvailable = true;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    };



    @Override
    public int backgroundColor() {
        return R.color.grey;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

    @Override
    public boolean canMoveFurther() {
        boolean validName = false;
        String displayName = displayNameEditText.getText().toString();

        if(displayName != null){
            if(!displayName.equals("")){
                if(displayName.length() > 2 && isAvailable){
                    validName = true;
                    IntroSingleton.getInstance().displayName = displayName;
                }
            }
        }

        return validName;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        if(!isAvailable){
            return getString(R.string.invalidDisplayNameTaken);
        }else{
            return getString(R.string.invalidDisplayName);
        }
    }
}
