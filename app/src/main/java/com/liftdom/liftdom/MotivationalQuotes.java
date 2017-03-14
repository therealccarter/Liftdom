package com.liftdom.liftdom;

import java.util.ArrayList;


/**
 * Created by Brodin on 3/14/2017.
 */

public class MotivationalQuotes {
    ArrayList<String> generalQuotes = new ArrayList<>();

    public String[] getQuote(){
        String[] quoteArray = new String[2];

        int random = (int)(Math.random() * generalQuotes.size() + 1);

        String quoteUnSplit = generalQuotes.get(random);

        String delims = "[-]";

        quoteArray = quoteUnSplit.split(delims);

        return quoteArray;
    }

    public MotivationalQuotes(){
        String m1 = "Lift hard, live easy. - Brodin";
        generalQuotes.add(m1);
    }


}
