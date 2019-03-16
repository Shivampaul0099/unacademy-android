package com.unacademyclone.utility;

public class DigitsUtility {
    public static String getCondensedNumber(int num){
        if(num<1000){
            return num+"";
        }
        else if(num<1000000) {
            return Math.round(num / 1000) + "K";
        }
        else if(num<1000000000) {
            return Math.round(num / 1000000) + "M";
        }
        else{
            return num+"";
        }
    }
}
