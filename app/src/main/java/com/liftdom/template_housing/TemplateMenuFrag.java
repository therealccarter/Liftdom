package com.liftdom.template_housing;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.appodeal.ads.Appodeal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.FirstTimeModelClass;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.utils.MotivationalQuotes;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateEditorActivity;
import com.mikepenz.materialdrawer.Drawer;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;

/**
 * A simple {@link Fragment} subclass.
 */
public class TemplateMenuFrag extends Fragment {

    int templateOptionsCheck = 0;

    String[] quoteArray;
    public MainActivity mainActivity;
    public boolean isFromIntent = false;

    public TemplateMenuFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.fromScratch) Button fromScratch;
    @BindView(R.id.savedTemplates) Button savedTemplates;
    @BindView(R.id.userMadeTemplates) Button userMadeTemplates;
    @BindView(R.id.premadeTemplates) Button premadeTemplates;
    @BindView(R.id.quoteBody) TextView quoteBody;
    @BindView(R.id.quoteAuthor) TextView quoteAuthor;

    headerChangeFromFrag mCallback;
    //navDrawerSelectorFromFrag navDrawerCallback;

    public interface headerChangeFromFrag{
        void changeHeaderTitle(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeaderTitle(title);
    }

    bottomNavChanger navChangerCallback;

    public interface bottomNavChanger{
        void setBottomNavIndex(int navIndex);
    }

    private void navChanger(int navIndex){
        navChangerCallback.setBottomNavIndex(navIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_template_menu, container, false);

        ButterKnife.bind(this, view);

        //String appKey = "e05b98bf43240a8687216b4e3106a598ced75a344b6c75f2";
        //Appodeal.initialize(getActivity(), appKey, Appodeal.BANNER);
        //Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);

        navChanger(1);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        MotivationalQuotes motivationalQuotes = new MotivationalQuotes();
        quoteArray = motivationalQuotes.getQuote();

        //BottomNavigation bottomNavigation = (BottomNavigation) getActivity().findViewById(R.id.BottomNavigation);
        //bottomNavigation.setSelectedIndex(0, false);

        if(savedInstanceState == null){
            quoteBody.setText(quoteArray[0]);
            quoteAuthor.setText(quoteArray[1]);
        }else{
            quoteArray = savedInstanceState.getStringArray("quoteArray");
            quoteBody.setText(quoteArray[0]);
            quoteAuthor.setText(quoteArray[1]);
        }

        savedTemplates.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new SavedTemplatesFrag(), "myTemplatesTag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        userMadeTemplates.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new PublicTemplateChooserFrag());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        final int padding = getResources().getDimensionPixelOffset(R.dimen.seven_dip);


        fromScratch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                String isEdit = "no";
                Intent intent = new Intent(v.getContext(), TemplateEditorActivity.class);
                intent.putExtra("isEdit", isEdit );
                startActivity(intent);
            }
        });


        premadeTemplates.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new PremadeTemplatesFrag(),
                        "premadeTemplatesTag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putStringArray("quoteArray", quoteArray);

    }

    @Override
    public void onStart(){
        super.onStart();
        headerChanger("Workout Programming");

        final DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child
                ("firstTime").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child
                ("isTemplateMenuFirstTime");
        firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    new FancyShowCaseView.Builder(getActivity())
                            .title("This is the Workout Programming page. \n " +
                                    "You can create a new program, view your saved programs," +
                                    "or check out user-made/pre-made programs.")
                            .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                            .build()
                            .show();
                    firstTimeRef.setValue(null);

                    //fromScratch.setEnabled(false);
                    //userMadeTemplates.setEnabled(false);
                    //premadeTemplates.setEnabled(false);
//
                    //FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(getActivity())
                    //        .title("This is where you'll handle all workout programming." + System.getProperty
                    //                ("line.separator") + System.getProperty
                    //                ("line.separator") + "Create workouts from " +
                    //                "scratch, view your saved programs, and view pre-made or user-made programs.")
                    //        .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                    //        .build();
//
                    //FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder(getActivity())
                    //        .focusOn(savedTemplates)
                    //        .title("Hit the Saved Programs button now!")
                    //        .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                    //        .focusShape(FocusShape.ROUNDED_RECTANGLE)
                    //        .roundRectRadius(90)
                    //        .fitSystemWindows(true)
                    //        .build();
//
                    //new FancyShowCaseQueue()
                    //        .add(fancyShowCaseView1)
                    //        .add(fancyShowCaseView2)
                    //        .show();
//
                    //firstTimeRef.setValue(null);
                }else{
                    //final DatabaseReference secondTimeRef = FirebaseDatabase.getInstance().getReference().child
                    //        ("firstTime")
                    //        .child(FirebaseAuth.getInstance().getCurrentUser().getxUid()).child
                    //                ("isTemplateMenuSecondTime");
                    //secondTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    //    @Override
                    //    public void onDataChange(DataSnapshot dataSnapshot) {
                    //        if(dataSnapshot.exists()){
//
                    //            savedTemplates.setEnabled(false);
                    //            userMadeTemplates.setEnabled(false);
                    //            premadeTemplates.setEnabled(false);
//
                    //            new FancyShowCaseView.Builder(getActivity())
                    //                    .focusOn(fromScratch)
                    //                    .title("Now create your first custom workout!")
                    //                    .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                    //                    .focusShape(FocusShape.ROUNDED_RECTANGLE)
                    //                    .roundRectRadius(90)
                    //                    .fitSystemWindows(true)
                    //                    .build().show();
                    //            secondTimeRef.setValue(null);
                    //        }
                    //    }
//
                    //    @Override
                    //    public void onCancelled(DatabaseError databaseError) {
//
                    //    }
                    //});
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeFromFrag) activity;
            navChangerCallback = (bottomNavChanger) activity;
        } catch (ClassCastException e) {
            //throw new ClassCastException(activity.toString()
            //        + " must implement OnHeadlineSelectedListener");
        }
    }


}
