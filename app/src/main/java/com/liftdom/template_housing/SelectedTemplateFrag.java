package com.liftdom.template_housing;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.SaveTemplateDialog;
import com.liftdom.template_editor.TemplateEditorActivity;
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.user_profile.UserModelClass;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedTemplateFrag extends Fragment {


    public SelectedTemplateFrag() {
        // Required empty public constructor
    }

    String templateName;
    boolean isFromPublicList;
    boolean isFromMyPublicList;
    String firebaseKey;

    @BindView(R.id.selectedTemplateTitle) TextView selectedTemplateNameView;
    @BindView(R.id.editThisTemplate) Button editTemplate;
    @BindView(R.id.deleteThisTemplate) Button deleteTemplate;
    @BindView(R.id.activeTemplateCheckbox) CheckBox setAsActiveTemplate;
    @BindView(R.id.templateListedView) LinearLayout templateListedViewHolder;
    @BindView(R.id.editPenSmall) ImageView editPenSmall;
    @BindView(R.id.descriptionView) TextView descriptionView;
    @BindView(R.id.shareThisTemplate) Button shareTemplate;
    @BindView(R.id.authorNameView) TextView authorNameView;
    @BindView(R.id.choicesBar) LinearLayout choicesBar;
    @BindView(R.id.publishButton) Button publishButton;
    @BindView(R.id.saveTemplateButton) Button saveTemplateButton;

    int colorIncrement = 0;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ArrayList<HashMap<String, List<String>>> mapList = new ArrayList<>();
    HashMap<String, List<String>>[] sortedMapList = new HashMap[7];

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
        View view = inflater.inflate(R.layout.fragment_selected_template, container, false);

        ButterKnife.bind(this, view);

        navChanger(1);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        selectedTemplateNameView.setText(templateName);
        selectedTemplateNameView.setTypeface(lobster);

        if(savedInstanceState != null){
            templateName = savedInstanceState.getString("template_name");
            selectedTemplateNameView.setText(templateName);
            selectedTemplateNameView.setTypeface(lobster);
        }

        editPenSmall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!isFromPublicList){
                    Intent intent = new Intent(v.getContext(), RenameTemplateDialog.class);
                    intent.putExtra("templateName", templateName);
                    startActivityForResult(intent, 1);
                }

            }
        });

        saveTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SaveTemplateDialog.class);
                intent.putExtra("isFromPublicSelected", "yes");
                intent.putExtra("templateName", templateName);
                startActivityForResult(intent, 3);
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // set title
                builder.setTitle("Publish Template?");

                // set dialog message
                builder
                        .setMessage("This will create a new Public Template. Access your public templates via the " +
                                "Public Templates page.")
                        .setCancelable(false)
                        .setPositiveButton("Publish Template",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                final DatabaseReference specificTemplateRef = mRootRef.child("templates").child(uid).child(templateName);
                                specificTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        TemplateModelClass templateModelClass = dataSnapshot.getValue
                                                (TemplateModelClass.class);
                                        DatabaseReference publicTemplateRef = mRootRef.child("public_templates")
                                                .child("public").push();
                                        String keyId = publicTemplateRef.getKey();
                                        publicTemplateRef.setValue(templateModelClass);
                                        templateModelClass.setPublicTemplateKeyId(keyId);
                                        DatabaseReference myPublicTemplateRef = mRootRef.child("public_templates")
                                                .child("my_public").child(uid).child(templateName);
                                        myPublicTemplateRef.setValue(templateModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                try{
                                                    Snackbar snackbar = Snackbar.make(getView(), "Template Published", Snackbar.LENGTH_SHORT);
                                                    snackbar.show();
                                                } catch (NullPointerException e){

                                                }
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = builder.create();

                // show it
                alertDialog.show();
            }
        });

        if(savedInstanceState == null){
            if(isFromPublicList){

                choicesBar.setVisibility(View.GONE);
                publishButton.setVisibility(View.GONE);
                saveTemplateButton.setVisibility(View.VISIBLE);

                final DatabaseReference specificTemplateRef = mRootRef.child("public_templates").child
                        ("public").child(firebaseKey);
                specificTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        TemplateModelClass templateClass = dataSnapshot.getValue(TemplateModelClass.class);
                        descriptionView.setText(templateClass.getDescription());
                        String cat = "Originally authored by: " + templateClass.getUserName();
                        authorNameView.setText(cat);

                        if(templateClass.getMapOne() != null){
                            mapList.add(templateClass.getMapOne());
                        }
                        if(templateClass.getMapTwo() != null){
                            mapList.add(templateClass.getMapTwo());
                        }
                        if(templateClass.getMapThree() != null){
                            mapList.add(templateClass.getMapThree());
                        }
                        if(templateClass.getMapFour() != null){
                            mapList.add(templateClass.getMapFour());
                        }
                        if(templateClass.getMapFive() != null){
                            mapList.add(templateClass.getMapFive());
                        }
                        if(templateClass.getMapSix() != null){
                            mapList.add(templateClass.getMapSix());
                        }
                        if(templateClass.getMapSeven() != null){
                            mapList.add(templateClass.getMapSeven());
                        }

                        for(HashMap<String, List<String>> map : mapList){
                            if(containsDay("Monday", map.get("0_key").get(0))){
                                sortedMapList[0] = map;
                            } else if(containsDay("Tuesday", map.get("0_key").get(0))){
                                sortedMapList[1] = map;
                            } else if(containsDay("Wednesday", map.get("0_key").get(0))){
                                sortedMapList[2] = map;
                            } else if(containsDay("Thursday", map.get("0_key").get(0))){
                                sortedMapList[3] = map;
                            } else if(containsDay("Friday", map.get("0_key").get(0))){
                                sortedMapList[4] = map;
                            } else if(containsDay("Saturday", map.get("0_key").get(0))){
                                sortedMapList[5] = map;
                            } else if(containsDay("Sunday", map.get("0_key").get(0))){
                                sortedMapList[6] = map;
                            }
                        }

                        for(HashMap<String, List<String>> map : sortedMapList){
                            if(map != null){
                                for(Map.Entry<String, List<String>> entry : map.entrySet()){
                                    if(entry.getKey().equals("0_key")) {
                                        // add day of week frag
                                        FragmentManager fragmentManager = getChildFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager
                                                .beginTransaction();
                                        HousingDoWFrag housingDoWFrag = new HousingDoWFrag();
                                        housingDoWFrag.dOWString = entry.getValue().get(0);
                                        housingDoWFrag.templateName = templateName;
                                        housingDoWFrag.map = map;
                                        fragmentTransaction.add(R.id.templateListedView,
                                                housingDoWFrag);
                                        if (!getActivity().isFinishing()) {
                                            fragmentTransaction.commitAllowingStateLoss();
                                        }
                                    }
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else if(isFromMyPublicList){
                publishButton.setVisibility(View.GONE);

                final DatabaseReference specificTemplateRef = mRootRef.child("public_templates").child
                        ("my_public").child(uid).child(templateName);
                specificTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        TemplateModelClass templateClass = dataSnapshot.getValue(TemplateModelClass.class);
                        descriptionView.setText(templateClass.getDescription());
                        String cat = "Originally authored by: " + templateClass.getUserName();
                        authorNameView.setText(cat);

                        if(templateClass.getMapOne() != null){
                            mapList.add(templateClass.getMapOne());
                        }
                        if(templateClass.getMapTwo() != null){
                            mapList.add(templateClass.getMapTwo());
                        }
                        if(templateClass.getMapThree() != null){
                            mapList.add(templateClass.getMapThree());
                        }
                        if(templateClass.getMapFour() != null){
                            mapList.add(templateClass.getMapFour());
                        }
                        if(templateClass.getMapFive() != null){
                            mapList.add(templateClass.getMapFive());
                        }
                        if(templateClass.getMapSix() != null){
                            mapList.add(templateClass.getMapSix());
                        }
                        if(templateClass.getMapSeven() != null){
                            mapList.add(templateClass.getMapSeven());
                        }

                        for(HashMap<String, List<String>> map : mapList){
                            if(containsDay("Monday", map.get("0_key").get(0))){
                                sortedMapList[0] = map;
                            } else if(containsDay("Tuesday", map.get("0_key").get(0))){
                                sortedMapList[1] = map;
                            } else if(containsDay("Wednesday", map.get("0_key").get(0))){
                                sortedMapList[2] = map;
                            } else if(containsDay("Thursday", map.get("0_key").get(0))){
                                sortedMapList[3] = map;
                            } else if(containsDay("Friday", map.get("0_key").get(0))){
                                sortedMapList[4] = map;
                            } else if(containsDay("Saturday", map.get("0_key").get(0))){
                                sortedMapList[5] = map;
                            } else if(containsDay("Sunday", map.get("0_key").get(0))){
                                sortedMapList[6] = map;
                            }
                        }

                        for(HashMap<String, List<String>> map : sortedMapList){
                            if(map != null){
                                for(Map.Entry<String, List<String>> entry : map.entrySet()){
                                    if(entry.getKey().equals("0_key")) {
                                        // add day of week frag
                                        FragmentManager fragmentManager = getChildFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager
                                                .beginTransaction();
                                        HousingDoWFrag housingDoWFrag = new HousingDoWFrag();
                                        housingDoWFrag.dOWString = entry.getValue().get(0);
                                        housingDoWFrag.templateName = templateName;
                                        housingDoWFrag.map = map;
                                        fragmentTransaction.add(R.id.templateListedView,
                                                housingDoWFrag);
                                        if (!getActivity().isFinishing()) {
                                            fragmentTransaction.commitAllowingStateLoss();
                                        }
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
            else{
                final DatabaseReference specificTemplateRef = mRootRef.child("templates").child(uid).child(templateName);
                specificTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        TemplateModelClass templateClass = dataSnapshot.getValue(TemplateModelClass.class);
                        descriptionView.setText(templateClass.getDescription());
                        String cat = "Originally authored by: " + templateClass.getUserName();
                        authorNameView.setText(cat);

                        if(templateClass.getMapOne() != null){
                            mapList.add(templateClass.getMapOne());
                        }
                        if(templateClass.getMapTwo() != null){
                            mapList.add(templateClass.getMapTwo());
                        }
                        if(templateClass.getMapThree() != null){
                            mapList.add(templateClass.getMapThree());
                        }
                        if(templateClass.getMapFour() != null){
                            mapList.add(templateClass.getMapFour());
                        }
                        if(templateClass.getMapFive() != null){
                            mapList.add(templateClass.getMapFive());
                        }
                        if(templateClass.getMapSix() != null){
                            mapList.add(templateClass.getMapSix());
                        }
                        if(templateClass.getMapSeven() != null){
                            mapList.add(templateClass.getMapSeven());
                        }

                        for(HashMap<String, List<String>> map : mapList){
                            if(containsDay("Monday", map.get("0_key").get(0))){
                                sortedMapList[0] = map;
                            } else if(containsDay("Tuesday", map.get("0_key").get(0))){
                                sortedMapList[1] = map;
                            } else if(containsDay("Wednesday", map.get("0_key").get(0))){
                                sortedMapList[2] = map;
                            } else if(containsDay("Thursday", map.get("0_key").get(0))){
                                sortedMapList[3] = map;
                            } else if(containsDay("Friday", map.get("0_key").get(0))){
                                sortedMapList[4] = map;
                            } else if(containsDay("Saturday", map.get("0_key").get(0))){
                                sortedMapList[5] = map;
                            } else if(containsDay("Sunday", map.get("0_key").get(0))){
                                sortedMapList[6] = map;
                            }
                        }

                        for(HashMap<String, List<String>> map : sortedMapList){
                            if(map != null){
                                for(Map.Entry<String, List<String>> entry : map.entrySet()){
                                    if(entry.getKey().equals("0_key")) {
                                        // add day of week frag
                                        FragmentManager fragmentManager = getChildFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager
                                                .beginTransaction();
                                        HousingDoWFrag housingDoWFrag = new HousingDoWFrag();
                                        housingDoWFrag.dOWString = entry.getValue().get(0);
                                        housingDoWFrag.templateName = templateName;
                                        housingDoWFrag.map = map;
                                        fragmentTransaction.add(R.id.templateListedView,
                                                housingDoWFrag);
                                        if (!getActivity().isFinishing()) {
                                            fragmentTransaction.commitAllowingStateLoss();
                                        }
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
        }


        editTemplate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                String isEdit = "yes";
                Intent intent = new Intent(v.getContext(), TemplateEditorActivity.class);
                intent.putExtra("isEdit", isEdit );
                intent.putExtra("templateName", templateName);
                if(isFromMyPublicList){
                    intent.putExtra("isFromPublic", "yes");
                }
                startActivity(intent);
            }
        });

        /**
         * What we need to do:
         * Change the keys of algorithmInfo
         * Add a boolean isApplyToAll to template model class
         * have superset exercises inherit parent exercise algorithm
         * read all this into singleton
         */

        deleteTemplate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());

                // set title
                alertDialogBuilder.setTitle("Delete template:");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to delete this template?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity

                                final DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid);
                                runningAssistorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            runningAssistorRef.setValue(null);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                if(isFromMyPublicList){
                                    DatabaseReference selectedTemplateRef = mRootRef.child("public_templates").child
                                            (uid).child("my_public").child(templateName);
                                    final DatabaseReference activeTemplateRef = mRootRef.child("user").child(uid);

                                    selectedTemplateRef.setValue(null);
                                    activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                                            if(userModelClass.getActiveTemplate() != null){
                                                if(userModelClass.getActiveTemplate().equals(templateName)){
                                                    userModelClass.setActiveTemplate(null);
                                                }
                                            }
                                            activeTemplateRef.setValue(userModelClass);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else{
                                    DatabaseReference selectedTemplateRef = mRootRef.child("templates").child(uid).child(templateName);
                                    final DatabaseReference activeTemplateRef = mRootRef.child("user").child(uid);

                                    selectedTemplateRef.setValue(null);
                                    activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                                            if(userModelClass.getActiveTemplate() != null){
                                                if(userModelClass.getActiveTemplate().equals(templateName)){
                                                    userModelClass.setActiveTemplate(null);
                                                }
                                            }
                                            activeTemplateRef.setValue(userModelClass);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }


                                //TODO: Just go back to the saved templates page
                                Intent intent = new Intent(v.getContext(), MainActivity.class);
                                intent.putExtra("fragID", 0);
                                startActivity(intent);

                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });

        final DatabaseReference activeTemplateRef = mRootRef.child("user").child(uid);

        activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                if(userModelClass.getActiveTemplate() != null && templateName != null){
                    if(userModelClass.getActiveTemplate().equals(templateName)){
                        setAsActiveTemplate.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setAsActiveTemplate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                            userModelClass.setActiveTemplate(templateName);
                            activeTemplateRef.setValue(userModelClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    CharSequence toastText = "Set as Active Template";
                                    int duration = Toast.LENGTH_SHORT;
                                    try{
                                        //Snackbar snackbar = Snackbar.make(getView(), toastText, duration);
                                        //snackbar.show();
                                        Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();
                                    } catch (NullPointerException e){

                                    }

                                    //final DatabaseReference firstTimeRef = FirebaseDatabase.getInstance()
                                    //        .getReference().child
                                    //        ("firstTime").child(FirebaseAuth.getInstance().getCurrentUser().getxUid()
                                    //).child
                                    //        ("isSelectedProgFirstTime");
                                    //firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    //    @Override
                                    //    public void onDataChange(DataSnapshot dataSnapshot) {
                                    //        if(dataSnapshot.exists()){
                                    //            new FancyShowCaseView.Builder(getActivity())
                                    //                    .title("Let's head to the Workout Assistor! " +
                                    //                            "\n \n It's the middle item on the bottom
                                    // navigation" +
                                    //                    " " +
                                    //                            "bar.")
                                    //                    .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                                    //                    .build()
                                    //                    .show();
                                    //            firstTimeRef.setValue(null);
                                    //        }
                                    //    }
//
                                    //    @Override
                                    //    public void onCancelled(DatabaseError databaseError) {
//
                                    //    }
                                    //});
                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else if(!isChecked){
                    activeTemplateRef.child("activeTemplate").setValue(null);

                    CharSequence toastText = "Unselected as Active Template";
                    int duration = Toast.LENGTH_SHORT;

                    try{
                        //Snackbar snackbar = Snackbar.make(getView(), toastText, duration);
                        //snackbar.show();
                        Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e){

                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        final DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child
                ("firstTime").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child
                ("isSelectedProgFirstTime");
        firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    //editPenSmall.setEnabled(false);
                    //editTemplate.setEnabled(false);
                    //deleteTemplate.setEnabled(false);
                    //publishButton.setEnabled(false);

                    FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(getActivity())
                            .title("This is where you can edit or delete any of your programs." +
                                    "\n \n You can also publish your program to the public repository for anyone to " +
                                    "use." +
                                    "\n \n Most importantly, this is where you can set your Active Program. ")
                            .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                            .build();
                    FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder(getActivity())
                            //.focusOn(setAsActiveTemplate)
                            .title("Your Active Program is the program that will be used for your daily " +
                            "workouts/rest days. \n\n You can change your Active Program whenever you want, but avoid" +
                                    " doing it while also completing a workout.")
                            .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                            //.focusShape(FocusShape.ROUNDED_RECTANGLE)
                            //.fitSystemWindows(true)
                            .build();

                    new FancyShowCaseQueue()
                            .add(fancyShowCaseView1)
                            .add(fancyShowCaseView2)
                            .show();

                    firstTimeRef.setValue(null);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean containsDay(String day, String unformatted){

        boolean contains = false;
        String delims = "[_]";
        String[] tokens = unformatted.split(delims);

        for(String string : tokens){
            if(day.equals(string)){
                contains = true;
            }
        }

        return contains;
    }

    private boolean isExerciseName(String input){
        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == 1){
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("fragID", 0);
                startActivity(intent);
            }else{

            }
        }else if(requestCode == 3){
            if(resultCode == 3){
                if(data != null){
                    final String returnedName = data.getExtras().getString("templateName");
                    final DatabaseReference specificTemplateRef = mRootRef.child("public_templates").child("public").child
                            (firebaseKey);
                    specificTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            TemplateModelClass modelClass = dataSnapshot.getValue(TemplateModelClass.class);
                            String templateName = modelClass.getTemplateName();
                            modelClass.setUserId2(uid);
                            modelClass.setTemplateName(returnedName);

                            SharedPreferences sharedPref = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                            final String userName = sharedPref.getString("userName", "loading...");

                            modelClass.setUserName2(userName);
                            DatabaseReference myTemplateRef = mRootRef.child("templates").child(uid).child(returnedName);
                            myTemplateRef.setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    CharSequence toastText = "Template Saved";
                                    int duration = Toast.LENGTH_SHORT;
                                    try{
                                        Snackbar snackbar = Snackbar.make(getView(), toastText, duration);
                                        snackbar.show();
                                    } catch (NullPointerException e){

                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("template_name", templateName);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            navChangerCallback = (bottomNavChanger) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
