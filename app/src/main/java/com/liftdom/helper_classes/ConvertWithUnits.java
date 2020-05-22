package com.liftdom.helper_classes;

/**
 * Created by Brodin on 5/22/2020.
 */
public class ConvertWithUnits {

    private boolean isTemplateImperial;
    private boolean isUserImperial;
    private double numberDouble;
    private int numberInt;
    boolean round;
    boolean isDouble;
    boolean isInt;
    private double number;

    public ConvertWithUnits(double nmbr, boolean iTI, boolean iUI, boolean rnd){
        numberDouble = nmbr;
        isTemplateImperial = iTI;
        isUserImperial = iUI;
        round = rnd;
        isDouble = true;
        number = nmbr;

        processNumber();
    }

    public ConvertWithUnits(int nmbr, boolean iTI, boolean iUI, boolean rnd){
        numberInt = nmbr;
        isTemplateImperial = iTI;
        isUserImperial = iUI;
        round = rnd;
        isInt = true;
        number = nmbr;

        processNumber();
    }

    public ConvertWithUnits(String nmbr, boolean iTI, boolean iUI, boolean rnd){
        number = Double.parseDouble(nmbr);
        isTemplateImperial = iTI;
        isUserImperial = iUI;
        round = rnd;

        processNumber();
    }

    private void processNumber(){
        if(isUserImperial && !isTemplateImperial){
            // user is lbs, template is kgs
            // metricToImperial
            double dNew = number * 2.2046;
            int intNew;
            if(round){
                intNew = (int) (5 * (Math.round(dNew / 5)));
            }else{
                intNew = (int) Math.round(dNew);
            }
            number = intNew;
        }else if(!isUserImperial && isTemplateImperial){
            // user is kgs, template is lbs
            // imperialToMetric
            double dNew = number / 2.2046;
            int intNew;
            if(round){
                intNew = (int) (5 * (Math.round(dNew / 5)));
            }else{
                intNew = (int) Math.round(dNew);
            }
            number = intNew;
        }else{
            if(isUserImperial){
                if(round){
                    number = (int) (5 * (Math.round(number / 5)));
                }
            }
        }
    }

    public double getNumber(){
        return number;
    }

    public double getNumberDouble() {
        return numberDouble;
    }

    public int getNumberInt() {
        return numberInt;
    }
}
