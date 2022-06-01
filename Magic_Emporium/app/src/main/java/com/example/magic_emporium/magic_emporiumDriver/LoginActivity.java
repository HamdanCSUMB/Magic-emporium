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
import com.example.magic_emporium.magicDataBase.Item;
import com.example.magic_emporium.magicDataBase.ItemDao;
import com.example.magic_emporium.magicDataBase.MagicEmporiumDatabase;
import com.example.magic_emporium.magicDataBase.User;
import com.example.magic_emporium.magicDataBase.UserDao;

public class LoginActivity extends AppCompatActivity {

    private TextView welcomeText;
    private TextView invalidText;

    private EditText login;
    private EditText password;

    private Button submit;
    private Button nAcc;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wireUp();   //get all buttons text and the like wired up
        nAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CreateAccountActivity.createLoginIntent(LoginActivity.this);
                System.out.println("we are calling from the inside");
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = validUser();
                if(temp == -1){
                    //let user know they have invalid input
                    invalidText.setVisibility(View.VISIBLE);
                    System.out.println("invalid login values");
                }
                else{
                    //user puts in valid login data
                    User tempUser = userDao.findByLogin(login.getText().toString());
                    Intent intent;
                    if(tempUser.isAdmin()){     //check if it's an admin or if it's a user and send them to the correct screen
                        intent = AdminHomeScreenActivity.adminHomeScreenIntent(LoginActivity.this,tempUser.getUid());
                    }
                    else{
                        //intent = LoginActivity.homeScreenIntent(LoginActivity.this, temp);
                        intent = HomeScreenActivity.homeScreenIntent(LoginActivity.this,tempUser.getUid());
                    }
                    startActivity(intent);
                }
            }
        });
        //FOR TESTING


//        ItemDao itemDao = Room.databaseBuilder(this,MagicEmporiumDatabase.class,MagicEmporiumDatabase.DB_NAME)
//                .allowMainThreadQueries()
//                .build()
//                .itemDao();
//        User tempUser =userDao.findByLogin("");
//        if(tempUser==null){
//            Item item = new Item("Wand","Weapon","A magic disintigrating wand.",20.0,5);
//            itemDao.insert(item);
//            item = new Item("Dragon","pet","Fire breathing dragon.",99.99,1);
//            itemDao.insert(item);
//            item = new Item("Sponge","tool","Super absorbant sponge, even better than on commercials.",5.99,1);
//            itemDao.insert(item);
//            item = new Item("Rock","pet","It's just a rock, what do you expect.",50.00,2);
//            itemDao.insert(item);
//            tempUser = new User("easy","","",true);
//            userDao.insert(tempUser);
//            tempUser=new User("admin2","admin2","admin2",true);
//            userDao.insert(tempUser);
//            tempUser=new User("testuser2","testuser2","testuser2",false);
//            userDao.insert();
//        }






    }

    private void wireUp(){
        this.welcomeText = findViewById(R.id.welcomeText);
        this.invalidText = findViewById(R.id.invalidLogin);

        this.login = findViewById(R.id.editTextLogin);
        this.password = findViewById(R.id.editTextPassword);

        this.submit = findViewById(R.id.submitButton);
        this.nAcc = findViewById(R.id.newAccountButton);



        userDao = Room.databaseBuilder(this, MagicEmporiumDatabase.class,MagicEmporiumDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .userDao();
    }

    public static Intent loginScreenIntent(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        return intent;
    }

    private int validUser(){
        String logVal;
        String passVal;
        User tempUser;

        logVal=login.getText().toString();
        passVal=password.getText().toString();
        try{
            tempUser = userDao.findByLogin(logVal);
            if(tempUser!=null&&tempUser.getPassword().equals(passVal)){
                return tempUser.getUid();
            }
        }catch (Exception e){
            System.out.println("an exception occurred when trying to validate user "+ e);
            return -2;
        }
        return -1;
    }
}