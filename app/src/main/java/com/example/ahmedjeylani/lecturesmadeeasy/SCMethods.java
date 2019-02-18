package com.example.ahmedjeylani.lecturesmadeeasy;

import android.text.TextUtils;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SCMethods {
    //This makes the child of the event and its value for example "Location = Uxbridge"
    public static void addChildAndValue(DatabaseReference ref, String child, String value) {
        ref.getRef().child(child).setValue(value);
    }

    public static boolean isFieldEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }

    public static String getCurrentDataAndTime() {
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy,  HH:mm", Locale.ENGLISH);

        return simpleDateFormat.format(currentDate);
    }
}
