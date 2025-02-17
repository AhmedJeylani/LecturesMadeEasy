package com.example.ahmedjeylani.lecturesmadeeasy.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedjeylani.lecturesmadeeasy.CustomListAdapters.CustomLectureListAdapter;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Lecture;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Professor;
import com.example.ahmedjeylani.lecturesmadeeasy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ahmedjeylani.lecturesmadeeasy.DatabaseStringReference.*;

public class LecturesHomeActivity extends AppCompatActivity {

    private ArrayList<Lecture> lectureList = new ArrayList<>();
    private ListView lectureListView;
    private FirebaseUser fUser;
    private Professor professorInfo;
    private Lecture pressedLecture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures_home);

        //setSupportActionBar((Toolbar) findViewById(R.id.home_toolbar_id));
        lectureListView = (ListView) findViewById(R.id.lectures_list_id);
        ImageButton addLectureBtn = (ImageButton) findViewById(R.id.imageAddBtn_id);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference lectureRef = databaseReference.child(LECTURES_REF);

        Intent loginIntent = getIntent();
        professorInfo = (Professor) loginIntent.getExtras().get("professorInfo");

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.lectureshome_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_lecturehome);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_lectureshome_view_id);

        View headerView = navView.getHeaderView(0);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    //User Profile Page
                    case R.id.nav_profileBtn_id:
                        Intent profileIntent = new Intent(LecturesHomeActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("userInfo", professorInfo);
                        startActivity(profileIntent);
                        return true;

                    //Sign Out Btn
                    case R.id.nav_signoutBtn_id:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent signoutIntent = new Intent(LecturesHomeActivity.this, LoginActivity.class);
                        startActivity(signoutIntent);
                        return true;

                    //Chatroom Page
                    case R.id.nav_professorsBtn_id:
                        Intent chatroomIntent = new Intent(LecturesHomeActivity.this, ProfessorFeedActivity.class);
                        chatroomIntent.putExtra("userInfo", professorInfo);
                        startActivity(chatroomIntent);
                        return true;

                    //Main page of events
                    case R.id.nav_lecturesBtn_id:
                        //Toast.makeText(EventListActivity.this, "You are already here :D", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LecturesHomeActivity.this, UsersLectures.class);
                        startActivity(intent);
                        return true;
                    default:
                }
                return LecturesHomeActivity.super.onOptionsItemSelected(item);
            }
        });

        CircleImageView navProfileImage = (CircleImageView) headerView.findViewById(R.id.nav_profileImage_id);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_username_id);
        TextView navEmail = (TextView) headerView.findViewById(R.id.nav_email_id);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Navigation Bar
        if(user != null) navEmail.setText(user.getEmail());
        //Picasso.with(this).load(studentInfo.getImageRef()).into(navProfileImage);
        navUsername.setText(professorInfo.getName());


        lectureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ctrl alt v Creates the correct data type to store it in
                //Iterable gets all of the children at this level
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //shake hands with each of them and save it as a variable (Child)
                for(DataSnapshot child: children) {
                    Lecture lecture = child.getValue(Lecture.class);
                    lectureList.add(lecture);
                }
                try {
                    Log.v("E_VALUE",lectureList.get(0).getLectureTitle());
                }catch (Exception e) {
                    Log.e("E_UniSocial", "GOT AN ERROR!",e);
                }

                CustomLectureListAdapter adapter = new CustomLectureListAdapter(LecturesHomeActivity.this,lectureList);
                lectureListView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lectureListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pressedLecture = (Lecture) parent.getItemAtPosition(position);
                selectAction(pressedLecture);


//                Intent lectureInfo = new Intent(LecturesHomeActivity.this,LectureInfoActivity.class);
//                lectureInfo.putExtra("userInfo",pressedLecture);
//                startActivity(lectureInfo);
                // Add back button?? and

            }
        });

        addLectureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(LecturesHomeActivity.this,AddLectureActivity.class);
                addIntent.putExtra("professorInfo",professorInfo);
                startActivity(addIntent);
                finish();
            }
        });
    }

    //Open Gallery or Camera to change profile image
    private void selectAction(final Lecture chosenLecture) {

        String title = "What would you like to do with "+chosenLecture.getLectureTitle() +"?";
        CharSequence[] itemlist ={"Copy Invite Code", "View Lecture Room", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(itemlist, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int userOption) {
                switch (userOption) {
                    case 0:// copy invite code
                        copyToClipboard(chosenLecture);
                        break;
                    case 1:// Edit lecture
                        Intent lectureInfoIntent = new Intent(LecturesHomeActivity.this,LectureInfoActivity.class);
                        lectureInfoIntent.putExtra("professorInfo",professorInfo);
                        lectureInfoIntent.putExtra("chosenLecture", chosenLecture);
                        startActivity(lectureInfoIntent);
                        break;
                    case 2:// Dismiss Dialogue
                        dialog.dismiss();
                    default:
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.setCancelable(true);
        alert.show();
    }

    private void copyToClipboard(Lecture chosenLecture) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(
                "lecture invite code", // Name of what you're copying
                chosenLecture.getUniqueID());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Saved to clip board", Toast.LENGTH_SHORT).show();
    }
}
