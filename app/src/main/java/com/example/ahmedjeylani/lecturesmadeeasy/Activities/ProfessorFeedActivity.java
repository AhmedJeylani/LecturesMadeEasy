package com.example.ahmedjeylani.lecturesmadeeasy.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ahmedjeylani.lecturesmadeeasy.CustomListAdapters.CustomFeedListAdapter;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.BaseUser;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Feed;
import com.example.ahmedjeylani.lecturesmadeeasy.R;
import static com.example.ahmedjeylani.lecturesmadeeasy.DatabaseStringReference.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfessorFeedActivity extends AppCompatActivity {


    private ArrayList<Feed> feedList = new ArrayList<>();
    private ArrayList<String> studentFollowing = new ArrayList<>();
    private ListView feedListView;
    private BaseUser userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_feed);

        feedListView = (ListView) findViewById(R.id.feed_listview_id);
        ImageButton addBtn = (ImageButton) findViewById(R.id.imageAddBtn_id);

        Intent intent = getIntent();
        userInfo = (BaseUser) intent.getExtras().get("userInfo");

        if(userInfo.getUserType().equals("student"))
            addBtn.setVisibility(View.INVISIBLE);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        DatabaseReference studentFollowingRef = FirebaseDatabase.getInstance().getReference().child(USERS_FOLLOWED_PROFESSORS_REF).child(userInfo.getUniqueID());
        studentFollowingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    studentFollowing.add((ds).getKey());

                }

                for (String professorId : studentFollowing) {
                    DatabaseReference feedRef = FirebaseDatabase.getInstance().getReference().child(PROFESSORS_POSTS_REF).child(professorId);
                    feedRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                            //shake hands with each of them and save it as a variable (Child)
                            for(DataSnapshot child: children) {
                                Feed feed = child.getValue(Feed.class);
                                feedList.add(feed);
                            }

                            CustomFeedListAdapter adapter = new CustomFeedListAdapter(ProfessorFeedActivity.this,feedList);
                            feedListView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Feed pressedFeed = (Feed) parent.getItemAtPosition(position);
                copyToClipboard(pressedFeed);

                
            }
        });




    }

    private void copyToClipboard(Feed chosenFeed) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(
                "lecture invite code", // Name of what you're copying
                chosenFeed.getLectureID());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Lecture Join Code Copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}
