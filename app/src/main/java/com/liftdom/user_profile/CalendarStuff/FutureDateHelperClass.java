package com.liftdom.user_profile.CalendarStuff;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

/**
 * Created by Brodin on 3/10/2017.
 */

public class FutureDateHelperClass {

    // Singleton boilerplate
    private static FutureDateHelperClass controller;
    static FutureDateHelperClass getInstance() {
        if (controller == null) {
            controller = new FutureDateHelperClass();
        }
        return controller;
    }


    //TODO: To allow for future custom calendar dates, we can store those later and check with them first.
    //TODO: Even will probably store them in Firebase


    // Could do seven arraylists of possible dates.
    // And then maybe just one arraylist of arraylists that's index based

    public ArrayList<CalendarDay> DateCollection1 = new ArrayList<>();
    public ArrayList<CalendarDay> DateCollection2 = new ArrayList<>();
    public ArrayList<CalendarDay> DateCollection3 = new ArrayList<>();
    public ArrayList<CalendarDay> DateCollection4 = new ArrayList<>();
    public ArrayList<CalendarDay> DateCollection5 = new ArrayList<>();
    public ArrayList<CalendarDay> DateCollection6 = new ArrayList<>();
    public ArrayList<CalendarDay> DateCollection7 = new ArrayList<>();

    public ArrayList<ArrayList<String>> DataCollection = new ArrayList<>();



}









