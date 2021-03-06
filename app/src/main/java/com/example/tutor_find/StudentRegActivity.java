package com.example.tutor_find;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Bundle;

public class StudentRegActivity extends AppCompatActivity {

    MaterialEditText email,password,FullName,ContactNo;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_reg);

        getSupportActionBar().setTitle("Register");

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
        FullName = findViewById(R.id.FullName);
        ContactNo = findViewById(R.id.ContactNo);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_ContactNo = ContactNo.getText().toString();
                String txt_FullName = FullName.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(StudentRegActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(StudentRegActivity.this, "Password is too small", Toast.LENGTH_SHORT).show();
                } else if (txt_ContactNo.length() < 11) {
                    Toast.makeText(StudentRegActivity.this, "Please enter valid contact number", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_email, txt_password, txt_ContactNo, txt_FullName);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.backmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.backButton)
        {
            startActivity(new Intent(StudentRegActivity.this, RegisterActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void register(final String email, final String password, final String contactno, final String fullname){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    // email verification part
                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(StudentRegActivity.this, "Registration successful. Please check your email.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(StudentRegActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                    HashMap<String , String> hashMap = new HashMap<>();
                    hashMap.put("name", fullname);
                    hashMap.put("email", email);
                    hashMap.put("password", password);
                    hashMap.put("number", contactno);
                    hashMap.put("userId",userid);
                    hashMap.put("userType", "Student");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(StudentRegActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
                else if(task.isCanceled()){
                    Toast.makeText(StudentRegActivity.this, "You cannot Register with this Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
