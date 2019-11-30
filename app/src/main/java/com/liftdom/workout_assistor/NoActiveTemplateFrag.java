package com.liftdom.workout_assistor;


import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateEditorActivity;
import com.liftdom.template_housing.NewTemplateMenuFrag;
import com.liftdom.template_housing.TemplateMenuFrag;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoActiveTemplateFrag extends Fragment {

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public NoActiveTemplateFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.toTemplatesButton) Button toTemplatesButton;
    @BindView(R.id.newTemplateButton) Button fromScratch;
    @BindView(R.id.workoutInsteadButton) Button freestyleButton;
    @BindView(R.id.workoutInsteadHolder) CardView workoutOnRestDayHolder;
    @BindView(R.id.loadingHolder) LinearLayout loadingHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_active_template, container, false);

        ButterKnife.bind(this, view);

        toTemplatesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new TemplateMenuFrag());
                fragmentTransaction.commit();
            }
        });

        fromScratch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                //String isEdit = "no";
                //Intent intent = new Intent(v.getContext(), TemplateEditorActivity.class);
                //intent.putExtra("isEdit", isEdit );
                //startActivity(intent);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new NewTemplateMenuFrag(), "myTemplatesTag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        freestyleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                workoutOnRestDayHolder.setVisibility(View.GONE);
                loadingHolder.setVisibility(View.VISIBLE);

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user")
                        .child(uid).child("isImperial");

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean isTemplateImperial = dataSnapshot.getValue(Boolean.class);

                        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                        final LocalDate localDate = LocalDate.now();
                        String dateTimeString = fmt.print(localDate);

                        HashMap<String, HashMap<String, List<String>>> runningMap = new HashMap<>();

                        HashMap<String, List<String>> subMap = new HashMap<>();

                        List<String> subList = new ArrayList<>();

                        subList.add("CLICK TO CHOOSE EXERCISE");
                        subList.add("1@1_unchecked");

                        subMap.put("0_key", subList);

                        runningMap.put("1_key", subMap);

                        WorkoutProgressModelClass progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                                false, runningMap, "", "", "", isTemplateImperial,
                                null, true, true);

                        DatabaseReference runningRef = FirebaseDatabase.getInstance().getReference().child
                                ("runningAssistor").child(uid).child("assistorModel");

                        runningRef.setValue(progressModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.putExtra("fragID",  2);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;
    }

}
