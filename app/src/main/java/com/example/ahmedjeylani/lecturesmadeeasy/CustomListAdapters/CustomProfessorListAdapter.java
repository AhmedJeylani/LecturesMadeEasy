package com.example.ahmedjeylani.lecturesmadeeasy.CustomListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.ahmedjeylani.lecturesmadeeasy.Models.Professor;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Student;
import com.example.ahmedjeylani.lecturesmadeeasy.R;
import static com.example.ahmedjeylani.lecturesmadeeasy.DatabaseStringReference.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomProfessorListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Professor> professorsList;
    private DatabaseReference followingRef;
    private Student studentInfo;

    public CustomProfessorListAdapter(Context c, ArrayList<Professor> list, Student studentInfo) {
        this.context = c;
        this.professorsList = list;
        this.studentInfo = studentInfo;
    }
    @Override
    public int getCount() {
        return professorsList.size();
    }

    @Override
    public Object getItem(int position) {
        return professorsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //This controls how the strings that were passed in are laid out
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //inflate = prepare or get ready for rendering
        //context = background information
        //this is equal to one custom row(view)
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_professor_row, parent, false);
        }

        TextView professorName = (TextView) convertView.findViewById(R.id.professorlist_name_id);
        CircleImageView profileImage = (CircleImageView) convertView.findViewById(R.id.professor_image_id);
        final Button followBtn = (Button) convertView.findViewById(R.id.follow_professor_btn_id);


        final Professor singleProfessor = (Professor) this.getItem(position);


        professorName.setText(singleProfessor.getName());

        followingRef = FirebaseDatabase.getInstance().getReference().child(USERS_FOLLOWED_PROFESSORS_REF).child(studentInfo.getUniqueID());

        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(singleProfessor.getUniqueID())) {
                    followBtn.setText("Following");
                    followBtn.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(singleProfessor.getUniqueID())) {

                        } else {
                            followBtn.setText("Following");
                            followBtn.setEnabled(false);
                            followingRef.child(singleProfessor.getUniqueID()).setValue(singleProfessor.getName());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        notifyDataSetChanged();

        return convertView;
    }
}
