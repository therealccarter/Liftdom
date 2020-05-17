package com.liftdom.workout_programs.FiveThreeOne_ForBeginners;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.InputFilterMinMax;
import io.github.dreierf.materialintroscreen.SlideFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class W531fBIntroFrag3 extends SlideFragment {

    public W531fBIntroFrag3() {
        // Required empty public constructor
    }

    boolean pushRepsCorrect = false;
    boolean pullRepsCorrect = false;
    boolean legsCoreRepsCorrect = false;

    private long delay = 50;
    private long lastTextEdit = 0;
    Handler handler = new Handler();

    @BindView(R.id.setsPush) EditText setsPush;
    @BindView(R.id.repsPush) EditText repsPush;
    @BindView(R.id.needsMorePush) TextView needsMorePush;
    @BindView(R.id.needsLessPush) TextView needsLessPush;
    @BindView(R.id.setsPull) EditText setsPull;
    @BindView(R.id.repsPull) EditText repsPull;
    @BindView(R.id.needsMorePull) TextView needsMorePull;
    @BindView(R.id.needsLessPull) TextView needsLessPull;
    @BindView(R.id.setsLegCore) EditText setsLegCore;
    @BindView(R.id.repsLegCore) EditText repsLegCore;
    @BindView(R.id.needsMoreLegCore) TextView needsMoreLegCore;
    @BindView(R.id.needsLessLegCore) TextView needsLessLegCore;
    @BindView(R.id.assistanceTitle) TextView assistanceTitle;
    @BindView(R.id.pushRepCount) TextView pushRepCount;
    @BindView(R.id.pullRepCount) TextView pullRepCount;
    @BindView(R.id.legCoreRepCount) TextView legCoreRepCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_w531f_b_intro_frag3, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");
        assistanceTitle.setTypeface(lobster);

        setsPush.setFilters(new InputFilter[]{new InputFilterMinMax(1, 20)});
        setsPull.setFilters(new InputFilter[]{new InputFilterMinMax(1, 20)});
        setsLegCore.setFilters(new InputFilter[]{new InputFilterMinMax(1, 20)});

        setsPush.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(inputFinishChecker);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lastTextEdit = System.currentTimeMillis();
                handler.postDelayed(inputFinishChecker, delay);
            }
        });

        repsPush.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(inputFinishChecker);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lastTextEdit = System.currentTimeMillis();
                handler.postDelayed(inputFinishChecker, delay);
            }
        });

        setsPull.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(inputFinishChecker);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lastTextEdit = System.currentTimeMillis();
                handler.postDelayed(inputFinishChecker, delay);
            }
        });

        repsPull.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(inputFinishChecker);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lastTextEdit = System.currentTimeMillis();
                handler.postDelayed(inputFinishChecker, delay);
            }
        });

        setsLegCore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(inputFinishChecker);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lastTextEdit = System.currentTimeMillis();
                handler.postDelayed(inputFinishChecker, delay);
            }
        });

        repsLegCore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(inputFinishChecker);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lastTextEdit = System.currentTimeMillis();
                handler.postDelayed(inputFinishChecker, delay);
            }
        });

        return view;
    }

    private Runnable inputFinishChecker = new Runnable() {
        @Override
        public void run() {
            if(System.currentTimeMillis() > (lastTextEdit + delay - 500)){
                checkAll();
            }
        }
    };

    private void checkAll(){
        String push = isPushCorrect();
        if(push.equals("less")){
            needsLessPush.setVisibility(View.VISIBLE);
            needsMorePush.setVisibility(View.GONE);
            pushRepsCorrect = false;
        }else if(push.equals("more")){
            needsLessPush.setVisibility(View.GONE);
            needsMorePush.setVisibility(View.VISIBLE);
            pushRepsCorrect = false;
        }else if(push.equals("correct")){
            needsLessPush.setVisibility(View.GONE);
            needsMorePush.setVisibility(View.GONE);
            pushRepsCorrect = true;
        }

        String pull = isPullCorrect();
        if(pull.equals("less")){
            needsLessPull.setVisibility(View.VISIBLE);
            needsMorePull.setVisibility(View.GONE);
            pullRepsCorrect = false;
        }else if(pull.equals("more")){
            needsLessPull.setVisibility(View.GONE);
            needsMorePull.setVisibility(View.VISIBLE);
            pullRepsCorrect = false;
        }else if(pull.equals("correct")){
            needsLessPull.setVisibility(View.GONE);
            needsMorePull.setVisibility(View.GONE);
            pullRepsCorrect = true;
        }

        String legCore = isLegCoreCorrect();
        if(legCore.equals("less")){
            needsLessLegCore.setVisibility(View.VISIBLE);
            needsMoreLegCore.setVisibility(View.GONE);
            legsCoreRepsCorrect = false;
        }else if(legCore.equals("more")){
            needsLessLegCore.setVisibility(View.GONE);
            needsMoreLegCore.setVisibility(View.VISIBLE);
            legsCoreRepsCorrect = false;
        }else if(legCore.equals("correct")){
            needsLessLegCore.setVisibility(View.GONE);
            needsMoreLegCore.setVisibility(View.GONE);
            legsCoreRepsCorrect = true;
        }

    }

    private String isPushCorrect(){
        int sets;
        int reps;
        if(setsPush.getText().toString().isEmpty()){
            sets = 0;
        }else{
            sets = Integer.parseInt(setsPush.getText().toString());
        }
        if(repsPush.getText().toString().isEmpty()){
            reps = 0;
        }else{
            reps = Integer.parseInt(repsPush.getText().toString());
        }

        int overall = sets * reps;

        String repCount = "(" + overall + " reps)";
        pushRepCount.setText(repCount);

        if(overall > 100){
            return "less";
        }else if(overall < 50){
            return "more";
        }else{
            return "correct";
        }
    }

    private String isPullCorrect(){
        int sets;
        int reps;
        if(setsPull.getText().toString().isEmpty()){
            sets = 0;
        }else{
            sets = Integer.parseInt(setsPull.getText().toString());
        }
        if(repsPull.getText().toString().isEmpty()){
            reps = 0;
        }else{
            reps = Integer.parseInt(repsPull.getText().toString());
        }

        int overall = sets * reps;

        String repCount = "(" + overall + " reps)";
        pullRepCount.setText(repCount);

        if(overall > 100){
            return "less";
        }else if(overall < 50){
            return "more";
        }else{
            return "correct";
        }
    }

    private String isLegCoreCorrect(){
        int sets;
        int reps;
        if(setsLegCore.getText().toString().isEmpty()){
            sets = 0;
        }else{
            sets = Integer.parseInt(setsLegCore.getText().toString());
        }
        if(repsLegCore.getText().toString().isEmpty()){
            reps = 0;
        }else{
            reps = Integer.parseInt(repsLegCore.getText().toString());
        }

        int overall = sets * reps;

        String repCount = "(" + overall + " reps)";
        legCoreRepCount.setText(repCount);

        if(overall > 100){
            return "less";
        }else if(overall < 50){
            return "more";
        }else{
            return "correct";
        }
    }

    @Override
    public boolean canMoveFurther(){
        boolean moveFurther = false;

        if(isPushCorrect().equals("correct")
        && isPullCorrect().equals("correct")
        && isLegCoreCorrect().equals("correct")){
            W531fBSingleton.getInstance().pushSetScheme =
                    setsPush.getText().toString() + "x" + repsPush.getText().toString();
            W531fBSingleton.getInstance().pullSetScheme =
                    setsPull.getText().toString() + "x" + repsPull.getText().toString();
            W531fBSingleton.getInstance().legCoreSetScheme =
                    setsLegCore.getText().toString() + "x" + repsLegCore.getText().toString();

            moveFurther = true;
        }

        return moveFurther;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.invalidW5314BFields);
    }

    @Override
    public int backgroundColor() {
        return R.color.grey;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

}
