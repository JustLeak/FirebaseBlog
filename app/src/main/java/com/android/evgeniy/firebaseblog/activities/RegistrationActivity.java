package com.android.evgeniy.firebaseblog.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.android.evgeniy.firebaseblog.R;
import com.android.evgeniy.firebaseblog.dataaccess.UserDetailsDao;
import com.android.evgeniy.firebaseblog.models.UserDetails;
import com.android.evgeniy.firebaseblog.services.Checker;
import com.android.evgeniy.firebaseblog.services.SearchMap;
import com.android.evgeniy.firebaseblog.services.UserDetailsSetter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private UserDetails userDetails;
    private FirebaseAuth mAuth;
    private String password;

    private EditText mUsername;
    private EditText mPassword;
    private EditText firstName;
    private EditText lastName;
    private EditText age;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mUsername = findViewById(R.id.et_email_reg);
        mPassword = findViewById(R.id.et_password_reg);
        firstName = findViewById(R.id.et_name_reg);
        lastName = findViewById(R.id.et_surname_reg);
        age = findViewById(R.id.et_age_reg);

        userDetails = new UserDetails();

        createAccountButton = findViewById(R.id.btn_create_acc);
        createAccountButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    public void Registration() {
        mAuth.createUserWithEmailAndPassword(userDetails.getEmail(), password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserDetailsDao detailsDao = new UserDetailsDao();
                            detailsDao.setOneByUid(userDetails, mAuth.getUid());
                            SearchMap searchMap = new SearchMap(mAuth.getUid());
                            searchMap.addMapItem(userDetails.getEmail());
                            Snackbar.make(findViewById(android.R.id.content), "registration success.", Snackbar.LENGTH_SHORT).show();

                            mAuth.signInWithEmailAndPassword(userDetails.getEmail(), password);
                            startUserNotesActivity();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "registration failed..", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startUserNotesActivity() {
        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Checker checker = new Checker();
        HashMap<String, String> inputs = getInputs();

        if (checker.isCorrectInputs(inputs)) {
            password = inputs.get("password");
            userDetails = UserDetailsSetter.set(inputs);
            Registration();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Please, fill in all fields.", Snackbar.LENGTH_SHORT).show();
            addErrorsToFields(checker.getResultMap());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private HashMap<String, String> getInputs() {
        HashMap<String, String> inputs = new HashMap<>();
        inputs.put("email", mUsername.getText().toString().trim());
        inputs.put("password", mPassword.getText().toString().trim());
        inputs.put("firstName", firstName.getText().toString().trim());
        inputs.put("lastName", lastName.getText().toString().trim());
        inputs.put("age", age.getText().toString().trim());

        RadioButton maleRadio = findViewById(R.id.rb_male_reg);
        if (maleRadio.isChecked())
            inputs.put("gender", "Male");
        else inputs.put("gender", "Female");

        return inputs;
    }

    private void addErrorsToFields(HashMap<String, Boolean> inputResults) {
        for (String key : inputResults.keySet()) {
            if (!inputResults.get(key)) {
                {
                    switch (key) {
                        case "email": {
                            mUsername.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                        }
                        case "password": {
                            mPassword.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                        }
                        case "firstName": {
                            firstName.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                        }
                        case "lastName": {
                            lastName.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                        }
                        case "age": {
                            age.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                        }
                    }
                }
            }
        }
    }
}
