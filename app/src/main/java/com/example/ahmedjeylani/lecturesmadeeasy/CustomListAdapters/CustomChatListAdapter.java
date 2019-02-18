package com.example.ahmedjeylani.lecturesmadeeasy.CustomListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedjeylani.lecturesmadeeasy.Models.BaseUser;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.ChatData;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Lecture;
import com.example.ahmedjeylani.lecturesmadeeasy.R;
import com.example.ahmedjeylani.lecturesmadeeasy.SCMethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.ahmedjeylani.lecturesmadeeasy.DatabaseStringReference.*;

import java.util.ArrayList;

public class CustomChatListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChatData> chatList;
    private String currentUser;
    private static final int CURRENT_USER = 0;
    private static final int OTHER_USER = 1;
    //private ImageButton likeBtn;
    //private TextView chatLikes;
    private DatabaseReference usersLikedMessagesRef ,numberOfLikesRef;
    private Lecture lectureInfo;
    private BaseUser currentUserInfo;
    private boolean processLike;

    public CustomChatListAdapter(Context c, ArrayList<ChatData> cd, String currentUser, Lecture lecture, BaseUser currentUserInfo) {
        this.context = c;
        this.chatList = cd;
        this.currentUser = currentUser;
        this.lectureInfo = lecture;
        this.currentUserInfo = currentUserInfo;
    }


    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public ChatData getItem(int position) {
        return chatList.get(position);
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


        final ChatData singleChatData =  this.getItem(position);
        usersLikedMessagesRef = FirebaseDatabase.getInstance().getReference().child(USERS_LIKED_MESSAGES_REF).child(currentUserInfo.getUniqueID()).child(lectureInfo.getUniqueID());


        if (convertView == null) {

            if(getItemViewType(position) == CURRENT_USER) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_currentuser, parent, false);
            } else if(getItemViewType(position) == OTHER_USER) {

                convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_otheruser, parent, false);
            }

        }

        if(getItemViewType(position) == CURRENT_USER) {
            TextView userMessage = (TextView) convertView.findViewById(R.id.currentuser_chatmsg_id);
            TextView userTime = (TextView) convertView.findViewById(R.id.currentuser_chatdate_id);
            //CircleImageView userProfilePic = (CircleImageView) convertView.findViewById(R.id.currentuser_chatdp_id);
            userMessage.setText(singleChatData.getMessage());
            userTime.setText(singleChatData.getMessageTime());


            //Picasso.with(context).load(singleChatData.getImageRef()).into(userProfilePic);


        } else if(getItemViewType(position) == OTHER_USER) {
            TextView userUN = (TextView) convertView.findViewById(R.id.otheruser_chatusername_id);
            TextView userMessage = (TextView) convertView.findViewById(R.id.otheruser_chatmessage_id);
            TextView userTime = (TextView) convertView.findViewById(R.id.otheruser_chatdate_id);
            //making it final fixed loads of issues
            final TextView chatLikes = (TextView) convertView.findViewById(R.id.number_of_likes_id);
            final ImageButton likeBtn = (ImageButton) convertView.findViewById(R.id.like_chat_btn_id);
            //CircleImageView userProfilePic = (CircleImageView) convertView.findViewById(R.id.otheruser_chatdp_id);

            userUN.setText(singleChatData.getUsername());
            userMessage.setText(singleChatData.getMessage());
            userTime.setText(singleChatData.getMessageTime());
            chatLikes.setText(singleChatData.getChatLikes());

            usersLikedMessagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(singleChatData.getUniqueID())) {
                        likeBtn.setBackgroundResource(R.drawable.ic_thumb_up_liked);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    processLike = true;

                    usersLikedMessagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            numberOfLikesRef = FirebaseDatabase.getInstance().getReference().child(CHATROOMS_REF).child(lectureInfo.getUniqueID()).child(singleChatData.getUniqueID());
                            if(processLike) {

                                if(dataSnapshot.hasChild(singleChatData.getUniqueID())) {
                                    usersLikedMessagesRef.child(singleChatData.getUniqueID()).removeValue();
                                    String likesStr = chatLikes.getText().toString();
                                    int noLikes = Integer.valueOf(likesStr);
                                    noLikes--;
                                    String newNoLikes = ""+ noLikes;
                                    SCMethods.addChildAndValue(numberOfLikesRef,MESSAGE_LIKES_KEY_NAME, newNoLikes);
                                    processLike = false;
                                } else {

                                    likeBtn.setBackgroundResource(R.drawable.ic_thumb_up_liked);
                                    String likesStr = chatLikes.getText().toString();
                                    int noLikes = Integer.valueOf(likesStr);
                                    noLikes++;
                                    String newNoLikes = ""+ noLikes;
                                    chatLikes.setText(newNoLikes);
                                    Toast.makeText(context, chatLikes.getText().toString(), Toast.LENGTH_SHORT).show();
                                    usersLikedMessagesRef.child(singleChatData.getUniqueID()).setValue(singleChatData.getUsername());
                                    numberOfLikesRef.child(MESSAGE_LIKES_KEY_NAME).setValue(newNoLikes);
                                    processLike = false;
                                }

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


//                    SCMethods.addChildAndValue(usersLikedMessagesRef,singleChatData.getUniqueID(),singleChatData.getUsername());
//                    SCMethods.addChildAndValue(numberOfLikesRef,MESSAGE_LIKES_KEY_NAME, newNoLikes);

                }
            });



            //Picasso.with(context).load(singleChatData.getImageRef()).into(userProfilePic);


        }



//        info.setText(singleEventData.getDesc());
//        eventImage.setImageURI(Uri.parse(singleEventData.getImageRef()));



        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {

        ChatData singleChatData = chatList.get(position);

        if(currentUser.equalsIgnoreCase(singleChatData.getUsername())) {
            return CURRENT_USER;
        } else {
            return OTHER_USER;
        }
    }

    //Checking a certain attribute
    @Override
    public int getViewTypeCount() {
        return 2;
    }
}
