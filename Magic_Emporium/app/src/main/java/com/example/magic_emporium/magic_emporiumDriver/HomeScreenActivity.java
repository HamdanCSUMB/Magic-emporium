package com.example.magic_emporium.magic_emporiumDriver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.magic_emporium.R;
import com.example.magic_emporium.magicDataBase.MagicEmporiumDatabase;
import com.example.magic_emporium.magicDataBase.User;
import com.example.magic_emporium.magicDataBase.UserDao;

public class HomeScreenActivity extends AppCompatActivity {
    public static final String USER_ID_KEY = "com.example.magic_emporium.magic_emporiumDriver.userid";

    private TextView welcomeText;

    private Button logout;
    private Button shop;
    private Button settings;
    private Button purchaseHistory;

    private ImageButton backToAdmin;


    private User user;
    private UserDao userDao;
    private boolean admin;  //need this to show a back button for the admins if they are one so that they can return to the admin screen


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        wireUp();
        setText();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.loginScreenIntent(HomeScreenActivity.this);
                startActivity(intent);
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ViewInventoryActivity.viewInventoryScreenIntentConvert(HomeScreenActivity.this,user.getUid());
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AccountSettingsActivity.accountSettingsScreenIntent(HomeScreenActivity.this,user.getUid()));
            }
        });
        purchaseHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PurchaseHistoryActivity.purchaseHistoryScreenIntent(HomeScreenActivity.this,user.getUid()));
            }
        });



        /*
        retrieve user ID from extras and use it to determine if user is an admin or not
         */
    }

    private void wireUp(){
        logout = findViewById(R.id.userLogOut);
        welcomeText = findViewById(R.id.welcomeUser);
        shop=findViewById(R.id.Shop);
        settings=findViewById(R.id.accountSettings);
        purchaseHistory=findViewById(R.id.purchaseHistory);
        backToAdmin=findViewById(R.id.backToAdminView);




        userDao = Room.databaseBuilder(this, MagicEmporiumDatabase.class,MagicEmporiumDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .userDao();

        int user_id = this.getIntent().getIntExtra(USER_ID_KEY,-1);
        user = userDao.findByUserId(user_id);
        if(user.isAdmin()){
            backToAdmin.setVisibility(View.VISIBLE);
            backToAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = AdminHomeScreenActivity.adminHomeScreenIntent(HomeScreenActivity.this,user.getUid());
                    startActivity(intent);
                }
            });
        }
    }


    public static Intent homeScreenIntent(Context context, int uid){
        Intent intent = new Intent(context,HomeScreenActivity.class);
        intent.putExtra(HomeScreenActivity.USER_ID_KEY, uid);
        return intent;
    }

    private void setText(){
        String tempText;
        if(user.isAdmin()){
            tempText = getString(R.string.home_welcome_text, "Admin", user.getUserName());
        }
        else{
            tempText = getString(R.string.home_welcome_text, "User", user.getUserName());
        }
        welcomeText.setText(tempText);
    }
}