package com.example.ahmedjeylani.lecturesmadeeasy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.ahmedjeylani.lecturesmadeeasy.CustomListAdapters.CustomProfessorListAdapter;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Professor;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Student;
import com.example.ahmedjeylani.lecturesmadeeasy.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.ahmedjeylani.lecturesmadeeasy.DatabaseStringReference.*;

import java.util.ArrayList;

public class ProfessorListActivity extends AppCompatActivity {

    private ArrayList<Professor> professorList = new ArrayList<>();
    private Student studentInfo;
    private DatabaseReference professorsRef;
    private ListView professorsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_list);

        professorsListView = (ListView) findViewById(R.id.professorListView);

        Intent intent = getIntent();
        studentInfo = (Student) intent.getExtras().get("studentInfo");

        professorsRef = FirebaseDatabase.getInstance().getReference().child(USER_REF).child(PROFESSOR_REF);

        professorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ctrl alt v Creates the correct data type to store it in
                //Iterable gets all of the children at this level
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //shake hands with each of them and save it as a variable (Child)
                for(DataSnapshot child: children) {
                    Professor professor = child.getValue(Professor.class);
                    professorList.add(professor);
                }

                CustomProfessorListAdapter adapter = new CustomProfessorListAdapter(ProfessorListActivity.this,professorList, studentInfo);
                professorsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
