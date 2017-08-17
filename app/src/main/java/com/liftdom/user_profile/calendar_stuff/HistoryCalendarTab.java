package com.liftdom.user_profile.calendar_stuff;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivitySingleton;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.user_profile.calendar_stuff.decorators.OneDayDecorator;
import com.liftdom.user_profile.calendar_stuff.decorators.PastEventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class HistoryCalendarTab extends Fragment implements OnDateSelectedListener {


    public HistoryCalendarTab() {
        // Required empty public constructor
    }

    public Boolean isOtherUser = false;
    public String xUid = "null";

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ArrayList<CalendarDay> pastDates = new ArrayList<>();
    LocalDate firstDay;
    ArrayList<CalendarDay> missedDates = new ArrayList<>();
    ArrayList<CalendarDay> futureDates = new ArrayList<>();

    long futureIncrementor = 0;
    int constructorInc = 0;
    long futureSubInc1 = 0;
    long futureSubInc2 = 0;
    long futureSubInc3 = 0;
    long futureSubInc4 = 0;
    long futureSubInc5 = 0;
    long futureSubInc6 = 0;
    long futureSubInc7 = 0;

    boolean subIncBool1 = true;
    boolean subIncBool2 = true;
    boolean subIncBool3 = true;
    boolean subIncBool4 = true;
    boolean subIncBool5 = true;
    boolean subIncBool6 = true;
    boolean subIncBool7 = true;

    ArrayList<String> firstAL = new ArrayList<>();
    HashMap<String, List<String>> firstMap = new HashMap<>();

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

        ButterKnife.bind(this, view);

        if(isOtherUser){
            uid = xUid;
        }

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

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    DateTime dateTime = new DateTime(dataSnapshot1.getKey());

                    CalendarDay convertedDateTime = CalendarDay.from(dateTime.toDate());

                    if (incrementor == 0) {
                        firstDay = new LocalDate(dateTime);
                    }

                    pastDates.add(convertedDateTime);

                    incrementor++;

                    if (incrementor == dataSnapshot.getChildrenCount()) {

                        widget.addDecorator(new PastEventDecorator(Color.GREEN, pastDates));

                        LocalDate today = LocalDate.now();

                        int days = Days.daysBetween(firstDay, today).getDays();

                        for (int i = 0; i < days; i++) {
                            LocalDate localDate = firstDay.withFieldAdded(DurationFieldType.days(), i);

                            CalendarDay calendarDay = CalendarDay.from(localDate.toDate());

                            if (!pastDates.contains(calendarDay)) {
                                missedDates.add(calendarDay);
                            }

                            if (i == days - 1) {
                                widget.addDecorator(new PastEventDecorator(Color.RED, missedDates, 5));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        DatabaseReference userRef = mRootRef.child("user").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                String activeTemplate = userModelClass.getActiveTemplate();

                if(activeTemplate != null){

                    final DatabaseReference futureRef = mRootRef.child("templates").child(uid).child(activeTemplate);

                    futureRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            TemplateModelClass templateModelClass = dataSnapshot.getValue(TemplateModelClass.class);

                            if(templateModelClass.getMapOne() != null){
                                if(!templateModelClass.getMapOne().isEmpty()){
                                    futureConstructor(templateModelClass.getMapOne());
                                }
                            }
                            if(templateModelClass.getMapTwo() != null){
                                if(!templateModelClass.getMapTwo().isEmpty()){
                                    futureConstructor(templateModelClass.getMapTwo());
                                }
                            }
                            if(templateModelClass.getMapThree() != null){
                                if(!templateModelClass.getMapThree().isEmpty()){
                                    futureConstructor(templateModelClass.getMapThree());
                                }
                            }
                            if(templateModelClass.getMapFour() != null){
                                if(!templateModelClass.getMapFour().isEmpty()){
                                    futureConstructor(templateModelClass.getMapFour());
                                }
                            }
                            if(templateModelClass.getMapFive() != null){
                                if(!templateModelClass.getMapFive().isEmpty()){
                                    futureConstructor(templateModelClass.getMapFive());
                                }
                            }
                            if(templateModelClass.getMapSix() != null){
                                if(!templateModelClass.getMapSix().isEmpty()){
                                    futureConstructor(templateModelClass.getMapSix());
                                }
                            }
                            if(templateModelClass.getMapSeven() != null){
                                if(!templateModelClass.getMapSeven().isEmpty()){
                                    futureConstructor(templateModelClass.getMapSeven());
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
            if(isOtherUser){
                pastIntent.putExtra("isOtherUser", true);
                pastIntent.putExtra("xUid", xUid);
            }
            pastIntent.putExtra("date", formatted);
            startActivity(pastIntent);
        }else if(FutureDateHelperClass.getInstance().DateCollection1.contains(date)){
            Intent futureIntent = new Intent(getContext(), SelectedFutureDateDialog.class);
            futureIntent.putExtra("date", formatted);
            futureIntent.putExtra("collectionNumber", 1);
            startActivity(futureIntent);
        }else if(FutureDateHelperClass.getInstance().DateCollection2.contains(date)){
            Intent futureIntent = new Intent(getContext(), SelectedFutureDateDialog.class);
            futureIntent.putExtra("date", formatted);
            futureIntent.putExtra("collectionNumber", 2);
            startActivity(futureIntent);
        }else if(FutureDateHelperClass.getInstance().DateCollection3.contains(date)){
            Intent futureIntent = new Intent(getContext(), SelectedFutureDateDialog.class);
            futureIntent.putExtra("date", formatted);
            futureIntent.putExtra("collectionNumber", 3);
            startActivity(futureIntent);
        }else if(FutureDateHelperClass.getInstance().DateCollection4.contains(date)){
            Intent futureIntent = new Intent(getContext(), SelectedFutureDateDialog.class);
            futureIntent.putExtra("date", formatted);
            futureIntent.putExtra("collectionNumber", 4);
            startActivity(futureIntent);
        }else if(FutureDateHelperClass.getInstance().DateCollection5.contains(date)){
            Intent futureIntent = new Intent(getContext(), SelectedFutureDateDialog.class);
            futureIntent.putExtra("date", formatted);
            futureIntent.putExtra("collectionNumber", 5);
            startActivity(futureIntent);
        }else if(FutureDateHelperClass.getInstance().DateCollection6.contains(date)){
            Intent futureIntent = new Intent(getContext(), SelectedFutureDateDialog.class);
            futureIntent.putExtra("date", formatted);
            futureIntent.putExtra("collectionNumber", 6);
            startActivity(futureIntent);
        }else if(FutureDateHelperClass.getInstance().DateCollection7.contains(date)){
            Intent futureIntent = new Intent(getContext(), SelectedFutureDateDialog.class);
            futureIntent.putExtra("date", formatted);
            futureIntent.putExtra("collectionNumber", 7);
            startActivity(futureIntent);
        }

        widget.invalidateDecorators();
    }

    private void futureConstructor(HashMap<String, List<String>> map){
        ArrayList<String> days = getDayStrings(map.get("0_key").get(0));
        ArrayList<Integer> daysAsInts = new ArrayList<>();

        constructorInc++;

        for(String string : days){
            daysAsInts.add(dayIntFromString(string));
        }

        FutureDateHelperClass.getInstance().DataCollectionMap.put(FutureDateHelperClass.getInstance()
                .DataCollectionMap.size() + "_key", map);

        for(int i = 1; i < 90; i++){
            LocalDate dateTime = LocalDate.now();
            dateTime = dateTime.plusDays(i);

            if(daysAsInts.contains(dateTime.getDayOfWeek())){

                CalendarDay convertedDateTime = CalendarDay.from(dateTime.toDate());

                if(constructorInc == 1){
                    FutureDateHelperClass.getInstance().DateCollection1.add(convertedDateTime);
                }else if(constructorInc == 2){
                    FutureDateHelperClass.getInstance().DateCollection2.add(convertedDateTime);
                }else if(constructorInc == 3){
                    FutureDateHelperClass.getInstance().DateCollection3.add(convertedDateTime);
                }else if(constructorInc == 4){
                    FutureDateHelperClass.getInstance().DateCollection4.add(convertedDateTime);
                }else if(constructorInc == 5){
                    FutureDateHelperClass.getInstance().DateCollection5.add(convertedDateTime);
                }else if(constructorInc == 6){
                    FutureDateHelperClass.getInstance().DateCollection6.add(convertedDateTime);
                }else if(constructorInc == 7){
                    FutureDateHelperClass.getInstance().DateCollection7.add(convertedDateTime);
                }
            }
        }


        if(constructorInc == 1){
            if(!FutureDateHelperClass.getInstance().DateCollection1.isEmpty()){
                widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                        .DateCollection1, 7));
            }
        }else if(constructorInc == 2){
            if(!FutureDateHelperClass.getInstance().DateCollection2.isEmpty()){
                widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                        .DateCollection2, 7));
            }
        }else if(constructorInc == 3){
            if(!FutureDateHelperClass.getInstance().DateCollection3.isEmpty()){
                widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                        .DateCollection3, 7));
            }
        }else if(constructorInc == 4){
            if(!FutureDateHelperClass.getInstance().DateCollection4.isEmpty()){
                widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                        .DateCollection4, 7));
            }
        }else if(constructorInc == 5){
            if(!FutureDateHelperClass.getInstance().DateCollection5.isEmpty()){
                widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                        .DateCollection5, 7));
            }
        }else if(constructorInc == 6){
            if(!FutureDateHelperClass.getInstance().DateCollection6.isEmpty()){
                widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                        .DateCollection6, 7));
            }
        }else if(constructorInc == 7){
            if(!FutureDateHelperClass.getInstance().DateCollection7.isEmpty()){
                widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                        .DateCollection7, 7));
            }
        }


    }

    int dayIntFromString(String day){
        int dayInt = 0;

        if(day.equals("Monday")){
            dayInt = 1;
        }else if(day.equals("Tuesday")){
            dayInt = 2;
        }else if(day.equals("Wednesday")){
            dayInt = 3;
        }else if(day.equals("Thursday")){
            dayInt = 4;
        }else if(day.equals("Friday")){
            dayInt = 5;
        }else if(day.equals("Saturday")){
            dayInt = 6;
        }else if(day.equals("Sunday")){
            dayInt = 7;
        }

        return dayInt;
    }

    public ArrayList<String> getDayStrings(String daysUnbroken){
        ArrayList<String> daysBroken = new ArrayList<>();

        String delims = "[_]";

        String[] broken = daysUnbroken.split(delims);

        for(String string : broken){
            daysBroken.add(string);
        }

        return daysBroken;
    }


}












