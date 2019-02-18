package com.example.ahmedjeylani.lecturesmadeeasy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmedjeylani.lecturesmadeeasy.Models.Lecture;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Professor;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Student;
import com.example.ahmedjeylani.lecturesmadeeasy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import static com.example.ahmedjeylani.lecturesmadeeasy.SCMethods.*;
import static com.example.ahmedjeylani.lecturesmadeeasy.DatabaseStringReference.*;

public class JoinLectureActivity extends AppCompatActivity {

    private EditText inviteCode;
    private DatabaseReference lectureReference;
    private Lecture lectureInfo;
    private Student studentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_lecture);

        inviteCode = (EditText) findViewById(R.id.invite_code_id);
        Button joinLectureBtn = (Button) findViewById(R.id.join_lecture_btn_id);
        Intent loginIntent = getIntent();
        studentInfo = (Student) loginIntent.getExtras().get("studentInfo");

        lectureReference = FirebaseDatabase.getInstance().getReference().child(LECTURES_REF);

        joinLectureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFieldEmpty(inviteCode)) {
                    Toast.makeText(JoinLectureActivity.this, "Enter Invitation Code", Toast.LENGTH_SHORT).show();
                } else {
                    lectureReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String inviteCodeStr = inviteCode.getText().toString().trim();
                            if(dataSnapshot.hasChild(inviteCodeStr)) {
//                                Iterator i = dataSnapshot.getChildren().iterator();
//
//                                while(i.hasNext()) {
//
//                                }
                                lectureReference.child(inviteCodeStr).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        lectureInfo = dataSnapshot.getValue(Lecture.class);
                                        Intent lectureRoomIntent = new Intent(JoinLectureActivity.this,LectureRoomActivity.class);
                                        lectureRoomIntent.putExtra("userInfo", studentInfo);
                                        lectureRoomIntent.putExtra("lectureInfo",  lectureInfo);
                                        startActivity(lectureRoomIntent);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                Toast.makeText(JoinLectureActivity.this, "This Lecture Does Exist", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(JoinLectureActivity.this, "This Lecture Does Not Exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
}
