package com.example.ahmedjeylani.lecturesmadeeasy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedjeylani.lecturesmadeeasy.LectureStatus;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Lecture;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Professor;
import com.example.ahmedjeylani.lecturesmadeeasy.R;
import com.example.ahmedjeylani.lecturesmadeeasy.SCMethods;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.example.ahmedjeylani.lecturesmadeeasy.DatabaseStringReference.*;
public class LectureInfoActivity extends AppCompatActivity {


    private Professor professorInfo;
    private Lecture lectureInfo;
    //private DatabaseReference lecturePostRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_info);

        TextView lectureTitleTV = (TextView) findViewById(R.id.lecture_title_view_id);
        TextView lectureRoomTV = (TextView) findViewById(R.id.lecture_room_view_id);
        TextView lectureInvitationCodeTV = (TextView) findViewById(R.id.lecture_invitation_view_id);
        TextView courseNameTV = (TextView) findViewById(R.id.course_view_id);
        TextView dateTV = (TextView) findViewById(R.id.date_view_id);
        TextView lectureStatusTV = (TextView) findViewById(R.id.lecture_status_view_id);

        Intent lectureHomeIntent = getIntent();
        professorInfo = (Professor) lectureHomeIntent.getExtras().get("professorInfo");
        lectureInfo = (Lecture) lectureHomeIntent.getExtras().get("chosenLecture");



        lectureTitleTV.setText(lectureInfo.getLectureTitle());
        lectureRoomTV.setText(lectureInfo.getLectureRoom());
        lectureInvitationCodeTV.setText(lectureInfo.getUniqueID());
        courseNameTV.setText(lectureInfo.getCourseName());
        dateTV.setText(lectureInfo.getDate());
        lectureStatusTV.setText(lectureInfo.getLectureStatus());

        Button startLectureBtn = (Button) findViewById(R.id.startLecture_button_id);
        Button postLectureBtn = (Button) findViewById(R.id.postlecture_button_id);

        startLectureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lectureInfo.setLectureStatus(LectureStatus.Open);
                DatabaseReference lectureRoomRef = FirebaseDatabase.getInstance().getReference().child(LECTURES_REF).child(lectureInfo.getUniqueID());
                SCMethods.addChildAndValue(lectureRoomRef, LECTURE_STATUS_KEY_NAME, LectureStatus.Open);
                Intent lectureRoomIntent = new Intent(LectureInfoActivity.this,LectureRoomActivity.class);
                lectureRoomIntent.putExtra("userInfo", professorInfo);
                lectureRoomIntent.putExtra("lectureInfo",  lectureInfo);
                startActivity(lectureRoomIntent);
                finish();
            }
        });

        postLectureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference lecturePostRef = FirebaseDatabase.getInstance().getReference().child(PROFESSORS_POSTS_REF).child(professorInfo.getUniqueID()).push();
                String postKey = lecturePostRef.getKey();
                String postText = "The Lecture " + lectureInfo.getLectureTitle()+ " will be starting at " + lectureInfo.getDate() + " please copy the lecture invitation code and join the lecture";
                SCMethods.addChildAndValue(lecturePostRef, UNIQUEID_KEY_NAME, postKey);
                SCMethods.addChildAndValue(lecturePostRef, DATE_KEY_NAME, SCMethods.getCurrentDataAndTime());
                SCMethods.addChildAndValue(lecturePostRef, POST_PROFESSOR_ID_KEY_NAME, professorInfo.getUniqueID());
                SCMethods.addChildAndValue(lecturePostRef, POST_PROFESSOR_NAME_KEY_NAME, professorInfo.getName());
                SCMethods.addChildAndValue(lecturePostRef, POST_LECTURE_ID_KEY_NAME, lectureInfo.getUniqueID());
                SCMethods.addChildAndValue(lecturePostRef, POST_TEXT_KEY_NAME, postText);
                Toast.makeText(LectureInfoActivity.this, "Lecture has been posted on your feed!", Toast.LENGTH_SHORT).show();


            }
        });

    }
}
