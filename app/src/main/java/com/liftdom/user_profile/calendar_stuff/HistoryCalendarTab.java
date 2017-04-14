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
import com.liftdom.liftdom.R;
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
    LocalDate firstDay;
    ArrayList<CalendarDay> missedDates = new ArrayList<>();
    ArrayList<CalendarDay> futureDates = new ArrayList<>();

    long futureIncrementor = 0;
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

                    if(incrementor == 0){
                        firstDay = new LocalDate(dateTime);
                    }

                    pastDates.add(convertedDateTime);

                    incrementor++;

                    if(incrementor == dataSnapshot.getChildrenCount()){

                        widget.addDecorator(new PastEventDecorator(Color.GREEN, pastDates));

                        LocalDate today = LocalDate.now();

                        int days = Days.daysBetween(firstDay, today).getDays();

                        for(int i = 0; i < days; i++){
                            LocalDate localDate = firstDay.withFieldAdded(DurationFieldType.days(), i);

                            CalendarDay calendarDay = CalendarDay.from(localDate.toDate());

                            if(!pastDates.contains(calendarDay)){
                                missedDates.add(calendarDay);
                            }

                            if(i == days - 1){
                                widget.addDecorator(new PastEventDecorator(Color.RED, missedDates, 7));
                            }
                        }
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

                                            subIncBool1 = false;

                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                                                ++futureSubInc1;

                                                String value = dataSnapshot2.getValue(String.class);
                                                firstAL.add(value);

                                                subIncBool1 = subIncValidator(futureSubInc1, dataSnapshot
                                                        .getChildrenCount());

                                                if(subIncBool1){
                                                    futureConstructor1();
                                                }
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

                                            subIncBool2 = false;

                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                                                ++futureSubInc2;

                                                String value = dataSnapshot2.getValue(String.class);
                                                secondAL.add(value);

                                                subIncBool2 = subIncValidator(futureSubInc2, dataSnapshot
                                                        .getChildrenCount());

                                                if(subIncBool2){
                                                    futureConstructor2();
                                                }
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

                                            subIncBool3 = false;

                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                                                ++futureSubInc3;

                                                String value = dataSnapshot2.getValue(String.class);
                                                thirdAL.add(value);

                                                subIncBool3 = subIncValidator(futureSubInc3, dataSnapshot
                                                        .getChildrenCount());

                                                if(subIncBool3){
                                                    futureConstructor3();
                                                }
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

                                            subIncBool4 = false;

                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                                                ++futureSubInc4;

                                                String value = dataSnapshot2.getValue(String.class);
                                                fourthAL.add(value);

                                                subIncBool4 = subIncValidator(futureSubInc4, dataSnapshot
                                                        .getChildrenCount());

                                                if(subIncBool4){
                                                    futureConstructor4();
                                                }
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

                                            subIncBool5 = false;

                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                                                ++futureSubInc5;

                                                String value = dataSnapshot2.getValue(String.class);
                                                fifthAL.add(value);

                                                subIncBool5 = subIncValidator(futureSubInc5, dataSnapshot
                                                        .getChildrenCount());

                                                if(subIncBool5){
                                                    futureConstructor5();
                                                }
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

                                            subIncBool6 = false;

                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                                                ++futureSubInc6;

                                                String value = dataSnapshot2.getValue(String.class);
                                                sixthAL.add(value);

                                                subIncBool6 = subIncValidator(futureSubInc6, dataSnapshot
                                                        .getChildrenCount());

                                                if(subIncBool6){
                                                    futureConstructor6();
                                                }
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

                                            subIncBool7 = false;

                                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                                                ++futureSubInc7;

                                                String value = dataSnapshot2.getValue(String.class);
                                                seventhAL.add(value);

                                                subIncBool7 = subIncValidator(futureSubInc7, dataSnapshot
                                                        .getChildrenCount());

                                                if(subIncBool7){
                                                    futureConstructor7();
                                                }
                                            }
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

    public void futureConstructor1(){
        if(!firstAL.isEmpty()){
            ArrayList<String> days = getDayStrings(firstAL.get(0));
            ArrayList<Integer> daysAsInts = new ArrayList<>();

            ArrayList<String> data = new ArrayList<>();

            for(String string : days){
                daysAsInts.add(dayIntFromString(string));
            }

            for(int i = 1; i < firstAL.size(); i++){
                data.add(firstAL.get(i));

                if(i == (firstAL.size() - 1)){
                    FutureDateHelperClass.getInstance().DataCollection.add(0, data);
                }
            }

            // so now we have an arraylist of, say, Monday & Thursday
            // we could get dates for the remainder of the year, and somehow associate those dates with the info
            // in a singleton helper class...

            // So, this part works fine..
            for(int i = 1; i < 90; i++){
                DateTime dateTime = new DateTime(DateTime.now());
                dateTime = dateTime.plusDays(i);

                if(daysAsInts.contains(dateTime.getDayOfWeek())){

                    CalendarDay convertedDateTime = CalendarDay.from(dateTime.toDate());

                    FutureDateHelperClass.getInstance().DateCollection1.add(convertedDateTime);
                }
            }
        }

        if(!FutureDateHelperClass.getInstance().DateCollection1.isEmpty()){
            widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                    .DateCollection1));
        }
    }

    public void futureConstructor2(){
        if(!secondAL.isEmpty()){
            ArrayList<String> days = getDayStrings(secondAL.get(0));
            ArrayList<Integer> daysAsInts = new ArrayList<>();

            ArrayList<String> data = new ArrayList<>();

            for(String string : days){
                daysAsInts.add(dayIntFromString(string));
            }

            for(int i = 1; i < secondAL.size(); i++){
                data.add(secondAL.get(i));

                if(i == (secondAL.size() - 1)){
                    FutureDateHelperClass.getInstance().DataCollection.add(1, data);
                }
            }


            for(int i = 1; i < 90; i++){
                DateTime dateTime = new DateTime(DateTime.now());
                dateTime = dateTime.plusDays(i);

                if(daysAsInts.contains(dateTime.getDayOfWeek())){

                    CalendarDay convertedDateTime = CalendarDay.from(dateTime.toDate());

                    FutureDateHelperClass.getInstance().DateCollection2.add(convertedDateTime);
                }
            }
        }

        if(!FutureDateHelperClass.getInstance().DateCollection2.isEmpty()){
            widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                    .DateCollection2));
        }
    }

    public void futureConstructor3(){
        if(!thirdAL.isEmpty()){
            ArrayList<String> days = getDayStrings(thirdAL.get(0));
            ArrayList<Integer> daysAsInts = new ArrayList<>();

            ArrayList<String> data = new ArrayList<>();

            for(String string : days){
                daysAsInts.add(dayIntFromString(string));
            }

            for(int i = 1; i < thirdAL.size(); i++){
                data.add(thirdAL.get(i));

                if(i == (thirdAL.size() - 1)){
                    FutureDateHelperClass.getInstance().DataCollection.add(2, data);
                }
            }


            for(int i = 1; i < 90; i++){
                DateTime dateTime = new DateTime(DateTime.now());
                dateTime = dateTime.plusDays(i);

                if(daysAsInts.contains(dateTime.getDayOfWeek())){

                    CalendarDay convertedDateTime = CalendarDay.from(dateTime.toDate());

                    FutureDateHelperClass.getInstance().DateCollection3.add(convertedDateTime);
                }
            }
        }

        if(!FutureDateHelperClass.getInstance().DateCollection3.isEmpty()){
            widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                    .DateCollection3));
        }
    }

    public void futureConstructor4(){
        if(!fourthAL.isEmpty()){
            ArrayList<String> days = getDayStrings(fourthAL.get(0));
            ArrayList<Integer> daysAsInts = new ArrayList<>();

            ArrayList<String> data = new ArrayList<>();

            for(String string : days){
                daysAsInts.add(dayIntFromString(string));
            }

            for(int i = 1; i < fourthAL.size(); i++){
                data.add(fourthAL.get(i));

                if(i == (fourthAL.size() - 1)){
                    FutureDateHelperClass.getInstance().DataCollection.add(3, data);
                }
            }


            for(int i = 1; i < 90; i++){
                DateTime dateTime = new DateTime(DateTime.now());
                dateTime = dateTime.plusDays(i);

                if(daysAsInts.contains(dateTime.getDayOfWeek())){

                    CalendarDay convertedDateTime = CalendarDay.from(dateTime.toDate());

                    FutureDateHelperClass.getInstance().DateCollection4.add(convertedDateTime);
                }
            }
        }

        if(!FutureDateHelperClass.getInstance().DateCollection4.isEmpty()){
            widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                    .DateCollection4));
        }
    }

    public void futureConstructor5(){
        if(!fifthAL.isEmpty()){
            ArrayList<String> days = getDayStrings(fifthAL.get(0));
            ArrayList<Integer> daysAsInts = new ArrayList<>();

            ArrayList<String> data = new ArrayList<>();

            for(String string : days){
                daysAsInts.add(dayIntFromString(string));
            }

            for(int i = 1; i < fifthAL.size(); i++){
                data.add(fifthAL.get(i));

                if(i == (fifthAL.size() - 1)){
                    FutureDateHelperClass.getInstance().DataCollection.add(4, data);
                }
            }


            for(int i = 1; i < 90; i++){
                DateTime dateTime = new DateTime(DateTime.now());
                dateTime = dateTime.plusDays(i);

                if(daysAsInts.contains(dateTime.getDayOfWeek())){

                    CalendarDay convertedDateTime = CalendarDay.from(dateTime.toDate());

                    FutureDateHelperClass.getInstance().DateCollection5.add(convertedDateTime);
                }
            }
        }

        if(!FutureDateHelperClass.getInstance().DateCollection5.isEmpty()){
            widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                    .DateCollection5));
        }
    }

    public void futureConstructor6(){
        if(!sixthAL.isEmpty()){
            ArrayList<String> days = getDayStrings(sixthAL.get(0));
            ArrayList<Integer> daysAsInts = new ArrayList<>();

            ArrayList<String> data = new ArrayList<>();

            for(String string : days){
                daysAsInts.add(dayIntFromString(string));
            }

            for(int i = 1; i < sixthAL.size(); i++){
                data.add(sixthAL.get(i));

                if(i == (sixthAL.size() - 1)){
                    FutureDateHelperClass.getInstance().DataCollection.add(5, data);
                }
            }


            for(int i = 1; i < 90; i++){
                DateTime dateTime = new DateTime(DateTime.now());
                dateTime = dateTime.plusDays(i);

                if(daysAsInts.contains(dateTime.getDayOfWeek())){

                    CalendarDay convertedDateTime = CalendarDay.from(dateTime.toDate());

                    FutureDateHelperClass.getInstance().DateCollection6.add(convertedDateTime);
                }
            }
        }

        if(!FutureDateHelperClass.getInstance().DateCollection6.isEmpty()){
            widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                    .DateCollection6));
        }
    }

    public void futureConstructor7(){
        if(!seventhAL.isEmpty()){
            ArrayList<String> days = getDayStrings(seventhAL.get(0));
            ArrayList<Integer> daysAsInts = new ArrayList<>();

            ArrayList<String> data = new ArrayList<>();

            for(String string : days){
                daysAsInts.add(dayIntFromString(string));
            }

            for(int i = 1; i < seventhAL.size(); i++){
                data.add(seventhAL.get(i));

                if(i == (seventhAL.size() - 1)){
                    FutureDateHelperClass.getInstance().DataCollection.add(6, data);
                }
            }


            for(int i = 1; i < 90; i++){
                DateTime dateTime = new DateTime(DateTime.now());
                dateTime = dateTime.plusDays(i);

                if(daysAsInts.contains(dateTime.getDayOfWeek())){

                    CalendarDay convertedDateTime = CalendarDay.from(dateTime.toDate());

                    FutureDateHelperClass.getInstance().DateCollection7.add(convertedDateTime);
                }
            }
        }

        if(!FutureDateHelperClass.getInstance().DateCollection7.isEmpty()){
            widget.addDecorator(new PastEventDecorator(Color.GRAY, FutureDateHelperClass.getInstance()
                    .DateCollection7));
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

    boolean subIncValidator(long inc, long size){
        if(inc == size){
            return true;
        }else {
            return false;
        }
    }

}












