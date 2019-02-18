package com.example.ahmedjeylani.lecturesmadeeasy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ahmedjeylani.lecturesmadeeasy.CustomListAdapters.CustomChatListAdapter;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.BaseUser;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.ChatData;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Lecture;
import com.example.ahmedjeylani.lecturesmadeeasy.R;
import com.example.ahmedjeylani.lecturesmadeeasy.SCMethods;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import static com.example.ahmedjeylani.lecturesmadeeasy.DatabaseStringReference.*;

public class LectureRoomActivity extends AppCompatActivity {


    private EditText inputMessage;
    private TextView chatConvo;

    private String tempKey;
    private String un;

    private DatabaseReference messageRef, chatRoomRef, chatroomUsersRef, usersLikedMessagesRef, numberOfLikesRef;
    private BaseUser userInfo;
    private Lecture lectureInfo;
    private ChatData pressedChat;

    private ArrayList<ChatData> chatList = new ArrayList<>();
    private ListView customChatList;
    private CustomChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_room);

        Intent intent = getIntent();
        lectureInfo = (Lecture) intent.getExtras().get("lectureInfo");
        String roomName = lectureInfo.getLectureTitle();
        userInfo = (BaseUser) intent.getExtras().get("userInfo");
        un = userInfo.getName();

        customChatList = (ListView) findViewById(R.id.chat_list_id);
        String fullRoomName = roomName + " Lecture";
        setTitle(fullRoomName);

        Button sendMessageBtn = (Button) findViewById(R.id.send_message_button_id);
        inputMessage = (EditText) findViewById(R.id.user_msg_id);

        chatRoomRef = FirebaseDatabase.getInstance().getReference().child(CHATROOMS_REF).child(lectureInfo.getUniqueID());
        chatroomUsersRef = FirebaseDatabase.getInstance().getReference().child(USERS_JOINED_CHATROOMS_REF).child(userInfo.getUniqueID());
        usersLikedMessagesRef = FirebaseDatabase.getInstance().getReference().child(USERS_LIKED_MESSAGES_REF).child(userInfo.getUniqueID()).child(lectureInfo.getUniqueID());


//        customChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                pressedMessage = (ChatData) parent.getItemAtPosition(position);
//                ImageButton likeBtn = (ImageButton) view.findViewById(R.id.like_chat_btn_id);
//                Toast.makeText(LectureRoomActivity.this, "Test", Toast.LENGTH_SHORT).show();
//            }
//        });

        chatRoomRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateChatConvo(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateChatConvo(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                tempKey = chatRoomRef.push().getKey();

                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                SCMethods.addChildAndValue(chatRoomRef.child(tempKey),"uniqueID",userInfo.getUniqueID());
                SCMethods.addChildAndValue(chatRoomRef.child(tempKey),"username",un);
                SCMethods.addChildAndValue(chatRoomRef.child(tempKey),"message",SCMethods.textToString(inputMessage));
                SCMethods.addChildAndValue(chatRoomRef.child(tempKey),"messageTime",currentDateTimeString);
                SCMethods.addChildAndValue(chatRoomRef.child(tempKey),"image",userInfo.getImageRef());

                inputMessage.getText().clear();

                */

                if(!TextUtils.isEmpty(inputMessage.getText().toString())) {
                    //This is for the random keys

                    String currentDateTimeString = SCMethods.getCurrentDataAndTime();
                    //Map<String,Object> map = new HashMap<String, Object>();
                    tempKey = chatRoomRef.push().getKey();
                    //TODO CHECK CODE THAT IS COMMENTED BELOW AND ABOVE!!!
                    //This will put all the previous chats in the map?????
                    //chatRoomRef.updateChildren(map);

                    messageRef = chatRoomRef.child(tempKey);

                    Map<String,Object> map2 = new HashMap<String, Object>();
                    map2.put(MESSAGE_USER_ID_KEY_NAME,userInfo.getUniqueID());
                    map2.put(USERNAME_KEY_NAME,un);
                    map2.put(MESSAGE_KEY_NAME,inputMessage.getText().toString());
                    map2.put(MESSAGE_TIME_KEY_NAME,currentDateTimeString);
                    map2.put(MESSAGE_LIKES_KEY_NAME, "0");
                    map2.put(UNIQUEID_KEY_NAME, tempKey);
                    //map2.put(IMAGE_REF_KEY_NAME,userInfo.getImageRef());


                    messageRef.updateChildren(map2);
                    inputMessage.getText().clear();

                }




            }
        });

    }

