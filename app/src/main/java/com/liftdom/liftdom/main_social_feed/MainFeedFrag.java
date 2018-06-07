package com.liftdom.liftdom.main_social_feed;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.ReadableInstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFeedFrag extends Fragment implements RandomUsersBannerFrag.removeFragCallback{

    public MainFeedFrag() {
        // Required empty public constructor
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
    @BindView(R.id.noPostsHolder) LinearLayout noPostsLL;
    @BindView(R.id.goToWorkoutProgramming) Button goToWorkoutProgramming;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_social_feed, container, false);

        ButterKnife.bind(this, view);

        //HideKey.initialize(getActivity(), view);

        goToWorkoutProgramming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragID",  1);
                startActivity(intent);
            }
        });

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
                //checkForRandomUsersBanner();
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
                        noPostsLL.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //kablam();

        return view;
    }

    private void setUpFirebaseAdapter(DatabaseReference databaseReference, final boolean isImperial){

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(10);

        Query query = databaseReference.orderByChild("dateTime");

        FirebaseRecyclerOptions<CompletedWorkoutModelClass> options = new FirebaseRecyclerOptions
                .Builder<CompletedWorkoutModelClass>()
                .setQuery(query, CompletedWorkoutModelClass.class)
                .build();

        firebaseAdapter = new FirebaseRecyclerAdapter<CompletedWorkoutModelClass, CompletedWorkoutViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull CompletedWorkoutViewHolder holder, int position,
                                            @NonNull CompletedWorkoutModelClass model) {

                holder.setCurrentUserId(uid);
                holder.setImperialPOV(isImperial);
                holder.setActivity(getActivity());
                holder.setRefKey(model.getRef());
                holder.setUserId(model.getUserId());
                holder.setPostInfo(model.getWorkoutInfoMap(), getActivity(), getContext(),
                        model.isIsImperial());
                holder.setUpProfilePics(model.getUserId());
                holder.setCommentRecycler(model.getRef(), model.getWorkoutInfoMap());
                holder.setUserName(model.getUserName());
                holder.setUserLevel(model.getUserId(), rootRef);
                holder.setPublicDescription(model.getPublicDescription());
                holder.setTimeStamp(model.getDateTime());
                holder.setHasReppedList(model.getHasReppedList());
                //holder.setBonusView(model.getBonusList());
                //viewHolder.setReppedCount(model.getRepCount());
                //viewHolder.setRepsCounterView(model.getRepCount());
                //viewHolder.setIsRepped(model.isHasRepped(), false);
                //viewHolder.setActivity(getActivity());
                //try{
                //    viewHolder.mBonusView.setText(model.getBonusList().get(0));
                //}catch (NullPointerException e){
                //}
                //viewHolder.setBonusView(model.getBonusList());
                //}
                //}else{
                //    viewHolder.hideLayout();
                //}

            }

            @Override
            public CompletedWorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.completed_workout_list_item2,
                        parent, false);

                return new CompletedWorkoutViewHolder(view);
            }
        };

        //firebaseAdapter.setHasStableIds(true);
        loadingView.setVisibility(View.GONE);
        firebaseAdapter.startListening();
        recyclerView.setAdapter(firebaseAdapter);
    }

    private void kablam2(){
        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("userList");
        userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final long size1 = dataSnapshot.getChildrenCount();
                DatabaseReference userListRef2 = FirebaseDatabase.getInstance().getReference().child("user");
                userListRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long size2 = dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkCompletionStreak(){
        String date = LocalDate.now().toString();
        final DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("workoutHistory").child
                (uid);
        historyRef.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    String date2 = LocalDate.now().minusDays(2).toString();
                    historyRef.child(date2).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user")
                                        .child(uid).child("currentStreak");
                                userRef.setValue("1");
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

    private void setLoginDate(){
        String date = LocalDate.now().toString();
        DatabaseReference loginDateRef = FirebaseDatabase.getInstance().getReference().child("loginDate").child(uid)
                .child("date");
        loginDateRef.setValue(date);
    }

    private void kablam(){

        // removes all the incompatible workouts in self feed based on date
        final DatabaseReference selfRef = FirebaseDatabase.getInstance().getReference().child("selfFeed");
        selfRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String subUid = dataSnapshot1.getKey();
                    final DatabaseReference subRef = selfRef.child(subUid);
                    subRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {
                            for(DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()){
                                DateTime dateTime1 = DateTime.parse("2017-12-02");
                                CompletedWorkoutModelClass workoutModelClass = dataSnapshot3.getValue
                                        (CompletedWorkoutModelClass.class);
                                DateTime dateTime2 = DateTime.parse(workoutModelClass.getDateTime());
                                if(dateTime2.isBefore(dateTime1)){
                                    subRef.child(workoutModelClass.getRef()).setValue(null);
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


        // removes all the incompatible workouts in global feed based on date
        //final DatabaseReference globalRef = FirebaseDatabase.getInstance().getReference().child("globalFeed");
        //globalRef.addListenerForSingleValueEvent(new ValueEventListener() {
        //    @Override
        //    public void onDataChange(DataSnapshot dataSnapshot) {
        //        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
        //            DateTime dateTime1 = DateTime.parse("2017-12-02");
        //            CompletedWorkoutModelClass workoutModelClass = dataSnapshot1.getValue(CompletedWorkoutModelClass
        //                    .class);
        //            DateTime dateTime2 = DateTime.parse(workoutModelClass.getDateTime());
        //            if(dateTime2.isBefore(dateTime1)){
        //                globalRef.child(workoutModelClass.getRef()).setValue(null);
        //            }
        //        }
        //    }
//
        //    @Override
        //    public void onCancelled(DatabaseError databaseError) {
//
        //    }
        //});

        // current issue is that mExInfoHasMap is being set to null after saving a revised workout.
        //DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("runningAssistor")
        //        .child(uid).child("assistorModel").child("exInfoHashMap");
        //HashMap<String, HashMap<String, List<String>>> mExInfoHashMap = new HashMap<>();
        //List<String> list1 = new ArrayList<>();
        //List<String> list2 = new ArrayList<>();
        //list1.add("Deadlift (Barbell - Conventional)");
        //list1.add("4@25_checked");
        //list1.add("4@25_checked");
        //list1.add("4@25_unchecked");
        //list2.add("Bench Press (Barbell - Flat)");
        //list2.add("3@58_checked_ss");
        //list2.add("3@58_checked_ss");
        //list2.add("3@58_unchecked_ss");
        //list2.add("3@58_unchecked_ss");
        //HashMap<String, List<String>> map = new HashMap<>();
        //map.put("0_key", list1);
        //map.put("1_key", list2);
        //mExInfoHashMap.put("1_key", map);
        //myRef.setValue(mExInfoHashMap);

        // updates <1.29 posts to the global feed
        //DatabaseReference selfFeedRef = FirebaseDatabase.getInstance().getReference().child("selfFeed");
        //selfFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
        //    @Override
        //    public void onDataChange(DataSnapshot dataSnapshot) {
        //        Map fanoutObject = new HashMap<>();
        //        int index1 = 0;
        //        int index2 = 0;
        //        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
        //            index1++;
        //            index2 = 0;
        //            for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
        //                index2++;
        //                fanoutObject.put("/globalFeed/" + dataSnapshot2.getKey(), dataSnapshot2.getValue());
        //                if(index1 == dataSnapshot.getChildrenCount()){
        //                    if(index2 == dataSnapshot1.getChildrenCount()){
        //                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //                        rootRef.updateChildren(fanoutObject);
        //                    }
        //                }
        //            }
        //        }
        //    }
//
        //    @Override
        //    public void onCancelled(DatabaseError databaseError) {
//
        //    }
        //});

        //DatabaseReference burkRef = FirebaseDatabase.getInstance().getReference().child("templates").child
        //        ("EcCB9ayXcegCctEaT1Y7n98NC5G2").child("MindPrimary");
        //burkRef.addListenerForSingleValueEvent(new ValueEventListener() {
        //    @Override
        //    public void onDataChange(DataSnapshot dataSnapshot) {
        //        TemplateModelClass templateModelClass = dataSnapshot.getValue(TemplateModelClass.class);
        //        templateModelClass.setUserName2("Brodin");
        //        templateModelClass.setUserId2(uid);
        //        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("templatesInbox").child
        //                (uid).child("MindPrimary");
        //        myRef.setValue(templateModelClass);
        //    }
//
        //    @Override
        //    public void onCancelled(DatabaseError databaseError) {
//
        //    }
        //});

        //firebaseAdapter = new FirebaseRecyclerAdapter<CompletedWorkoutModelClass, CompletedWorkoutViewHolder>
        //        (CompletedWorkoutModelClass.class, R.layout.completed_workout_list_item2,
        //                CompletedWorkoutViewHolder.class, databaseReference) {
        //    @Override
        //    protected void populateViewHolder(CompletedWorkoutViewHolder viewHolder,
        //                                      CompletedWorkoutModelClass model, int position) {
        //        //if(model.getUserId().equals(uid)){
        //        //viewHolder.setPosition(position);
//
        //        /**
        //         * Going to try the new FirebaseUI version. Or at least see if we're on the old version.
        //         */
//
        //        viewHolder.setCurrentUserId(uid);
        //        viewHolder.setImperialPOV(isImperial);
        //        viewHolder.setActivity(getActivity());
        //        viewHolder.setRefKey(model.getRef());
        //        viewHolder.setUserId(model.getUserId());
        //        viewHolder.setPostInfo(model.getWorkoutInfoMap(), getActivity(), getContext(),
        //                model.isIsImperial());
        //        viewHolder.setUpProfilePics(model.getUserId());
        //        viewHolder.setCommentRecycler(model.getRef());
        //        viewHolder.setUserName(model.getUserName());
        //        viewHolder.setUserLevel(model.getUserId(), rootRef);
        //        viewHolder.setPublicDescription(model.getPublicDescription());
        //        viewHolder.setTimeStamp(model.getDateTime());
        //        //viewHolder.setReppedCount(model.getRepCount());
        //        viewHolder.setHasReppedList(model.getHasReppedList());
        //        //viewHolder.setRepsCounterView(model.getRepCount());
        //        //viewHolder.setIsRepped(model.isHasRepped(), false);
        //        //viewHolder.setActivity(getActivity());
        //        //if(model.getBonusList() != null){
        //        try{
        //            viewHolder.mBonusView.setText(model.getBonusList().get(0));
        //        }catch (NullPointerException e){
//
        //        }
//
        //        //viewHolder.setBonusView(model.getBonusList());
        //        //}
        //        //}else{
        //        //    viewHolder.hideLayout();
        //        //}
//
//
        //    }
        //};

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
                            try{
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                intent.putExtra("fragID",  0);
                                startActivity(intent);
                            }catch (NullPointerException e1){

                            }
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
                        final int currentVersionInt = 138;
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
                                    checkCompletionStreak();
                                    setLoginDate();
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
                                        checkCompletionStreak();
                                        setLoginDate();
                                    }else{
                                        MainActivitySingleton.getInstance().isReleaseCheck = true;
                                        checkCompletionStreak();
                                        setLoginDate();
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

    @Override
    public void onStart(){
        super.onStart();
        if(firebaseAdapter != null && firebaseAdapter.getItemCount() == 0){
            firebaseAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(firebaseAdapter != null){
            firebaseAdapter.stopListening();
        }
    }

}
