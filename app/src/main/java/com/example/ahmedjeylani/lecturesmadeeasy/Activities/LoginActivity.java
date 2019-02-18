package com.example.ahmedjeylani.lecturesmadeeasy.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmedjeylani.lecturesmadeeasy.Models.BaseUser;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Professor;
import com.example.ahmedjeylani.lecturesmadeeasy.Models.Student;
import com.example.ahmedjeylani.lecturesmadeeasy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.ahmedjeylani.lecturesmadeeasy.DatabaseStringReference.*;


public class LoginActivity extends AppCompatActivity {

    private EditText emailTextField,passwordTextField;
    private CheckBox professorCheckbox;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private Student studentData;
    private Professor professorData;
    private DatabaseReference userDatabase;
    private BaseUser userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextField = (EditText) findViewById(R.id.email);
        passwordTextField = (EditText) findViewById(R.id.password);
        professorCheckbox = (CheckBox) findViewById(R.id.profess_checkBox_id);
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        Button signInBtn = (Button) findViewById(R.id.sign_in_button);
        Button registerBtn = (Button) findViewById(R.id.register_button);

        if(fUser != null) {
            FirebaseAuth.getInstance().signOut();
        }


        userDatabase = FirebaseDatabase.getInstance().getReference().child(USER_REF);


        if(fUser != null) {
            //getUserDetails();
        }

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailTextField.getText().toString();
                final String password = passwordTextField.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Fill in Login Details!", Toast.LENGTH_SHORT).show();
                } else {
                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                fUser = FirebaseAuth.getInstance().getCurrentUser();
                                if(fUser == null) {
                                    passwordTextField.getText().clear();
                                    // TEST THIS IF STATEMENT
                                    // This cou
                                    Toast.makeText(LoginActivity.this, "INVESTIGATE", Toast.LENGTH_SHORT).show();

                                }
                                else if(fUser.isEmailVerified()) {

                                    if(professorCheckbox.isChecked()) {
                                        getUserDetails(true, userDatabase.child(PROFESSOR_REF));
                                    } else {
                                        getUserDetails(false, userDatabase.child(STUDENT_REF));
                                    }
                                    // If the userInfo has no internet, double check if fUser is null

                                } else if(!fUser.isEmailVerified()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                                    builder.setTitle("Error")
                                            .setMessage("You haven't verified your email, please do this before continuing. The verification email could take up 20 minutes to send")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    passwordTextField.getText().clear();
                                                    FirebaseAuth.getInstance().signOut();
                                                }
                                            });
                                    builder.show();

                                    // If this else statement is hit INVESTIGATE!!
                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Error")
                                            .setMessage("Unknown Error!, please contact support");
                                    builder.show();

                                }

                            }
                            // This else statement is run when userInfo enters incorrect details
                            else {
                                passwordTextField.getText().clear();
                                Toast.makeText(LoginActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Goes to the register page when the button is pressed
                Intent start = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(start);
            }
        });


    }

    private void getUserDetails(final boolean isProfessor, DatabaseReference userReference) {
        // This gets the current users ID which we use to get the users information from the database
        String userID = fUser.getUid();



        // Get all the values in the users database
        userReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                // Can't Login as Event Organiser Yet
                //Stores all the values in studentData
                if(isProfessor) {
                    userInfo = dataSnapshot.getValue(Professor.class);
                } else {
                    userInfo = dataSnapshot.getValue(Student.class);
                }


                if(userInfo == null && isProfessor) {
                    Toast.makeText(LoginActivity.this, "You are not registered as a professor", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                } else if (userInfo == null && !isProfessor) {
                    Toast.makeText(LoginActivity.this, "You are not registered as a student", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                }
                else {
                    try {
                        if(professorCheckbox.isChecked()) {
                            Intent lectureHomeIntent = new Intent(LoginActivity.this,LecturesHomeActivity.class);
                            lectureHomeIntent.putExtra("professorInfo", userInfo);
                            startActivity(lectureHomeIntent);
                            finish();

                        }else {
                            Log.v("E_VALUE---------------", userInfo.getName());
                            Intent joinLectureIntent = new Intent(LoginActivity.this,JoinLectureActivity.class);
                            joinLectureIntent.putExtra("studentInfo", userInfo);
                            startActivity(joinLectureIntent);
                            finish();
                        }
                    }catch (Exception exception){
                        Log.v("E_VALUE---------------", exception.getMessage());
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Look into adding something here


            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


}

