package com.example.magic_emporium.magic_emporiumDriver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.magic_emporium.R;
import com.example.magic_emporium.magicDataBase.MagicEmporiumDatabase;
import com.example.magic_emporium.magicDataBase.User;
import com.example.magic_emporium.magicDataBase.UserDao;

public class AccountSettingsActivity extends AppCompatActivity {
    public static final String USER_ID_KEY = "com.example.magic_emporium.magic_emporiumDriver.userId";
    /*
    not yet implemented
     */

    private Button back;
    private Button cancelInput;
    private Button changeName;
    private Button changePassword;
    private Button deleteAccount;

    private Button deleteSubmit;
    private Button nameSubmit;
    private Button passwordSubmit;

    private EditText inputField;
    private TextView instructions;
    User user;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        wireUp();
        wireButtons();
    }

    private void wireUp(){
        userDao = Room.databaseBuilder(this, MagicEmporiumDatabase.class,MagicEmporiumDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .userDao();
        user = userDao.findByUserId(this.getIntent().getIntExtra(USER_ID_KEY,-1));

        back = findViewById(R.id.backFromAccountSettings);
        cancelInput=findViewById(R.id.backFromAccountInputConfirm);
        changeName=findViewById(R.id.userChangeNameButton);
        changePassword=findViewById(R.id.userChangePassword);
        deleteAccount=findViewById(R.id.userDeleteAccountButton);

        deleteSubmit=findViewById(R.id.userDeleteConfirm);
        nameSubmit=findViewById(R.id.userChangeNameConfirm);
        passwordSubmit=findViewById(R.id.userChangePasswordConfirm);

        inputField=findViewById(R.id.accountSettingsInputField);
        instructions=findViewById(R.id.inputInstructionsField);

    }
    private void wireButtons(){
        back.setOnClickListener(v -> {
            Intent intent = HomeScreenActivity.homeScreenIntent(AccountSettingsActivity.this,user.getUid());
            startActivity(intent);
        });
        cancelInput.setOnClickListener(v->{
            baseView();
            inputField.setText("");
        });
        changePassword.setOnClickListener(v->{
            changeView(1);
        });
        changeName.setOnClickListener(v->{
            changeView(2);
        });
        deleteAccount.setOnClickListener(v->{
            changeView(3);
        });



        deleteSubmit.setOnClickListener(v->{
            if(inputField.getText().toString().equals("delete")){
                userDao.delete(user);
                baseView();
                startActivity(LoginActivity.loginScreenIntent(AccountSettingsActivity.this));
            }
        });
        nameSubmit.setOnClickListener(v->{
            if(!inputField.getText().toString().equals("")){
                user.setUserName(inputField.getText().toString());
                userDao.update(user);
                baseView();
            }
        });
        passwordSubmit.setOnClickListener(v->{
            if(!inputField.getText().toString().equals("")){
                user.setPassword(inputField.getText().toString());
                userDao.update(user);
                baseView();
            }
        });

    }
    private void baseView(){
        cancelInput.setVisibility(View.GONE);
        inputField.setVisibility(View.GONE);
        deleteSubmit.setVisibility(View.GONE);
        nameSubmit.setVisibility(View.GONE);
        passwordSubmit.setVisibility(View.GONE);
        instructions.setVisibility(View.GONE);

        changeName.setVisibility(View.VISIBLE);
        changePassword.setVisibility(View.VISIBLE);
        deleteAccount.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);

    }
    private void changeView(int x){
        changeName.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        deleteAccount.setVisibility(View.GONE);
        back.setVisibility(View.GONE);

        inputField.setVisibility(View.VISIBLE);
        cancelInput.setVisibility(View.VISIBLE);
        instructions.setVisibility(View.VISIBLE);

        switch(x){
            case 1: passwordSubmit.setVisibility(View.VISIBLE);instructions.setText("Password");break;
            case 2: nameSubmit.setVisibility(View.VISIBLE);instructions.setText("Name");break;
            case 3: deleteSubmit.setVisibility(View.VISIBLE);instructions.setText("Type delete and then press confirm");break;
        }
    }

    public static Intent accountSettingsScreenIntent(Context context,int uid){
        Intent intent = new Intent(context,AccountSettingsActivity.class);
        intent.putExtra(AccountSettingsActivity.USER_ID_KEY,uid);
        return intent;
    }
}