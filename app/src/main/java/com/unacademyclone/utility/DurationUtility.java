package com.unacademyclone.utility;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DurationUtility {

    public static String getTimerFromSeconds(int duration){
        duration = duration*1000;
        String time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
        return time;
    }

    public static String getTimeAgo(String time_str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try{
            long time = sdf.parse(time_str).getTime();
            long now = System.currentTimeMillis();

            CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return ago.toString();
        }
        catch (Exception e){
            return "";
        }
    }


    public static String getDateFromZulu(String str_date){
//        String string = "2013-03-05T18:05:05Z";
        try{
            String defaultTimezone = TimeZone.getDefault().getID();
            Date date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")).parse(str_date.replaceAll("Z$", "+0000"));
            return (new SimpleDateFormat("MMM dd hh:mm aaa")).format(date);
        }
        catch (Exception e){
            return "";
        }
    }

    public static String getMonthDayFromAZulu(String str_date, boolean secondsInDouble){
//        String string = "2013-03-05T18:05:05Z";
//        String string = "2019-01-23T07:17:36.360Z";
        String pattern = secondsInDouble? "yyyy-MM-dd'T'HH:mm:ss.sssZ" : "yyyy-MM-dd'T'HH:mm:ssZ";

        try{
            String defaultTimezone = TimeZone.getDefault().getID();
            Date date = (new SimpleDateFormat(pattern)).parse(str_date.replaceAll("Z$", "+0000"));
            return (new SimpleDateFormat("MMM dd")).format(date);
        }
        catch (Exception e){
            return "";
        }
    }

    public static String getTimeFromZulu(String str_date, boolean secondsInDouble){
//        String string = "2013-03-05T18:05:05Z";
//        String string = "2019-01-23T07:17:36.360Z";
        String pattern = secondsInDouble? "yyyy-MM-dd'T'HH:mm:ss.sssZ" : "yyyy-MM-dd'T'HH:mm:ssZ";
        try{
            String defaultTimezone = TimeZone.getDefault().getID();
            Date date = (new SimpleDateFormat(pattern)).parse(str_date.replaceAll("Z$", "+0000"));
            return (new SimpleDateFormat(" hh:mm aaa")).format(date);
        }
        catch (Exception e){
            return "";
        }
    }
}
