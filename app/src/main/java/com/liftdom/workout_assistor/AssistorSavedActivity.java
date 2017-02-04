package com.liftdom.workout_assistor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.SignInActivity;
import com.liftdom.template_editor.TemplateSavedActivity;
import com.liftdom.template_housing.TemplateHousingActivity;
import com.liftdom.user_profile.CurrentUserProfile;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class AssistorSavedActivity extends AppCompatActivity {

    @BindView(R.id.goBackHome) Button goHome;


    private static final String FIREBASE_URL = "https://liftdom-27d9d.firebaseio.com/";

    private static final String TAG = "EmailPassword";
    // declare_auth
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    // declare_auth
    private FirebaseUser mFirebaseUser;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ArrayList<String> daySpecificArrayList = new ArrayList<String>();

    boolean isFirstupload = true;
    boolean isFirstupload2 = true;

    String dayKey = "fail";

    // tells us which exercises we want to increment
    ArrayList<String> algoExercisesAL = new ArrayList<>();

    // tells us the data to apply to successful exercises
    ArrayList<String> algoInfoAL = new ArrayList<>();
    int[] algoInfoArray = new int[7];

    // The
    ArrayList<String> runningALOriginal = new ArrayList<>();
    ArrayList<String> runningALCompleted = new ArrayList<>();

    ArrayList<String> originalAL = new ArrayList<>();
    ArrayList<String> firstSetsRepsAL = new ArrayList<>();

    DatabaseReference daySpecificRef;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistor_saved);

        ButterKnife.bind(this);


        // [START AUTH AND NAV-DRAWER BOILERPLATE]

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
        }

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(AssistorSavedActivity.this, SignInActivity.class));
                }

            }
        };
        // [END auth_state_listener]


        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Saved Templates");

        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //Handle Profile changes
                        return false;
                    }
                }).withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Intent intent = new Intent(AssistorSavedActivity.this, CurrentUserProfile.class);
                        startActivity(intent);
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        // create the drawer
        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIdentifier(1),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Workout Templating").withIdentifier(2),
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(3),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Tools").withIdentifier(4),
                        new SecondaryDrawerItem().withName("Exercise Academy (Info)").withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // Handle clicks

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(AssistorSavedActivity.this, MainActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(AssistorSavedActivity.this, TemplateHousingActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(AssistorSavedActivity.this, WorkoutAssistorActivity.class);
                            }
                            if (intent != null) {
                                AssistorSavedActivity.this.startActivity(intent);
                            }
                        }
                        return true;
                    }
                })
                .build();

        // Later
        header.addProfile(new ProfileDrawerItem().withIcon(ContextCompat.getDrawable(this, R.drawable.usertest))
                        .withName
                                (mFirebaseUser.getDisplayName()).withEmail(email),
                0);

        // [END AUTH AND NAV-DRAWER BOILERPLATE] =================================================================




        // HERE WE'RE GETTING THE ORIGINAL VALUES FROM THE TEMPLATE NODE
        final DatabaseReference algorithmRef = mRootRef.child("templates").child(uid).child(WorkoutAssistorAssemblerClass
                .getInstance().templateName);
        algorithmRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    if(dataSnapshot1.getKey().equals("algorithm")){
                        DatabaseReference algorithmInfoRef = algorithmRef.child("algorithm");
                        algorithmInfoRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int index = 0;
                                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                    if(!dataSnapshot2.getValue(String.class).equals("loop")){
                                        if(dataSnapshot2.getValue(String.class).equals("")){
                                            algoInfoArray[index] = 0;
                                            ++index;
                                        }else{
                                            int value = Integer.parseInt(dataSnapshot2.getValue(String.class));
                                            algoInfoArray[index] = value;
                                            ++index;
                                        }
                                    }else if(dataSnapshot2.getValue(String.class).equals("loop")){
                                        algoInfoArray[6] = 1;
                                    }else if(dataSnapshot2.getValue(String.class).equals("no loop")){
                                        algoInfoArray[6] = 0;
                                    }
                                    //algoInfoAL.add(value);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else if(dataSnapshot1.getKey().equals("algorithmExercises")){
                        DatabaseReference algorithmInfoRef = algorithmRef.child("algorithmExercises");
                        algorithmInfoRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                    String value = dataSnapshot2.getValue(String.class);
                                    algoExercisesAL.add(value);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // HERE WE'RE COMPARING THE ORIGINAL VALUES TO THE COMPLETED/NEW VALUES
        final DatabaseReference activeTemplateDataRef = mRootRef.child("templates").child(uid)
                .child(WorkoutAssistorAssemblerClass.getInstance().templateName);
        activeTemplateDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    String key = dataSnapshot1.getKey();

                    if(isToday(key)){
                        DatabaseReference dayRef = activeTemplateDataRef.child(key);

                        dayRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                ArrayList<String> assistorArrayList = WorkoutAssistorAssemblerClass.getInstance().DoWAL1;

                                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                                    String snapshotString = dataSnapshot2.getValue(String.class);
                                    if(isExerciseName(snapshotString)){
                                        originalAL.add(snapshotString);
                                        //firstSetsRepsAL.set(0, snapshotString);
                                    }else{
                                        String stringSansSpaces = snapshotString.replaceAll("\\s+", "");
                                        String delims = "[x,@]";
                                        String[] tokens = stringSansSpaces.split(delims);

                                        int setAmount = Integer.parseInt(tokens[0]);

                                        for(int i = 0; i < setAmount; i++){
                                            String cat = tokens[1] + "@" + tokens[2];
                                            //if(some shit){
                                            //    String cat2 = tokens[0] + "x" + tokens[1];
                                            //    firstSetsRepsAL.add(cat2);
                                            //}
                                            originalAL.add(cat);
                                        }
                                    }
                                }

                                //OK what we're going to do for the looper is create a full arraylist for the day
                                // we're in of the original pure sets/reps/weight data.
                                // then we'll look at that to get the original values.






                                ArrayList<String> exercisesToInc = exercisesCompleted(assistorArrayList, originalAL);
                                // here we'd perform the algo operations
                                // for each completed exercise
                                for(final String completedEx : exercisesToInc){

                                    /**
                                     * What are we trying to accomplish here?
                                     * We need to get the last completed date and compare it to today's date
                                     * Then we'll check that value against the values in the algo data array
                                     * Finally, we'll increment the values that pass the test and update the
                                     * template's SxRxW data.
                                     */

                                    // runningAlgorithm->uid->templateName->Exercise->TimeStamp

                                    final DatabaseReference runningAlgoRef = mRootRef.child("runningAlgorithm")
                                            .child(uid)
                                            .child(WorkoutAssistorAssemblerClass.getInstance().templateName)
                                            .child(completedEx).child("timeStamp");

                                    //DatabaseReference runningAlgoTimeStampRef = runningAlgoRef.child("timeStamp");

                                    //runningAlgoTimeStampRef.setValue(formattedDate);


                                    runningAlgoRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String oldTimeStamp = dataSnapshot.getValue(String.class);
                                            if(oldTimeStamp != null){
                                                //TODO: here we'll compare the last completed date to today's date
                                                LocalDate oldDate = LocalDate.parse(oldTimeStamp);
                                                LocalDate newDate = LocalDate.now();

                                                int daysBetween = Days.daysBetween(oldDate, newDate).getDays();
                                                final int weeksBetween = (int) Math.round(daysBetween / 7);


                                                final DatabaseReference templateRef = mRootRef.child("templates")
                                                        .child(uid)
                                                        .child(WorkoutAssistorAssemblerClass.getInstance()
                                                                .templateName);

                                                templateRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                                            String dayKey = dataSnapshot2.getKey();
                                                            if(isToday(dayKey)){
                                                                daySpecificRef = templateRef.child
                                                                        (dayKey);

                                                                daySpecificRef.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        for(DataSnapshot dataSnapshot3 : dataSnapshot
                                                                                .getChildren()){
                                                                            String value = dataSnapshot3.getValue
                                                                                    (String.class);
                                                                            daySpecificArrayList.add(value);
                                                                        }
                                                                        int arrayListLength = daySpecificArrayList.size();
                                                                        int exIndex = daySpecificArrayList.indexOf(completedEx);

                                                                        for(int i = exIndex + 1; i < arrayListLength;
                                                                            i++){
                                                                            if(!isExerciseName(daySpecificArrayList.get(i))){
                                                                                int sets = 0;
                                                                                int reps = 0;
                                                                                int weight = 0;

                                                                                String delims = "[x,@]";
                                                                                String noSpaces = daySpecificArrayList.get(i).replace(" ", "");
                                                                                String[] cutArray = noSpaces.split(delims);

                                                                                if(weeksBetween >= algoInfoArray[0]){
                                                                                    sets = Integer.parseInt(cutArray[0]);
                                                                                    sets += algoInfoArray[1];
                                                                                } else{
                                                                                    sets = Integer.parseInt(cutArray[0]);
                                                                                }
                                                                                if(weeksBetween >= algoInfoArray[2]){
                                                                                    reps = Integer.parseInt(cutArray[1]);
                                                                                    reps += algoInfoArray[3];
                                                                                } else{
                                                                                    reps = Integer.parseInt(cutArray[1]);
                                                                                }
                                                                                if(weeksBetween >= algoInfoArray[4]){
                                                                                    weight = Integer.parseInt(cutArray[2]);
                                                                                    weight += algoInfoArray[5];
                                                                                    if(algoInfoArray[6] == 1){
                                                                                        // what if we just subtracted the
                                                                                        // algo shit from the weeks
                                                                                        // between?...
                                                                                        for(int j = 0; j < weeksBetween;
                                                                                            j++){
                                                                                            sets = sets - algoInfoArray[1];
                                                                                            reps = reps - algoInfoArray[3];
                                                                                        }
                                                                                    }
                                                                                }else{
                                                                                    weight = Integer.parseInt(cutArray[2]);
                                                                                }



                                                                                String concat = Integer.toString
                                                                                        (sets) + " x " + Integer.toString(reps)
                                                                                        + " @ " + Integer.toString
                                                                                        (weight);

                                                                                daySpecificArrayList.set(i, concat);
                                                                            }else{
                                                                                break;
                                                                            }
                                                                        }

                                                                        if(isFirstupload){
                                                                            daySpecificUploader(daySpecificArrayList);
                                                                            isFirstupload = false;
                                                                        }

                                                                        String date = LocalDate.now().toString();
                                                                        runningAlgoRef.setValue(date);
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                    ArrayList<String> exerciseSpecificArrayList = new ArrayList<String>();


                                                });


                                            }else{
                                                String date = LocalDate.now().toString();
                                                runningAlgoRef.setValue(date);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                                //daySpecificRef.setValue(daySpecificArrayList);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                //Calendar c = Calendar.getInstance();
                //SimpleDateFormat df = new SimpleDateFormat("MM:dd:yyyy");
                //final String formattedDate = df.format(c.getTime());

                LocalDate d1 = LocalDate.now();
                String date = LocalDate.now().toString();

                DatabaseReference workoutHistoryRef = mRootRef.child("workout_history").child(uid);
                DatabaseReference specificDate = workoutHistoryRef.child(date);
                DatabaseReference journalRef = specificDate.child("private_journal");
                String privateJournalString = WorkoutAssistorAssemblerClass.getInstance().privateJournal;
                ArrayList<String> assistorArrayList = WorkoutAssistorAssemblerClass.getInstance().DoWAL1;


                List<String> list = new ArrayList<>();

                if(savedInstanceState == null) {
                    for (String item : assistorArrayList) {
                        list.add(item);
                    }

                    specificDate.setValue(list);

                    if (privateJournalString != null) {
                        journalRef.setValue(privateJournalString);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }

    void templateAlgorithmSetter(String exName){

    }

    void daySpecificUploader(final ArrayList<String> arrayList){
        final DatabaseReference templateRef = mRootRef.child("templates")
                .child(uid)
                .child(WorkoutAssistorAssemblerClass.getInstance()
                        .templateName);


        templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String key = dataSnapshot1.getKey();
                    if(isToday(key)){
                        DatabaseReference daySpecificRef1 = templateRef.child(key);

                        List<String> list = new ArrayList<>();

                        for(String value : arrayList){
                            list.add(value);
                        }

                        //daySpecificRef1.setValue(null);

                        daySpecificRef1.setValue(list);

                        //isFirstupload2 = false;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ArrayList<String> exercisesCompleted(ArrayList<String> completedArrayList, ArrayList<String> originalAL){
        ArrayList<String> completedExercises = new ArrayList<>();

        // first we should get the original exercises to compare with...
        for(String exName : originalAL){
            if(isExerciseName(exName)){
                int index = originalAL.indexOf(exName);
                runningALOriginal.add(Integer.toString(index));
                runningALOriginal.add(exName);
            }
        }

        // get all the completed exercises to compare with...
        for(String completedEx : completedArrayList){
            if(isExerciseName(completedEx)){
                runningALCompleted.add(completedEx);
            }
        }

        // next we should compare each exercise in the original array list with each exercise in the completed list
        for(String completedEx : runningALCompleted){
            boolean isTotallyComplete = false;
            if(runningALOriginal.contains(completedEx)){
                int nextIndex = 0;
                int originalIndex = 0;

                try{
                    nextIndex = Integer.parseInt(runningALOriginal.get(runningALOriginal.indexOf(completedEx) + 1));
                } catch (IndexOutOfBoundsException e){
                    nextIndex = originalAL.size();
                }

                originalIndex = Integer.parseInt(runningALOriginal.get(runningALOriginal.indexOf(completedEx) - 1));

                //try{
                //    originalIndex = Integer.parseInt(runningALOriginal.get(runningALOriginal.indexOf(completedEx) -
                //            1));
                //} catch (IndexOutOfBoundsException e){
                //
                //}

                //if(runningALOriginal.indexOf(completedEx) - 1 > 0){
                //    originalIndex = runningALOriginal.indexOf(completedEx) - 1;
                //}

                for(int i = 1; i < nextIndex - originalIndex; i++){
                    if (i == 0) {
                        i++;
                    }
                    try{
                        if(completedArrayList.get(completedArrayList.indexOf(completedEx) + i).equals(
                                originalAL.get(originalAL.indexOf(completedEx) + i)
                        )){
                            isTotallyComplete = true;
                        }else{
                            isTotallyComplete = false;
                            //TODO check to see if it's greater than
                        }
                    } catch (IndexOutOfBoundsException e){

                    }

                }

                if(isTotallyComplete){
                    completedExercises.add(completedEx);
                }
            }
        }

        return completedExercises;
    }

    boolean isToday(String dayString){
        boolean today = false;
        if(dayString != null) {
            if(!dayString.equals("algorithm") || !dayString.equals("algorithmExercises")){
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date d = new Date();
                String currentDoW = sdf.format(d);

                String delims = "[_]";
                String[] tokens = dayString.split(delims);

                for (String day : tokens) {
                    if (day.equals(currentDoW)) {
                        today = true;
                    }
                }
            }
        }
        return today;
    }

    boolean isExerciseName(String input){
        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;

    }

}
