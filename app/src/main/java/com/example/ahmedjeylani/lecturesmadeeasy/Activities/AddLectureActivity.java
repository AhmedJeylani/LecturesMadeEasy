package com.example.ahmedjeylani.lecturesmadeeasy.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedjeylani.lecturesmadeeasy.LectureStatus;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Professor;
import com.example.ahmedjeylani.lecturesmadeeasy.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static com.example.ahmedjeylani.lecturesmadeeasy.DatabaseStringReference.*;
import static com.example.ahmedjeylani.lecturesmadeeasy.SCMethods.addChildAndValue;

public class AddLectureActivity extends AppCompatActivity {

    private EditText lectureTitle, lectureRoom, courseName;
    private TextView date;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String strDate = "";
    private DatabaseReference lecturesDatabase;
    private Professor professorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);

        lectureTitle = (EditText) findViewById(R.id.lecture_title_field_id);
        lectureRoom = (EditText) findViewById(R.id.lecture_room_field_id);
        courseName = (EditText) findViewById(R.id.course_field_id);

        date = (TextView) findViewById(R.id.date_field_id);

        final Button addLectureBtn = (Button) findViewById(R.id.addLecture_button_id);

        Intent lectureHomeIntent = getIntent();
        professorInfo = (Professor) lectureHomeIntent.getExtras().get("professorInfo");

        lecturesDatabase = FirebaseDatabase.getInstance().getReference().child(LECTURES_REF);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddLectureActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String strMonth;
                if(month < 10) {
                    strMonth = "0" + month;
                } else {
                    strMonth = String.valueOf(month);
                }

                String strDate = dayOfMonth + "/" + strMonth + "/" + year;
                date.setText(strDate);
            }
        };

        addLectureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(lectureTitle.getText().toString()) || TextUtils.isEmpty(lectureRoom.getText().toString()) || TextUtils.isEmpty(courseName.getText().toString()) || date.getText().equals("Set Date")) {
                    Toast.makeText(AddLectureActivity.this, "Fill In All Details", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference lectureRoomRef = lecturesDatabase.push();
                    addChildAndValue(lectureRoomRef, LECTURE_ROOM_KEY_NAME,lectureRoom.getText().toString());
                    addChildAndValue(lectureRoomRef, LECTURE_TITLE_KEY_NAME, lectureTitle.getText().toString());
                    addChildAndValue(lectureRoomRef, COURSE_KEY_NAME, courseName.getText().toString());
                    addChildAndValue(lectureRoomRef, LECTURE_DATE_KEY_NAME, date.getText().toString());
                    addChildAndValue(lectureRoomRef, LECTURE_STATUS_KEY_NAME, LectureStatus.Closed);
                    addChildAndValue(lectureRoomRef, UNIQUEID_KEY_NAME, lectureRoomRef.getKey());
                    addChildAndValue(lectureRoomRef, PROFESSOR_KEY_NAME, professorInfo.getName());
                    Intent addIntent = new Intent(AddLectureActivity.this,LecturesHomeActivity.class);
                    addIntent.putExtra("professorInfo",professorInfo);
                    startActivity(addIntent);
                    finish();
                }
            }
        });

    }
}
