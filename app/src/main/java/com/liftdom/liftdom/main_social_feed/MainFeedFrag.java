package com.liftdom.liftdom.main_social_feed;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutModelClass;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutViewHolder;
import com.liftdom.liftdom.main_social_feed.utils.RandomUsersBannerFrag;
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.template_housing.TemplateMenuFrag;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import me.toptas.fancyshowcase.FancyShowCaseView;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import org.joda.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFeedFrag extends Fragment implements RandomUsersBannerFrag.removeFragCallback{

    public MainFeedFrag() {
        // Required empty public constructor
    }

    headerChangeFromFrag mCallback;

    public interface headerChangeFromFrag{
        public void changeHeaderTitle(String title);
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

    private String uid;
    private DatabaseReference rootRef;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseAdapter;

    boolean isFirstKonfetti;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.noPostsView) TextView noPostsView;
    @BindView(R.id.randomUsersBannerLL) LinearLayout randomUsersBannerLL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_social_feed, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        navChanger(0);
        headerChanger("Home");


        //if(!MainActivitySingleton.getInstance().isBannerViewInitialized){
        //    String appKey = "e05b98bf43240a8687216b4e3106a598ced75a344b6c75f2";
        //    Appodeal.initialize(getActivity(), appKey, Appodeal.BANNER);
        //    Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
        //    MainActivitySingleton.getInstance().isBannerViewInitialized = true;
        //}else{
        //    //Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
        //}

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getContext(), SignInActivity.class));
        }else{
            //showTutorialChoice();
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            rootRef = FirebaseDatabase.getInstance().getReference();
            if(savedInstanceState == null){
                checkForRandomUsersBanner();
            }
            final DatabaseReference socialRef = rootRef.child("feed").child(uid);
            socialRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    firstTimeTutorial();
                    if(dataSnapshot.exists()){
                        checkForReleaseNotes();
                        DatabaseReference userRef = rootRef.child("user").child(uid);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                                if(userModelClass.isIsImperial()){
                                    loadingView.setVisibility(View.GONE);
                                    noPostsView.setVisibility(View.GONE);
                                    setUpFirebaseAdapter(socialRef, true);
                                }else{
                                    loadingView.setVisibility(View.GONE);
                                    noPostsView.setVisibility(View.GONE);
                                    setUpFirebaseAdapter(socialRef, false);
                                }
                                //kablam();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }else{
                        loadingView.setVisibility(View.GONE);
                        noPostsView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return view;
    }

    private void kablam(){
        DatabaseReference burkRef = FirebaseDatabase.getInstance().getReference().child("templates").child
                ("EcCB9ayXcegCctEaT1Y7n98NC5G2").child("TryAgn2ElecB");
        burkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TemplateModelClass templateModelClass = dataSnapshot.getValue(TemplateModelClass.class);
                templateModelClass.setUserName2("Brodin");
                templateModelClass.setUserId2(uid);
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("templatesInbox").child
                        (uid).child("TryAgn2ElecB");
                myRef.setValue(templateModelClass);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkForRandomUsersBanner(){
        final DatabaseReference randomUsersRef = FirebaseDatabase.getInstance().getReference().child("mainFeedBanner").child
                (FirebaseAuth.getInstance().getCurrentUser().getUid());
        randomUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentDate = LocalDate.now().toString();
                String day = LocalDate.now().toString("dd");
                double dayDouble = Double.parseDouble(day);
                if(dayDouble % (double) 3 == 0.0){
                    if(dataSnapshot.exists()){
                        String delims = "[_]";
                        String value = dataSnapshot.getValue(String.class);
                        String[] tokens = value.split(delims);
                        if(tokens[0].equals(currentDate) && tokens[1].equals("open")){
                            FragmentManager fragmentManager = getChildFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            RandomUsersBannerFrag bannerFrag = new RandomUsersBannerFrag();
                            fragmentTransaction.replace(R.id.randomUsersBannerLL, bannerFrag, "randomUsersBanner");
                            fragmentTransaction.commitAllowingStateLoss();
                        }else if(!tokens[0].equals(currentDate)){
                            FragmentManager fragmentManager = getChildFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            RandomUsersBannerFrag bannerFrag = new RandomUsersBannerFrag();
                            fragmentTransaction.replace(R.id.randomUsersBannerLL, bannerFrag, "randomUsersBanner");
                            fragmentTransaction.commitAllowingStateLoss();
                            randomUsersRef.setValue(currentDate + "_open");
                        }
                    }else{
                        try{
                            FragmentManager fragmentManager = getChildFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            RandomUsersBannerFrag bannerFrag = new RandomUsersBannerFrag();
                            fragmentTransaction.replace(R.id.randomUsersBannerLL, bannerFrag, "randomUsersBanner");
                            fragmentTransaction.commitAllowingStateLoss();
                        }catch (IllegalStateException e){
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("fragID",  0);
                            startActivity(intent);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void removeRandomUsersBanner(){
        String currentDate = LocalDate.now().toString();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragmentManager.findFragmentByTag("randomUsersBanner")).commit();
        DatabaseReference randomUsersRef = FirebaseDatabase.getInstance().getReference().child("mainFeedBanner").child
                (FirebaseAuth.getInstance().getCurrentUser().getUid());
        randomUsersRef.setValue(currentDate + "_closed");
        Toast.makeText(getContext(), "You can always view more users in the forum!", Toast.LENGTH_SHORT).show();
    }

    private void konfetti(){
        KonfettiView konfettiView = (KonfettiView) getActivity().findViewById(R.id.viewKonfetti);

        if(isFirstKonfetti){
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

            isFirstKonfetti = false;
        }
    }

    // RELEASE NOTES
    private void checkForReleaseNotes(){
        if(!MainActivitySingleton.getInstance().isReleaseCheck){
            DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child("firstTime").child
                    (uid).child("isTemplateMenuFirstTime");
            firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        final int currentVersionInt = 124;
                        final String currentVersionString = String.valueOf(currentVersionInt);
                        final DatabaseReference currentVersionRef = FirebaseDatabase.getInstance().getReference().child("versionCheck")
                                .child(uid);
                        currentVersionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.exists()){
                                    MainActivitySingleton.getInstance().isReleaseCheck = true;
                                    // display release notes for currentVersionString
                                    currentVersionRef.setValue(currentVersionString);
                                    Intent intent = new Intent(getContext(), ReleaseNotesActivity.class);
                                    isFirstKonfetti = true;
                                    konfetti();
                                    startActivity(intent);
                                }else{
                                    MainActivitySingleton.getInstance().isReleaseCheck = true;
                                    String databaseVersion = dataSnapshot.getValue(String.class);
                                    int databaseVersionInt = Integer.parseInt(databaseVersion);
                                    if(currentVersionInt > databaseVersionInt){
                                        currentVersionRef.setValue(currentVersionString);
                                        Intent intent = new Intent(getContext(), ReleaseNotesActivity.class);
                                        isFirstKonfetti = true;
                                        konfetti();
                                        startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void showTutorialChoice(){

        final DatabaseReference firstTime = FirebaseDatabase.getInstance().getReference().child("firstTime")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isFeedFirstTime");
        firstTime.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // set title
                    builder.setTitle("Take tutorial?");

                    builder.setMessage("Would you like to take the tutorial? It's highly recommended!")
                            .setPositiveButton("Yes, take tutorial", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firstTime.setValue(null);
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.mainFragHolder, new TemplateMenuFrag());
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child
                                            ("firstTime").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    firstTimeRef.setValue(null);
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void firstTimeTutorial(){
        final DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child("firstTime")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isFeedFirstTime");
        firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    new FancyShowCaseView.Builder(getActivity())
                            .title("This is the Social Feed. It's where you and your friends' completed workouts will " +
                                    "be posted.")
                            .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                            .build()
                            .show();
                    firstTimeRef.setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpFirebaseAdapter(DatabaseReference databaseReference, final boolean isImperial){

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(10);

        firebaseAdapter = new FirebaseRecyclerAdapter<CompletedWorkoutModelClass, CompletedWorkoutViewHolder>
                (CompletedWorkoutModelClass.class, R.layout.completed_workout_list_item,
                        CompletedWorkoutViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(CompletedWorkoutViewHolder viewHolder,
                                              CompletedWorkoutModelClass model, int position) {
                if(loadingView.getVisibility() == View.VISIBLE){
                    loadingView.setVisibility(View.GONE);
                }
                //if(model.getUserId().equals(uid)){
                    //viewHolder.setPosition(position);
                    viewHolder.setCurrentUserId(uid);
                    viewHolder.setImperialPOV(isImperial);
                    viewHolder.setActivity(getActivity());
                    viewHolder.setRefKey(model.getRef());
                    viewHolder.setUserId(model.getUserId());
                    viewHolder.setPostInfo(model.getWorkoutInfoMap(), getActivity(), getContext(),
                            model.isIsImperial());
                    viewHolder.setUpProfilePics(model.getUserId());
                    viewHolder.setCommentRecycler(model.getRef());
                    viewHolder.setUserName(model.getUserName());
                    viewHolder.setUserLevel(model.getUserId(), rootRef);
                    viewHolder.setPublicDescription(model.getPublicDescription());
                    viewHolder.setTimeStamp(model.getDateTime());
                    //viewHolder.setReppedCount(model.getRepCount());
                    viewHolder.setHasReppedList(model.getHasReppedList());
                    //viewHolder.setRepsCounterView(model.getRepCount());
                    //viewHolder.setIsRepped(model.isHasRepped(), false);
                    //viewHolder.setActivity(getActivity());
                    if(model.getBonusList() != null){
                        if(!model.getBonusList().isEmpty()){
                            viewHolder.setBonusView(model.getBonusList());
                        }
                    }
                //}else{
                //    viewHolder.hideLayout();
                //}

            }
        };

        //firebaseAdapter.setHasStableIds(true);
        recyclerView.setAdapter(firebaseAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(firebaseAdapter != null){
            firebaseAdapter.cleanup();
        }
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
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
