package com.liftdom.user_profile;


import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CalendarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.decorators.OneDayDecorator;
import com.liftdom.liftdom.decorators.PastEventDecorator;
import com.liftdom.template_housing.TemplateHousingActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

public class HistoryCalendarTab extends Fragment implements OnDateSelectedListener {


    public HistoryCalendarTab() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ArrayList<CalendarDay> pastDates = new ArrayList<>();
    ArrayList<CalendarDay> futureDates = new ArrayList<>();

    long futureIncrementor = 0;

    ArrayList<String> firstAL = new ArrayList<>();
    ArrayList<String> secondAL = new ArrayList<>();
    ArrayList<String> thirdAL = new ArrayList<>();
    ArrayList<String> fourthAL = new ArrayList<>();
    ArrayList<String> fifthAL = new ArrayList<>();
    ArrayList<String> sixthAL = new ArrayList<>();
    ArrayList<String> seventhAL = new ArrayList<>();

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    @BindView(R.id.calendarView) MaterialCalendarView widget;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_calendar_tab, container, false);

        //TODO: Add missed days in the past

        ButterKnife.bind(this, view);

        // things that only happen upon action

        widget.setOnDateChangedListener(this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        // Future dates draw from active template.
        // Past dates draw from history, obviously.

        LocalDate currentDate = LocalDate.now();
        widget.setSelectedDate(currentDate.toDate());

        DateTime minDate = new DateTime("2017-01-01");
        DateTime maxDate = new DateTime("2017-12-31");

        widget.state().edit()
                .setMinimumDate(minDate.toDate())
                .setMaximumDate(maxDate.toDate())
                .commit();

        DatabaseReference historyRef = mRootRef.child("workout_history").child(uid);

        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long incrementor = 0;

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    DateTime dateTime = new DateTime(dataSnapshot1.getKey());

                    CalendarDay convertedDateTime = CalendarDay.from(dateTime.toDate());

                    pastDates.add(convertedDateTime);

                    incrementor++;

                    if(incrementor == dataSnapshot.getChildrenCount()){
                        widget.addDecorator(new PastEventDecorator(Color.GREEN, pastDates));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");

        activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //TODO: Apply algorithm

                String activeTemplate = dataSnapshot.getValue(String.class);

                final DatabaseReference futureRef = mRootRef.child("templates").child(uid).child(activeTemplate);

                futureRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            ++futureIncrementor;

                            String keyValue = dataSnapshot1.getKey();

                            if(!keyValue.equals("algorithm") && !keyValue.equals("algorithmExercises")){
                                if(futureIncrementor == 1){
                                    firstAL.add(keyValue);
                                    DatabaseReference specificDayRef = futureRef.child(keyValue);

                                    specificDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                                String value = dataSnapshot2.getValue(String.class);
                                                firstAL.add(value);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else if(futureIncrementor == 2){
                                    secondAL.add(keyValue);
                                    DatabaseReference specificDayRef = futureRef.child(keyValue);

                                    specificDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                                String value = dataSnapshot2.getValue(String.class);
                                                secondAL.add(value);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else if(futureIncrementor == 3){
                                    thirdAL.add(keyValue);
                                    DatabaseReference specificDayRef = futureRef.child(keyValue);

                                    specificDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                                String value = dataSnapshot2.getValue(String.class);
                                                thirdAL.add(value);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else if(futureIncrementor == 4){
                                    fourthAL.add(keyValue);
                                    DatabaseReference specificDayRef = futureRef.child(keyValue);

                                    specificDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                                String value = dataSnapshot2.getValue(String.class);
                                                fourthAL.add(value);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else if(futureIncrementor == 5){
                                    fifthAL.add(keyValue);
                                    DatabaseReference specificDayRef = futureRef.child(keyValue);

                                    specificDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                                String value = dataSnapshot2.getValue(String.class);
                                                fifthAL.add(value);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else if(futureIncrementor == 6){
                                    sixthAL.add(keyValue);
                                    DatabaseReference specificDayRef = futureRef.child(keyValue);

                                    specificDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                                String value = dataSnapshot2.getValue(String.class);
                                                sixthAL.add(value);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else if(futureIncrementor == 7){
                                    seventhAL.add(keyValue);
                                    DatabaseReference specificDayRef = futureRef.child(keyValue);

                                    specificDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                                String value = dataSnapshot2.getValue(String.class);
                                                seventhAL.add(value);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            if(futureIncrementor == dataSnapshot.getChildrenCount()){
                                futureDatesConstructor();
                            }

                        }

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



        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());

        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

        DateTime dateTime = new DateTime(date.getDate());

        String formatted = fmt.print(dateTime);

        if(pastDates.contains(date)){
            Intent pastIntent = new Intent(getContext(), SelectedPastDateDialog.class);
            pastIntent.putExtra("date", formatted);
            startActivity(pastIntent);
        }

        widget.invalidateDecorators();
    }

    public void futureDatesConstructor(){
        // Here we'll deconstruct the array lists and try to turn it into something tangible
    }

}












