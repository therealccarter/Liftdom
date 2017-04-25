package com.liftdom.template_housing;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;

public class RenameTemplateDialog extends Activity {


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String templateName1 = "fail";

    ArrayList<String> arrayList1 = new ArrayList<>();
    String arrayListHeader1 = "fail";

    ArrayList<String> arrayList2 = new ArrayList<>();
    String arrayListHeader2 = "fail";

    ArrayList<String> arrayList3 = new ArrayList<>();
    String arrayListHeader3 = "fail";

    ArrayList<String> arrayList4 = new ArrayList<>();
    String arrayListHeader4 = "fail";

    ArrayList<String> arrayList5 = new ArrayList<>();
    String arrayListHeader5 = "fail";

    ArrayList<String> arrayList6 = new ArrayList<>();
    String arrayListHeader6 = "fail";

    ArrayList<String> arrayList7 = new ArrayList<>();
    String arrayListHeader7 = "fail";

    ArrayList<String> algorithmArrayList = new ArrayList<>();
    ArrayList<String> algorithmExList = new ArrayList<>();

    @BindView(R.id.templateNameEditText) EditText templateNameEditText;
    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.cancelButton) Button cancelButton;

    int arrayListInc = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rename_template_dialog);

        ButterKnife.bind(this);

        templateName1 = getIntent().getExtras().getString("templateName");
        templateNameEditText.setText(templateName1);

        final DatabaseReference templateRef = mRootRef.child("templates").child(uid).child(templateName1);

        templateRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            final String dayOrAlgString = dataSnapshot1.getKey();


                            if (!dayOrAlgString.equals("algorithm") && !dayOrAlgString.equals
                                    ("algorithmExercises")) {

                                    if (arrayListInc == 1) {

                                        DatabaseReference daySpecificRef = templateRef.child(dayOrAlgString);

                                        arrayListHeader1 = dayOrAlgString;

                                        daySpecificRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                    String value = dataSnapshot2.getValue(String.class);

                                                    arrayList1.add(value);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    } else if (arrayListInc == 2) {

                                        DatabaseReference daySpecificRef = templateRef.child(dayOrAlgString);

                                        arrayListHeader2 = dayOrAlgString;

                                        daySpecificRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                    String value = dataSnapshot2.getValue(String.class);

                                                    arrayList2.add(value);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    } else if (arrayListInc == 3) {


                                        DatabaseReference daySpecificRef = templateRef.child(dayOrAlgString);

                                        arrayListHeader3 = dayOrAlgString;

                                        daySpecificRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                    String value = dataSnapshot2.getValue(String.class);

                                                    arrayList3.add(value);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    } else if (arrayListInc == 4) {


                                        DatabaseReference daySpecificRef = templateRef.child(dayOrAlgString);

                                        arrayListHeader4 = dayOrAlgString;

                                        daySpecificRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                    String value = dataSnapshot2.getValue(String.class);

                                                    arrayList4.add(value);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    } else if (arrayListInc == 5) {


                                        DatabaseReference daySpecificRef = templateRef.child(dayOrAlgString);

                                        arrayListHeader5 = dayOrAlgString;

                                        daySpecificRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                    String value = dataSnapshot2.getValue(String.class);

                                                    arrayList5.add(value);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    } else if (arrayListInc == 6) {


                                        DatabaseReference daySpecificRef = templateRef.child(dayOrAlgString);

                                        arrayListHeader6 = dayOrAlgString;

                                        daySpecificRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                    String value = dataSnapshot2.getValue(String.class);

                                                    arrayList6.add(value);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    } else if (arrayListInc == 7) {


                                        DatabaseReference daySpecificRef = templateRef.child(dayOrAlgString);

                                        arrayListHeader7 = dayOrAlgString;

                                        daySpecificRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                    String value = dataSnapshot2.getValue(String.class);

                                                    arrayList7.add(value);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                arrayListInc++;

                            } else if (dayOrAlgString.equals("algorithm")) {
                                DatabaseReference algorithmRef = templateRef.child(dayOrAlgString);

                                algorithmRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot3 : dataSnapshot.getChildren()) {
                                            String algorithmValue = dataSnapshot3.getValue(String.class);

                                            algorithmArrayList.add(algorithmValue);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else if (dayOrAlgString.equals("algorithmExercises")) {
                                DatabaseReference algorithmExRef = templateRef.child(dayOrAlgString);

                                algorithmExRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot4 : dataSnapshot.getChildren()) {
                                            String algorithmExValue = dataSnapshot4.getValue(String.class);

                                            algorithmExList.add(algorithmExValue);
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




        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                templateRef.setValue(null);

                final String templateNameNew = templateNameEditText.getText().toString();

                final DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");
                activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String activeTemplate = dataSnapshot.getValue(String.class);
                        if(activeTemplate != null){
                            if(activeTemplate.equals(templateName1)){
                                activeTemplateRef.setValue(templateNameNew);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                DatabaseReference newTemplateRef = mRootRef.child("templates").child(uid).child(templateNameNew);

                if(!arrayList1.isEmpty()){
                    List<String> list = new ArrayList<>();

                    for(String value : arrayList1){
                        list.add(value);
                    }

                    newTemplateRef.child(arrayListHeader1).setValue(list);

                }

                if(!arrayList2.isEmpty()){
                    List<String> list = new ArrayList<>();


                    for(String value : arrayList2){
                        list.add(value);
                    }

                    newTemplateRef.child(arrayListHeader2).setValue(list);

                }

                if(!arrayList3.isEmpty()){
                    List<String> list = new ArrayList<>();

                    for(String value : arrayList3){
                        list.add(value);
                    }

                    newTemplateRef.child(arrayListHeader3).setValue(list);

                }

                if(!arrayList4.isEmpty()){
                    List<String> list = new ArrayList<>();

                    for(String value : arrayList4){
                        list.add(value);
                    }

                    newTemplateRef.child(arrayListHeader4).setValue(list);

                }

                if(!arrayList5.isEmpty()){
                    List<String> list = new ArrayList<>();

                    for(String value : arrayList5){
                        list.add(value);
                    }

                    newTemplateRef.child(arrayListHeader5).setValue(list);

                }

                if(!arrayList6.isEmpty()){
                    List<String> list = new ArrayList<>();

                    for(String value : arrayList6){
                        list.add(value);
                    }

                    newTemplateRef.child(arrayListHeader6).setValue(list);

                }

                if(!arrayList7.isEmpty()){
                    List<String> list = new ArrayList<>();

                    for(String value : arrayList7){
                        list.add(value);
                    }

                    newTemplateRef.child(arrayListHeader7).setValue(list);

                }

                if(!algorithmArrayList.isEmpty()){
                    List<String> list = new ArrayList<>();

                    for(String value : algorithmArrayList){
                        list.add(value);
                    }

                    newTemplateRef.child("algorithm").setValue(list);
                }

                if(!algorithmExList.isEmpty()){
                    List<String> list = new ArrayList<>();

                    for(String value : algorithmExList){
                        list.add(value);
                    }

                    newTemplateRef.child("algorithmExercises").setValue(list);
                }



                setResult(1);

                finish();

                }
            });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });

    }



    @Override
    public void onStart() {
        super.onStart();



    }


}
