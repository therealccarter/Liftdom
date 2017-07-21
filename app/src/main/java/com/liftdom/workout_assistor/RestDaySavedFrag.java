package com.liftdom.workout_assistor;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestDaySavedFrag extends Fragment {


    public RestDaySavedFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.goBackHome) Button goHomeButton;
    @BindView(R.id.finishedTextView) TextView finishedTextView;
    @BindView(R.id.powerLevelTextView) TextView powerLevelTextView;
    @BindView(R.id.currentPowerXpTextView) TextView powerLevelXpView1;
    @BindView(R.id.goalPowerXpTextView) TextView powerLevelXpView2;
    @BindView(R.id.xpGainedOverallView) TextView xpGainedView;
    @BindView(R.id.xpFromWorkoutView) TextView xpFromWorkoutView;
    @BindView(R.id.completionMultiplierView) TextView streakMultiplierView;
    @BindView(R.id.completionStreakView) TextView streakView;
    @BindView(R.id.totalXpGainedLL) LinearLayout totalXpGainedLL;
    @BindView(R.id.xpFromWorkoutLL) LinearLayout xpFromWorkoutLL;
    @BindView(R.id.streakMultiplierLL) LinearLayout streakMultiplierLL;
    @BindView(R.id.dailyStreakLL) LinearLayout dailyStreakLL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistor_saved, container, false);

        ButterKnife.bind(this, view);

        finishedTextView.setText("REST DAY COMPLETED");

        powerLevelXpView1.setText("0");

        totalXpGainedLL.setAlpha(0);
        xpFromWorkoutLL.setAlpha(0);
        streakMultiplierLL.setAlpha(0);
        dailyStreakLL.setAlpha(0);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        fadeInViews();
    }

    private void fadeInViews(){
        dailyStreakLL.animate().alpha(1).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //startCounterAnimation(0, 12, streakView);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        streakMultiplierLL.animate().alpha(1).setDuration(1300).start();
        xpFromWorkoutLL.animate().alpha(1).setDuration(1600).start();
        totalXpGainedLL.animate().alpha(1).setDuration(2000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                startCounterAnimation(0, 100, xpGainedView);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //scaleXp1(powerLevelXpView1);
                startCounterAnimation(0, 50, powerLevelXpView1);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void scaleXp1(TextView textView){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.simple_scale_1);
        textView.startAnimation(animation);
    }

    private void startCounterAnimation(int initialNumber, int finalNumber, final TextView textView){
        ValueAnimator animator = ValueAnimator.ofInt(initialNumber, finalNumber);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText(animation.getAnimatedValue().toString());
            }
        });
        if(textView == powerLevelXpView1){
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    powerLevelTextView.setText("4");
                    scaleXp1(powerLevelTextView);
                    konfetti();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        animator.start();
}

    private void konfetti(){
        KonfettiView konfettiView = (KonfettiView) getActivity().findViewById(R.id.viewKonfetti);
        konfettiView.build()
                .addColors(Color.parseColor("#D1B91D"), Color.WHITE)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 3f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .stream(300, 3000L);
    }



}
