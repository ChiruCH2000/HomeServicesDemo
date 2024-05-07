package com.example.homeservicesdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    public static final String CHANNEL_ID = "vr";
    public static final String CHANNEL_NAME = "vr";
    public static final String CHANNEL_DESC = "vr Alert";

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final int REQUEST_CODE = 102;
    public static int RESULT_LOAD_IMAGE = 103;
    public static int REQUEST_NOTIFICATION = 104;

    public final static String expression = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*public static boolean itemInCart(Context context, String psid, String sid) {
        boolean isCart = false;
        List<CartBean> cartBeanList = new ArrayList<>();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        cartBeanList = databaseHelper.getAllItems();
        for (int i = 0; i < cartBeanList.size(); i++) {
            if (cartBeanList.get(i).getPsid().equalsIgnoreCase(psid) && cartBeanList.get(i).getSizeId().equalsIgnoreCase(sid)) {
                isCart = true;
                break;
            }
        }
        return isCart;
    }*/

    /*public static boolean itemInCart(Context context, String pcode) {
        boolean isCart = false;
        List<CartBean> cartBeanList = new ArrayList<>();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        cartBeanList = databaseHelper.getAllItems();
        for (int i = 0; i < cartBeanList.size(); i++) {
            if (cartBeanList.get(i).getpCode().equalsIgnoreCase(pcode)) {
                isCart = true;
                break;
            }
        }
        return isCart;
    }*/

    public static boolean isValidEmail(String trimEmail) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(trimEmail);
        return matcher.matches();
    }

    public static String getVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.trim().length() <= 0) {
            return "";
        }
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(videoUrl);
        try {
            if (matcher.find())
                return matcher.group();
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static byte[] getByteArrayFromFile(File file) {
        byte[] fileBytes = new byte[(int) file.length()];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(fileBytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fileBytes;
    }

    public static String get12HFormat(int hour, int minutes) {
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String min = "";
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);

        // Append in a StringBuilder
        return String.valueOf(hour) + ':' + min + " " + timeSet;
    }

    public static String changeDateTimeFormat(String date_time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");

        String[] scanDateTime = date_time.split("T");
        String scanDate = scanDateTime[0];
        String[] scanTime = scanDateTime[1].split(":");
        String hour = scanTime[0];
        String minute = scanTime[1];

        try {
            Date date = simpleDateFormat.parse(scanDate + " " + hour + ":" + minute);
            System.out.println("Final: " + simpleDateFormat2.format(date));
            String finalTime = get12HFormat(Integer.parseInt(hour), Integer.parseInt(minute));
            if (date != null) {
                return simpleDateFormat2.format(date) + " " + finalTime;
            } else {
                return date_time.replace("T", " ");
            }
        } catch (ParseException e) {
            return date_time.replace("T", " ");
        }
    }

    public static int convertDpToPixels(float dp, Context context) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }
}
