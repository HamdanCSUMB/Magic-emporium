package com.example.magic_emporium.magic_emporiumDriver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.magic_emporium.R;
import com.example.magic_emporium.magicDataBase.Billing;
import com.example.magic_emporium.magicDataBase.BillingDao;
import com.example.magic_emporium.magicDataBase.MagicEmporiumDatabase;
import com.example.magic_emporium.magicDataBase.User;
import com.example.magic_emporium.magicDataBase.UserDao;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, BillingAdapter.OnItemListener {
    public static final String USER_ID_KEY = "com.example.magic_emporium.magic_emporiumDriver.userId";
    /*
    implementation still needed
     */
    private Button back;
    private BillingDao billingDao;

    User user;
    UserDao userDao;
    List<Billing> bills;
    ArrayList<Billing> billingArrayList;

    RecyclerView billRecyclerView;
    BillingAdapter billAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        wireUp();
    }

    private void wireUp(){
        userDao = Room.databaseBuilder(this, MagicEmporiumDatabase.class,MagicEmporiumDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .userDao();
        billingDao = Room.databaseBuilder(this, MagicEmporiumDatabase.class,MagicEmporiumDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .billingDao();
        user = userDao.findByUserId(this.getIntent().getIntExtra(USER_ID_KEY,-1));
        back = findViewById(R.id.backFromPurchaseHistory);
        bills=billingDao.getUserBillingHistory(user.getUid());
        billingArrayList=new ArrayList<Billing>(bills);

        billRecyclerView=findViewById(R.id.userPurchaseHistory);

        billAdapter = new BillingAdapter(this,billingArrayList,this);
        billRecyclerView.setAdapter(billAdapter);
        billRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HomeScreenActivity.homeScreenIntent(PurchaseHistoryActivity.this,user.getUid());
                startActivity(intent);
            }
        });
    }
    public static Intent purchaseHistoryScreenIntent(Context context, int uid){
        Intent intent = new Intent(context,PurchaseHistoryActivity.class);
        intent.putExtra(PurchaseHistoryActivity.USER_ID_KEY,uid);
        return intent;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(int position) {

    }
}