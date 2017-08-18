package com.liftdom.workout_assistor;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistorHolderFrag extends android.app.Fragment
                implements ExNameWAFrag.removeFragCallback{


    public AssistorHolderFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TemplateModelClass templateClass;
    ArrayList<ExNameWAFrag> exNameFragList = new ArrayList<>();
    int exNameInc = 0;
    boolean savedState = false;
    WorkoutProgressModelClass modelClass;


    @BindView(R.id.addExerciseButton) Button addExButton;
    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.saveHolder) LinearLayout saveHolder;
    @BindView(R.id.saveProgressButton) Button saveProgressButton;
    @BindView(R.id.privateJournal) EditText privateJournalView;
    @BindView(R.id.publicComment) EditText publicCommentView;
    @BindView(R.id.saveImage) ImageButton saveImage;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistor_holder, container, false);

        ButterKnife.bind(this, view);

        DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child
                ("assistorModel");
        runningAssistorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.now();
                    String dateTimeString = fmt.print(localDate);
                    modelClass = dataSnapshot.getValue(WorkoutProgressModelClass.class);
                    if(dateTimeString.equals(modelClass.getDate())){
                        if(!modelClass.isCompletedBool()){
                            trueProgressInflateViews(modelClass.getExInfoHashMap(), modelClass.getPrivateJournal(),
                                    modelClass.getPublicComment());
                            //noProgressInflateViews();
                        }else{
                            noProgressInflateViews();
                        }
                    }else{
                        noProgressInflateViews();
                    }
                }else{
                    noProgressInflateViews();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        addExButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exNameInc++;
                String tag = String.valueOf(exNameInc) + "ex";
                android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                ExNameWAFrag exNameFrag = new ExNameWAFrag();
                exNameFrag.fragTag = tag;
                if (!getActivity().isFinishing()) {
                    fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                    fragmentTransaction.commitAllowingStateLoss();
                    getChildFragmentManager().executePendingTransactions();
                    exNameFragList.add(exNameFrag);
                }
            }
        });

        saveHolder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<String> exInfo = new ArrayList<>();
                for(ExNameWAFrag exNameFrag : exNameFragList){
                    exInfo.addAll(exNameFrag.getExInfo());
                }
                AssistorSingleton.getInstance().endList.clear();
                AssistorSingleton.getInstance().endList.addAll(exInfo);
                Intent intent = new Intent(getActivity(), SaveAssistorDialog.class);
                startActivityForResult(intent, 1);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(exNameFragList.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // set title
                    builder.setTitle("Error");

                    // set dialog message
                    builder.setMessage("At least one exercise must be present to complete workout")
                            .setCancelable(false)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    dialog.dismiss();

                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = builder.create();

                    // show it
                    alertDialog.show();
                }else{
                    List<String> exInfo = new ArrayList<>();
                    for(ExNameWAFrag exNameFrag : exNameFragList){
                        exInfo.addAll(exNameFrag.getExInfo());
                    }
                    AssistorSingleton.getInstance().endList.clear();
                    AssistorSingleton.getInstance().endList.addAll(exInfo);
                    Intent intent = new Intent(getActivity(), SaveAssistorDialog.class);
                    intent.putExtra("isRestDay", "no");
                    startActivityForResult(intent, 1);
                }
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<String> exInfo = new ArrayList<>();
                for(ExNameWAFrag exNameFrag : exNameFragList){
                    exInfo.addAll(exNameFrag.getExInfo());
                }
                AssistorSingleton.getInstance().endList.clear();
                AssistorSingleton.getInstance().endList.addAll(exInfo);
                Intent intent = new Intent(getActivity(), SaveAssistorDialog.class);
                startActivityForResult(intent, 1);
            }
        });

        saveProgressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child
                        ("assistorModel");
                HashMap<String, HashMap<String, List<String>>> runningMap = new HashMap<>();
                int inc = 0;
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.now();
                String dateTimeString = fmt.print(localDate);
                String privateJournal = privateJournalView.getText().toString();
                String publicComment = publicCommentView.getText().toString();
                boolean completedBool = false; // obviously this will be set to true in assistor saved
                String mediaResource = "";


                // might need to make this not clickable without inflated views so it isn't set to null
                for(ExNameWAFrag exNameFrag : exNameFragList){
                    inc++;
                    runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
                }

                WorkoutProgressModelClass progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                        completedBool, runningMap, privateJournal, publicComment, mediaResource);

                runningAssistorRef.setValue(progressModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        try{
                            Snackbar snackbar = Snackbar.make(getView(), "Progress Saved", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        } catch (NullPointerException e){

                        }
                    }
                });

            }
        });

        return view;
    }

    private void trueProgressInflateViews(HashMap<String, HashMap<String, List<String>>> runningMap, String
            privateJournal, String
            publicComment){

        //TODO: On change of active template, delete this node so we don't accidentally get info from old template
        for(int i = 0; i < runningMap.size(); i ++){
            for(Map.Entry<String, HashMap<String, List<String>>> entry : runningMap.entrySet()) {
                if(isOfIndex(i, entry.getKey())){
                    exNameInc++;
                    String tag = String.valueOf(exNameInc) + "ex";
                    HashMap<String, List<String>> exerciseMap = entry.getValue();
                    android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    ExNameWAFrag exNameFrag = new ExNameWAFrag();
                    exNameFrag.isEditInfoList = exerciseMap;
                    exNameFrag.fragTag = tag;
                    exNameFrag.isEdit = true;
                    if (!getActivity().isFinishing()) {
                        fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                        fragmentTransaction.commitAllowingStateLoss();
                        getChildFragmentManager().executePendingTransactions();
                        exNameFragList.add(exNameFrag);
                    }
                }
            }
        }

        privateJournalView.setText(privateJournal);
        publicCommentView.setText(publicComment);

    }

    private void noProgressInflateViews(){

        DateTime dateTime = new DateTime();
        int currentWeekday = dateTime.getDayOfWeek();
        if(templateClass.getMapForDay(intToWeekday(currentWeekday)) != null){
            if(!templateClass.getMapForDay(intToWeekday(currentWeekday)).isEmpty()){
                HashMap<String, List<String>> map = templateClass.getMapForDay(intToWeekday(currentWeekday));
                for(int i = 0; i < map.size(); i++){
                    for(Map.Entry<String, List<String>> entry : map.entrySet()) {
                        if(!entry.getKey().equals("0_key")){
                            if(isOfIndex(i, entry.getKey())){
                                exNameInc++;
                                String tag = String.valueOf(exNameInc) + "ex";
                                List<String> stringList = entry.getValue();
                                android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                                ExNameWAFrag exNameFrag = new ExNameWAFrag();
                                exNameFrag.infoList = stringList;
                                exNameFrag.fragTag = tag;
                                if (!getActivity().isFinishing()) {
                                    fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                                    fragmentTransaction.commitAllowingStateLoss();
                                    getChildFragmentManager().executePendingTransactions();
                                    exNameFragList.add(exNameFrag);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isOfIndex(int index, String key){
        String delims = "[_]";
        String tokens[] = key.split(delims);
        int index2 = Integer.valueOf(tokens[0]);
        if(index + 1 == index2){
            return true;
        }else{
            return false;
        }
    }

    private HashMap<String, List<String>> reorderMap(HashMap<String, List<String>> map){
        HashMap<String, List<String>> orderedMap = new HashMap<>();

        String dummyString = "dummy";
        List<String> dummyList = new ArrayList<>();
        dummyList.add(dummyString);

        int mapSize = map.size();
        for(int i = 0; i < mapSize; i++){
            orderedMap.put(dummyString, dummyList);
        }

        for(Map.Entry<String, List<String>> subMap : map.entrySet()){
            String delims = "[_]";
            //int index = Integer.valueOf(subMap.getValue().get(0));
            String tokens[] = subMap.getValue().get(0).split(delims);
            int index = Integer.valueOf(tokens[0]);

        }

        return orderedMap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            HashMap<String, HashMap<String, List<String>>> runningMap = new HashMap<>();
            int inc = 0;
            for(ExNameWAFrag exNameFrag : exNameFragList){
                inc++;
                runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
            }

            String privateJournal = privateJournalView.getText().toString();
            String publicComment = publicCommentView.getText().toString();

            android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
            android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AssistorSavedFrag assistorSavedFrag = new AssistorSavedFrag();
            assistorSavedFrag.templateClass = templateClass;
            assistorSavedFrag.completedMap = runningMap;
            assistorSavedFrag.privateJournal = privateJournal;
            assistorSavedFrag.publicDescription = publicComment;
            fragmentTransaction.replace(R.id.exInfoHolder, assistorSavedFrag);
            fragmentTransaction.commit();
        }
    }

    public void removeFrag(String tag){
        getChildFragmentManager().executePendingTransactions();
        android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if(exNameInc != 0){
            if(getChildFragmentManager().findFragmentByTag(tag) != null){
                fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                int inc = 0;
                int index = 0;
                boolean hasIndex = false;
                for(ExNameWAFrag exNameWAFrag : exNameFragList){
                    if(exNameWAFrag.fragTag.equals(tag)){
                        index = inc;
                        hasIndex = true;
                    }
                    inc++;
                }
                if(hasIndex){
                    exNameFragList.remove(index);
                }
                --exNameInc;
            }
        }
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

    String intToWeekday(int inc){
        String weekday = "";

        if(inc == 1){
            weekday = "Monday";
        }else if(inc == 2){
            weekday = "Tuesday";
        }else if(inc == 3){
            weekday = "Wednesday";
        }else if(inc == 4){
            weekday = "Thursday";
        }else if(inc == 5){
            weekday = "Friday";
        }else if(inc == 6){
            weekday = "Saturday";
        }else if(inc == 7){
            weekday = "Sunday";
        }

        return weekday;
    }

    boolean containsToday(String dayUnformatted, int inc){
        boolean contains = false;
        String[] tokens = dayUnformatted.split("_");

        try{
            for(String string : tokens){
                if(string.equals(intToWeekday(inc))){
                    contains = true;
                }
            }
        } catch (IndexOutOfBoundsException e){

        }

        return contains;
    }

}
