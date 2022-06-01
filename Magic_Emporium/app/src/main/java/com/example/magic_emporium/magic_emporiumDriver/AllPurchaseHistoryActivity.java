package com.example.magic_emporium.magic_emporiumDriver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class AllPurchaseHistoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, BillingAdapter.OnItemListener {
    //here we will just have a recycler view that allows us to see all purchase histories
    //if time permits we will add the ability to delete or manually add one
    /*
    not yet implemented
     */
    public static final String USER_ID_KEY = "com.example.magic_emporium.magic_emporiumDriver.userId";

    private Button back;
    private BillingDao billingDao;
    List<Billing> bills;
    ArrayList<Billing> billingArrayList;

    RecyclerView billRecyclerView;
    BillingAdapter billAdapter;

    User user;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_purchase_history);

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
        bills=billingDao.getAllBillingData();
        billingArrayList=new ArrayList<Billing>(bills);

        billRecyclerView=findViewById(R.id.billRecyclerView);
        back = findViewById(R.id.backFromAllPurchaseHistory);

        billAdapter = new BillingAdapter(this,billingArrayList,this);
        billRecyclerView.setAdapter(billAdapter);
        billRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminHomeScreenActivity.adminHomeScreenIntent(AllPurchaseHistoryActivity.this,user.getUid());
                startActivity(intent);
            }
        });
    }
    public static Intent allPurchaseHistoryScreenIntent(Context context, int uid){
        Intent intent = new Intent(context,AllPurchaseHistoryActivity.class);
        intent.putExtra(AllPurchaseHistoryActivity.USER_ID_KEY,uid);
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