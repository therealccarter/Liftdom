package com.liftdom.template_housing;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.SetsLevelSSFrag;
import com.liftdom.template_editor.TemplateEditorActivity;
import com.liftdom.template_editor.TemplateModelClass;
import org.joda.time.LocalDate;

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

    @BindView(R.id.selectedTemplateTitle) TextView selectedTemplateNameView;
    @BindView(R.id.editThisTemplate) Button editTemplate;
    @BindView(R.id.deleteThisTemplate) Button deleteTemplate;
    @BindView(R.id.activeTemplateCheckbox) CheckBox setAsActiveTemplate;
    @BindView(R.id.templateListedView) LinearLayout templateListedViewHolder;
    @BindView(R.id.editPenSmall) ImageView editPenSmall;
    @BindView(R.id.descriptionView) TextView descriptionView;
    @BindView(R.id.shareThisTemplate) Button shareTemplate;
    @BindView(R.id.authorNameView) TextView authorNameView;

    int colorIncrement = 0;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ArrayList<HashMap<String, List<String>>> mapList = new ArrayList<>();
    HashMap<String, List<String>>[] sortedMapList = new HashMap[7];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_template, container, false);

        ButterKnife.bind(this, view);

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
                Intent intent = new Intent(v.getContext(), RenameTemplateDialog.class);
                intent.putExtra("templateName", templateName);
                startActivityForResult(intent, 1);
            }
        });

        if(savedInstanceState == null){
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
                            break;
                        } else if(containsDay("Tuesday", map.get("0_key").get(0))){
                            sortedMapList[1] = map;
                            break;
                        } else if(containsDay("Wednesday", map.get("0_key").get(0))){
                            sortedMapList[2] = map;
                            break;
                        } else if(containsDay("Thursday", map.get("0_key").get(0))){
                            sortedMapList[3] = map;
                            break;
                        } else if(containsDay("Friday", map.get("0_key").get(0))){
                            sortedMapList[4] = map;
                            break;
                        } else if(containsDay("Saturday", map.get("0_key").get(0))){
                            sortedMapList[5] = map;
                            break;
                        } else if(containsDay("Sunday", map.get("0_key").get(0))){
                            sortedMapList[6] = map;
                            break;
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


        editTemplate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                String isEdit = "yes";
                Intent intent = new Intent(v.getContext(), TemplateEditorActivity.class);
                intent.putExtra("isEdit", isEdit );
                intent.putExtra("templateName", templateName);

                startActivity(intent);
            }
        });

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
                                DatabaseReference selectedTemplateRef = mRootRef.child("templates").child(uid).child(templateName);
                                final DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child
                                        ("active_template");

                                selectedTemplateRef.setValue(null);
                                activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String value = dataSnapshot.getValue(String.class);
                                        if(value != null){
                                            if(value.equals(templateName)){
                                                activeTemplateRef.setValue(null);
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                Intent intent = new Intent(getContext(), TemplateHousingActivity.class);
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

        final DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");

        activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String activeTemplate = dataSnapshot.getValue(String.class);
                if(activeTemplate != null && templateName != null) {
                    if (templateName.equals(activeTemplate)) {
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
                            String activeTemplate = dataSnapshot.getValue(String.class);
                            if(activeTemplate != null && templateName != null) {
                                if (!templateName.equals(activeTemplate)) {
                                    DatabaseReference boolRunDateRef = mRootRef.child("runningAssistor").child(uid)
                                            .child("isRunning").child("isRunningBoolDate");
                                    boolRunDateRef.setValue("false" + "_" + LocalDate.now().toString());
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    activeTemplateRef.setValue(templateName);
                    CharSequence toastText = "Set as Active Template";
                    int duration = Toast.LENGTH_SHORT;
                    try{
                        Snackbar snackbar = Snackbar.make(getView(), toastText, duration);
                        snackbar.show();
                    } catch (NullPointerException e){

                    }


                } else if(!isChecked){
                    activeTemplateRef.setValue(null);

                    CharSequence toastText = "Unselected as Active Template";
                    int duration = Toast.LENGTH_SHORT;

                    try{
                        Snackbar snackbar = Snackbar.make(getView(), toastText, duration);
                        snackbar.show();
                    } catch (NullPointerException e){

                    }
                }
            }
        });

        return view;
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
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("fragID", 0);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("template_name", templateName);

        super.onSaveInstanceState(savedInstanceState);
    }



}
