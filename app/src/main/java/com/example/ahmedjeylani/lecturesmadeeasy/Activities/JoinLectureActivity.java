package com.example.ahmedjeylani.lecturesmadeeasy.Activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedjeylani.lecturesmadeeasy.Models.Lecture;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Student;
import com.example.ahmedjeylani.lecturesmadeeasy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view_id);

        View headerView = navView.getHeaderView(0);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    //User Profile Page
                    case R.id.nav_profileBtn_id:
                        Intent profileIntent = new Intent(JoinLectureActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("userInfo", studentInfo);
                        startActivity(profileIntent);
                        return true;

                    //Sign Out Btn
                    case R.id.nav_signoutBtn_id:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent signoutIntent = new Intent(JoinLectureActivity.this, LoginActivity.class);
                        startActivity(signoutIntent);
                        return true;

                    //Chatroom Page
                    case R.id.nav_professorsBtn_id:
                        Intent professorListIntent = new Intent(JoinLectureActivity.this, ProfessorListActivity.class);
                        professorListIntent.putExtra("studentInfo", studentInfo);
                        startActivity(professorListIntent);
                        return true;

                    //Main page of events
                    case R.id.nav_lecturesBtn_id:
                        //Toast.makeText(EventListActivity.this, "You are already here :D", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(JoinLectureActivity.this, UsersLectures.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_feedBtn_id:
                        Intent feedIntent = new Intent(JoinLectureActivity.this, ProfessorFeedActivity.class);
                        feedIntent.putExtra("userInfo", studentInfo);
                        startActivity(feedIntent);
                        return true;


                    default:
                }
                return JoinLectureActivity.super.onOptionsItemSelected(item);
            }
        });

        CircleImageView navProfileImage = (CircleImageView) headerView.findViewById(R.id.nav_profileImage_id);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_username_id);
        TextView navEmail = (TextView) headerView.findViewById(R.id.nav_email_id);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Navigation Bar
        if(user != null) navEmail.setText(user.getEmail());
        //Picasso.with(this).load(studentInfo.getImageRef()).into(navProfileImage);
        navUsername.setText(studentInfo.getName());


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
