package com.example.ahmedjeylani.lecturesmadeeasy.CustomListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ahmedjeylani.lecturesmadeeasy.Models.Lecture;
import com.example.ahmedjeylani.lecturesmadeeasy.R;

import java.util.ArrayList;

public class CustomLectureListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Lecture> lectureList;

    public CustomLectureListAdapter(Context c, ArrayList<Lecture> list) {
        this.context = c;
        this.lectureList = list;
    }


    @Override
    public int getCount() {
        return lectureList.size();
    }

    @Override
    public Object getItem(int position) {
        return lectureList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_lecture_row, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.lecture_title_id);
        TextView room = (TextView) convertView.findViewById(R.id.lecture_room_id);
        TextView date = (TextView) convertView.findViewById(R.id.lecture_date_added_id);
        TextView courseName = (TextView) convertView.findViewById(R.id.feed_text_id);

        final Lecture singleLecture = (Lecture) this.getItem(position);

        title.setText(singleLecture.getLectureTitle());
        room.setText(singleLecture.getLectureRoom());
        date.setText(singleLecture.getDate());
        courseName.setText(singleLecture.getCourseName());

        //notifyDataSetChanged();

        return convertView;
    }
}
