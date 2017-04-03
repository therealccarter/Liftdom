package com.liftdom.liftdom.utils;

/**
 * Created by Brodin on 4/3/2017.
 */

public class PlateRounderClass {

    int originalWeight;
    int[] commonIntsArray = {
            5, 10, 25, 35, 45, 55, 65, 95, 115, 135, 145,
            155, 185, 205, 225, 235, 245, 275, 295, 315, 325,
            335, 365, 385, 405, 415, 425, 455, 475, 495, 505,
            515, 545, 565, 585, 595, 605, 635, 655, 675, 685,
            695, 725, 745, 765, 775, 785, 815, 835, 855, 865,
            875, 905, 925, 945, 955, 965, 995, 1015, 1035, 1045,
            1055, 1085, 1105, 1125, 1135, 1145, 1175, 1195, 1215
    };


    public PlateRounderClass(int weight){
        originalWeight = weight;
    }

    public int getNewWeight(){
        int newWeight = 0;

        int iDifference = Math.abs(commonIntsArray[0] - originalWeight);

        for(int i = 0; i < commonIntsArray.length; i++){
            int difference = Math.abs(commonIntsArray[i] - originalWeight);
            if(difference <  iDifference){
                newWeight = commonIntsArray[i];
            }
        }

        return newWeight;
    }

}