//    private void changeItemWhatever(DataSnapshot ds) {
//        ArrayList<ChatData> oldList = chatList;
//        chatList.clear();
//
//        Iterator i = ds.getChildren().iterator();
//
//        while(i.hasNext()) {
//
//            boolean duplicateMessage = false;
//            //i.next gets the first child value then the next child value
//            //i.hasNext Loops till there are no more next child values!
//            String chatLikes = (String) ((DataSnapshot) i.next()).getValue(); //IMG Ref
//            String chatMsg = (String) ((DataSnapshot) i.next()).getValue();
//            String chatDateTime = (String) ((DataSnapshot) i.next()).getValue(); //Date and time
//            String senderID = (String) ((DataSnapshot) i.next()).getValue();
//            String chatUID = (String) ((DataSnapshot) i.next()).getValue();
//            String chatUN = (String) ((DataSnapshot) i.next()).getValue();
//
//            Log.d("D-----------------", "UID - " + chatUID);
//            Log.d("D-----------------", "UN - " + chatUN);
//            Log.d("D-----------------", "MSG - "+ chatMsg);
//            Log.d("D-----------------", "ChatLikes - " + chatLikes);
//            Log.d("D-----------------", "Sender ID - " + senderID);
//            Log.d("D-----------------", "Date - " + chatDateTime);
//            //Log.d("D-----------------", "IMG Ref" + chatImgRef);
//
//
//            ChatData userMessage = new ChatData(chatUID, chatUN, chatMsg,chatDateTime, chatLikes,senderID);
//
//            for(int j = 0; j < oldList.size(); j++) {
//
//                if(oldList.get(j).getUniqueID().equals(userMessage.getUniqueID())) {
//                    oldList.set(j, userMessage);
//                }
//
//
//
//            }
//
//            chatList = oldList;
//
//            CustomChatListAdapter adapter = new CustomChatListAdapter(LectureRoomActivity.this,chatList,un,lectureInfo, userInfo);
//            customChatList.setAdapter(adapter);
//            adapter.notifyDataSetChanged();;
//
//            //This puts the string together into one
//            //chatConvo.append(chatUN+" : "+chatMsg+" \n");
//
//
//
//        }
//
//
//    }

    private void updateChatConvo(DataSnapshot ds) {

        Iterator i = ds.getChildren().iterator();

        while(i.hasNext()) {

            boolean duplicateMessage = false;
            //i.next gets the first child value then the next child value
            //i.hasNext Loops till there are no more next child values!
            String chatLikes = (String) ((DataSnapshot) i.next()).getValue(); //IMG Ref
            String chatMsg = (String) ((DataSnapshot) i.next()).getValue();
            String chatDateTime = (String) ((DataSnapshot) i.next()).getValue(); //Date and time
            String senderID = (String) ((DataSnapshot) i.next()).getValue();
            String chatUID = (String) ((DataSnapshot) i.next()).getValue();
            String chatUN = (String) ((DataSnapshot) i.next()).getValue();

            Log.d("D-----------------", "UID - " + chatUID);
            Log.d("D-----------------", "UN - " + chatUN);
            Log.d("D-----------------", "MSG - "+ chatMsg);
            Log.d("D-----------------", "ChatLikes - " + chatLikes);
            Log.d("D-----------------", "Sender ID - " + senderID);
            Log.d("D-----------------", "Date - " + chatDateTime);
            //Log.d("D-----------------", "IMG Ref" + chatImgRef);


            ChatData userMessage = new ChatData(chatUID, chatUN, chatMsg,chatDateTime, chatLikes,senderID);

            for(int j = 0; j < chatList.size(); j++) {

                if(chatList.get(j).getUniqueID().equals(userMessage.getUniqueID())) {
                    chatList.set(j, userMessage);
                    duplicateMessage = true;
                    adapter.notifyDataSetChanged();
                }
            }

            if(!duplicateMessage)
                chatList.add(userMessage);


            adapter = new CustomChatListAdapter(LectureRoomActivity.this,chatList,un,lectureInfo, userInfo);
            customChatList.setAdapter(adapter);

            //This puts the string together into one
            //chatConvo.append(chatUN+" : "+chatMsg+" \n");



        }
    }
}
