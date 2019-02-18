package com.example.ahmedjeylani.lecturesmadeeasy.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmedjeylani.lecturesmadeeasy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.ahmedjeylani.lecturesmadeeasy.DatabaseStringReference.*;
import static com.example.ahmedjeylani.lecturesmadeeasy.SCMethods.*;


public class RegisterActivity extends AppCompatActivity {


    private EditText nameTextFIeld, emailTextField, passwordTextField, reenterPasswordTextField, studentIdTextField;
    private CheckBox studentCheckBox;
    private Button signUpBtn;

    private FirebaseAuth fAuth;
    private DatabaseReference userReference, studentReference, professorReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameTextFIeld = (EditText) findViewById(R.id.name_field_id);
        emailTextField = (EditText) findViewById(R.id.email_field_id);
        passwordTextField = (EditText) findViewById(R.id.password_field_id);
        reenterPasswordTextField = (EditText) findViewById(R.id.reenter_password_field_id);
        studentIdTextField = (EditText) findViewById(R.id.studentId_field_id);

        studentCheckBox = (CheckBox) findViewById(R.id.student_checkBox_id);

        signUpBtn = (Button) findViewById(R.id.signUp_button_id);

        userReference = FirebaseDatabase.getInstance().getReference().child(USER_REF);
        studentReference = userReference.child(STUDENT_REF);
        professorReference = userReference.child(PROFESSOR_REF);

        fAuth = FirebaseAuth.getInstance();

        studentCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {

                    studentIdTextField.setVisibility(View.VISIBLE);
                    studentIdTextField.setEnabled(true);
                }
                else {
                    studentIdTextField.setVisibility(View.INVISIBLE);
                    studentIdTextField.setEnabled(false);

                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(studentCheckBox.isChecked()) {
                    if(TextUtils.isEmpty(studentIdTextField.getText().toString())) {

                    } else {
                        registerUser(studentReference);
                    }
                } else {
                    registerUser(professorReference);
                }

            }
        });

    }

    private void registerUser(final DatabaseReference registerReference) {

        if(isFieldEmpty(nameTextFIeld) || isFieldEmpty(emailTextField) || isFieldEmpty(passwordTextField) || isFieldEmpty(reenterPasswordTextField)) {
            Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
        } else if(!passwordTextField.getText().toString().equals(reenterPasswordTextField.getText().toString())) {
            Toast.makeText(this, "The Password doesn't match", Toast.LENGTH_SHORT).show();
        } else {
            String email = emailTextField.getText().toString();
            String password = passwordTextField.getText().toString();
            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isComplete()) {
                        if(task.isSuccessful()) {
                            currentUser = fAuth.getCurrentUser();
                            DatabaseReference uniqueIdRef = registerReference.child(currentUser.getUid());
                            addChildAndValue(uniqueIdRef, UNIQUEID_KEY_NAME, currentUser.getUid());
                            addChildAndValue(uniqueIdRef, NAME_KEY_NAME, nameTextFIeld.getText().toString());
                            addChildAndValue(uniqueIdRef, IMAGE_REF_KEY_NAME, "");
                            if(studentCheckBox.isChecked()) {
                                addChildAndValue(uniqueIdRef, STUDENTID_KEY_NAME, studentIdTextField.getText().toString());
                                addChildAndValue(uniqueIdRef, USERTYPE_KEY_NAME, "student");
                            } else {
                                addChildAndValue(uniqueIdRef,USERTYPE_KEY_NAME, "professor");
                                addChildAndValue(uniqueIdRef, BIO_KEY_NAME,"");
                            }

                            if (currentUser != null) {
                                //Takes about 20-30 mins to send
                                currentUser.sendEmailVerification().addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Email Verification Sent", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(RegisterActivity.this,"Email Verification hasn't sent, please contact Support",Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });
                            }


                            Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            //push through register data?
                            startActivity(intent);
                            RegisterActivity.this.finish();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Issue Creating User", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Issue Completing Task", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}
