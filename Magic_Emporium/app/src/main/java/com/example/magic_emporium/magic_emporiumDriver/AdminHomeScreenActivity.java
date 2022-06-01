package com.example.magic_emporium.magic_emporiumDriver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.magic_emporium.R;
import com.example.magic_emporium.magicDataBase.MagicEmporiumDatabase;
import com.example.magic_emporium.magicDataBase.User;
import com.example.magic_emporium.magicDataBase.UserDao;

public class AdminHomeScreenActivity extends AppCompatActivity {
    public static final String USER_ID_KEY = "com.example.magic_emporium.magic_emporiumDriver.AdminId";

    private Button logout;
    private Button userView;
    private Button inventory;
    private Button modifyUsers;
    private Button viewPurchaseHistories;


    private User user;
    private UserDao userDao;
    private boolean admin;  //need this to show a back button for the admins if they are one so that they can return to the admin screen


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

        wireUp();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.loginScreenIntent(AdminHomeScreenActivity.this);
                startActivity(intent);
            }
        });
        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HomeScreenActivity.homeScreenIntent(AdminHomeScreenActivity.this,user.getUid());
                startActivity(intent);
            }
        });
        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = ViewInventoryActivity.viewInventoryScreenIntent(AdminHomeScreenActivity.this,user.getUid());
               startActivity(intent);
            }
        });
        modifyUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ModifyUsersActivity.modifyUsersScreenIntent(AdminHomeScreenActivity.this,user.getUid()));
            }
        });
        viewPurchaseHistories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AllPurchaseHistoryActivity.allPurchaseHistoryScreenIntent(AdminHomeScreenActivity.this,user.getUid()));
            }
        });

        /*
        retrieve user ID from extras and use it to determine if user is an admin or not
         */
    }

    private void wireUp(){
        logout = findViewById(R.id.adminLogOut);
        userView = findViewById(R.id.viewUserView);
        this.inventory=findViewById(R.id.Inventory);
        this.modifyUsers=findViewById(R.id.modifyUsers);
        viewPurchaseHistories=findViewById(R.id.viewAllPurchaseHistory);

        userDao = Room.databaseBuilder(this, MagicEmporiumDatabase.class,MagicEmporiumDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .userDao();

        int user_id = this.getIntent().getIntExtra(USER_ID_KEY,-1);
        user = userDao.findByUserId(user_id);
    }

    public static Intent adminHomeScreenIntent(Context context, int uid){
        Intent intent = new Intent(context,AdminHomeScreenActivity.class);
        intent.putExtra(AdminHomeScreenActivity.USER_ID_KEY,uid);
        return intent;
    }

}