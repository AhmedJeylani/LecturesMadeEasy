package com.example.ahmedjeylani.lecturesmadeeasy.CustomListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ahmedjeylani.lecturesmadeeasy.Models.Feed;
import com.example.ahmedjeylani.lecturesmadeeasy.R;

import java.util.ArrayList;

public class CustomFeedListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Feed> feedList;

    public CustomFeedListAdapter(Context c, ArrayList<Feed> list) {
        this.context = c;
        this.feedList = list;
    }


    @Override
    public int getCount() {
        return feedList.size();
    }

    @Override
    public Object getItem(int position) {
        return feedList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_feed_row, parent, false);
        }

        TextView professorName = (TextView) convertView.findViewById(R.id.professor_name_feed_id);
        TextView feedText = (TextView) convertView.findViewById(R.id.feed_text_id);
        TextView date = (TextView) convertView.findViewById(R.id.feed_upload_date_id);
        TextView lectureCode = (TextView) convertView.findViewById(R.id.lecture_code_id);

        final Feed singleFeed = (Feed) this.getItem(position);

        professorName.setText(singleFeed.getProfessorName());
        feedText.setText(singleFeed.getPostText());
        date.setText(singleFeed.getDate());
        lectureCode.setText("Lecture Join Code: " + singleFeed.getLectureID());

        notifyDataSetChanged();

        return convertView;
    }
}

