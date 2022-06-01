package com.example.magic_emporium.magic_emporiumDriver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.magic_emporium.R;
import com.example.magic_emporium.magicDataBase.MagicEmporiumDatabase;
import com.example.magic_emporium.magicDataBase.User;
import com.example.magic_emporium.magicDataBase.UserDao;


public class CreateAccountActivity extends AppCompatActivity {

    private TextView invalidLogin;
    private TextView invalidPassword;
    private TextView invalidUserName;

    private EditText login;
    private EditText password;
    private EditText name;

    private Button submit;
    private ImageButton cancel;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        wireUp();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validUser()){
                    System.out.println("the button worked");
                    User nAcc = new User(name.getText().toString(),login.getText().toString(),password.getText().toString(),false);
                    userDao.insert(nAcc);
                    Intent intent = LoginActivity.loginScreenIntent(CreateAccountActivity.this);  //brings you back to login after a succesful creation
                    startActivity(intent);
                }else{
                    System.out.println("it didn't work");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.loginScreenIntent(CreateAccountActivity.this);  //brings you back to login when canceling
                startActivity(intent);
            }
        });
    }

    private void wireUp(){
        this.invalidLogin = findViewById(R.id.createInvalidLogin);
        this.invalidPassword = findViewById(R.id.createInvalidPassword);
        this.invalidUserName = findViewById(R.id.createInvalidUsername);

        this.login = findViewById(R.id.editTextLogin);
        this.password = findViewById(R.id.editTextPassword);
        this.name = findViewById(R.id.editTextName);

        this.submit = findViewById(R.id.submitButton);
        this.cancel = findViewById(R.id.cancelAccountCreation);


        userDao = Room.databaseBuilder(this, MagicEmporiumDatabase.class,MagicEmporiumDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .userDao();
    }

    private boolean validUser(){
        boolean log = validLogin();
        boolean pas = validPassword();
        boolean nam = validName();
        if(log&&pas&&nam){
            User tempUser = userDao.findByLogin(this.login.getText().toString());
            if(tempUser!=null){
                return false;
            }
            return true;
        }else{
            return false;
        }

    }
    private boolean validLogin(){
        String login = this.login.getText().toString();
        if(login.equals("")){
            invalidLogin.setVisibility(View.VISIBLE);
            return false;
        }
        for(int i = 0; i<login.length(); i++){
            if(!(Character.isLetter(login.charAt(i))||Character.isDigit(login.charAt(i)))){
                invalidLogin.setVisibility(View.VISIBLE);
                return false;
            }
        }
        invalidLogin.setVisibility(View.INVISIBLE);
        return true;
    }
    private boolean validPassword(){
        String password = this.password.getText().toString();
        if(password.equals("")){
            invalidPassword.setVisibility(View.VISIBLE);
            return false;
        }
        for(int i = 0; i<password.length(); i++){
            if(!(Character.isLetter(password.charAt(i))||Character.isDigit(password.charAt(i)))){
                invalidPassword.setVisibility(View.VISIBLE);
                return false;
            }
        }
        invalidPassword.setVisibility(View.INVISIBLE);
        return true;
    }
    private boolean validName(){
        String name = this.name.getText().toString();
        if(name.equals("")){
            invalidUserName.setVisibility(View.VISIBLE);
            return false;
        }
        for(int i = 0; i<name.length(); i++){
            if(!(Character.isLetter(name.charAt(i))||Character.isDigit(name.charAt(i)))){
                invalidUserName.setVisibility(View.VISIBLE);
                return false;
            }
        }
        invalidUserName.setVisibility(View.INVISIBLE);
        return true;
    }
    public static Intent createLoginIntent(Context context){
        Intent intent = new Intent(context, CreateAccountActivity.class);
        return intent;
    }

}