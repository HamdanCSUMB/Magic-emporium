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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.magic_emporium.R;
import com.example.magic_emporium.magicDataBase.MagicEmporiumDatabase;
import com.example.magic_emporium.magicDataBase.User;
import com.example.magic_emporium.magicDataBase.UserDao;

import java.util.ArrayList;
import java.util.List;

public class ModifyUsersActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, UserAdapter.OnItemListener {
    public static final String USER_ID_KEY = "com.example.magic_emporium.magic_emporiumDriver.userId";
    private static String location ="index in user adapter";
    /*
    implementation still needed
     */

    private User user;
    private UserDao userDao;
    private List<User> users;
    private ArrayList<User> userArrayList;
    private User selectedUser = null;
    private UserAdapter userAdapter;  //for use later with recyclerView

    private RecyclerView recyclerView;

    private TextView userSearch;
    private ImageButton submitSearch;
    private TextView selectedUserText;

    private EditText creaModUserName;       //add user / modify user text fields and checkbox
    private EditText creaModUserLogin;
    private EditText creaModUserPassword;
    private CheckBox creaModUserIsAdmin;

    private Button addUser;         //main menu buttons
    private Button modifyUser;
    private Button deleteUser;
    private Button back;

    private Button addUserSubmit;   //modify / add submenus for submission / canceling
    private Button addUserCancel;
    private Button modifyUserSubmit;
    private Button modifyUserCancel;

    private Intent useIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_users);

        wireUp();
        wireButtons();
        useIntent=this.getIntent();


        userArrayList = new ArrayList<User>(users);
        userAdapter = new UserAdapter(this,userArrayList, this);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //setupCheckbox here
    }

    private void wireUp(){
        userDao = Room.databaseBuilder(this, MagicEmporiumDatabase.class,MagicEmporiumDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .userDao();
        user = userDao.findByUserId(this.getIntent().getIntExtra(USER_ID_KEY,-1));
        users = userDao.getAllUsers();


        recyclerView=findViewById(R.id.recyclerAdminUserView);
        creaModUserIsAdmin=findViewById(R.id.adminUserAdminStatusCheckBox);

        this.userSearch=findViewById(R.id.userSearchBar);
        this.submitSearch =findViewById(R.id.userSearchButton);
        this.selectedUserText=findViewById(R.id.selectedUserText);

        this.creaModUserName=findViewById(R.id.adminUserNameEditText);
        this.creaModUserLogin=findViewById(R.id.adminUserLoginEditText);
        this.creaModUserPassword=findViewById(R.id.adminUserPasswordEditText);

        this.addUser=findViewById(R.id.adminAddUserButton);
        this.deleteUser=findViewById(R.id.adminDeleteUser);
        this.modifyUser=findViewById(R.id.adminModifyUser);
        this.back = findViewById(R.id.adminModifyBackCancel);

        this.addUserCancel=findViewById(R.id.adminUserCreateCancel);
        this.modifyUserCancel=findViewById(R.id.adminUserModifyCancel);
        this.addUserSubmit=findViewById(R.id.adminUserCreateSubmit);
        this.modifyUserSubmit=findViewById(R.id.adminUserModifySubmit);

        addUser.setVisibility(View.VISIBLE);
        deleteUser.setVisibility(View.VISIBLE);
        modifyUser.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
    }
    private void wireButtons(){
        back.setOnClickListener(v -> {
            Intent intent = AdminHomeScreenActivity.adminHomeScreenIntent(ModifyUsersActivity.this,user.getUid());
            startActivity(intent);
        });
        deleteUser.setOnClickListener(v -> {
            if(selectedUser!=null){
                userDao.delete(selectedUser);
                int temp = userArrayList.indexOf(selectedUser);
                //users.remove(selectedUser);   //if there is an issue with the copy, and the arraylist isn't changeing the list, need to add this
                userArrayList.remove(selectedUser);
                userAdapter.notifyItemRemoved(temp);
            }
        });
        modifyUser.setOnClickListener(v -> {
            if(selectedUser!=null){
                modifyView();
            }

        });
        addUser.setOnClickListener(v -> {
            addView();
        });



        addUserSubmit.setOnClickListener(v -> {
            if(validInput()){
                completeAdd();
                baseView();
            }
        });
        addUserCancel.setOnClickListener(v -> {
            baseView();
        });
        modifyUserSubmit.setOnClickListener(v -> {
            if(validInput()){
                completeModify();
            }
        });
        modifyUserCancel.setOnClickListener(v -> {
            baseView();
        });
        submitSearch.setOnClickListener(v -> {
            search();
        });



    }
    private void completeAdd(){
        User tempUser;
        tempUser = new User(creaModUserName.getText().toString(),
                creaModUserLogin.getText().toString(),
                creaModUserPassword.getText().toString(),
                creaModUserIsAdmin.isChecked());
        userDao.insert(tempUser);
        userArrayList.add(tempUser);
        userAdapter.notifyDataSetChanged();
    }

    private void completeModify(){
        selectedUser.setAdmin(creaModUserIsAdmin.isChecked());
        selectedUser.setLogin(creaModUserLogin.getText().toString());
        selectedUser.setUserName(creaModUserName.getText().toString());
        selectedUser.setPassword(creaModUserPassword.getText().toString());
        userDao.update(selectedUser);
        int temp = userArrayList.indexOf(selectedUser);
        userAdapter.notifyItemChanged(temp);
        baseView();
    }

    private boolean validInput(){
        if(creaModUserPassword.getText().toString().equals("")||creaModUserName.getText().toString().equals("")||creaModUserLogin.getText().toString().equals("")){
            return false;
        }else{
            return true;
        }
    }

    private void search(){
        String searchText = userSearch.getText().toString();
        ArrayList<User> searchedUsers = new ArrayList<>();
        for(int i = 0; i<users.size();i++){
            if(users.get(i).getUserName().toLowerCase().contains(searchText.toLowerCase())){
                searchedUsers.add(users.get(i));
            }
        }
        userArrayList.clear();
        userArrayList.addAll(searchedUsers);
        userAdapter.notifyDataSetChanged();
    }

    private void baseView(){
        userSearch.setVisibility(View.VISIBLE);
        submitSearch.setVisibility(View.VISIBLE);
        selectedUserText.setVisibility(View.VISIBLE);

        recyclerView.setVisibility(View.VISIBLE);

        addUser.setVisibility(View.VISIBLE);
        modifyUser.setVisibility(View.VISIBLE);
        deleteUser.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);

        creaModUserIsAdmin.setVisibility(View.GONE);
        creaModUserName.setVisibility(View.GONE);
        creaModUserLogin.setVisibility(View.GONE);
        creaModUserPassword.setVisibility(View.GONE);

        creaModUserPassword.setText("");
        creaModUserLogin.setText("");
        creaModUserName.setText("");
        creaModUserIsAdmin.setChecked(false);

        addUserSubmit.setVisibility(View.GONE);
        addUserCancel.setVisibility(View.GONE);
        modifyUserCancel.setVisibility(View.GONE);
        modifyUserSubmit.setVisibility(View.GONE);
    }
    private void modifyView(){
        creaModUserPassword.setVisibility(View.VISIBLE);
        creaModUserLogin.setVisibility(View.VISIBLE);
        creaModUserName.setVisibility(View.VISIBLE);
        creaModUserIsAdmin.setVisibility(View.VISIBLE);

        modifyUserCancel.setVisibility(View.VISIBLE);
        modifyUserSubmit.setVisibility(View.VISIBLE);

        userSearch.setVisibility(View.GONE);
        submitSearch.setVisibility(View.GONE);
        selectedUserText.setVisibility(View.GONE);

        recyclerView.setVisibility(View.GONE);

        addUser.setVisibility(View.GONE);
        modifyUser.setVisibility(View.GONE);
        deleteUser.setVisibility(View.GONE);
        back.setVisibility(View.GONE);

        creaModUserIsAdmin.setChecked(selectedUser.isAdmin());
        creaModUserLogin.setText(selectedUser.getLogin());
        creaModUserName.setText(selectedUser.getUserName());
        creaModUserPassword.setText(selectedUser.getPassword());
    }
    private void addView(){
        creaModUserIsAdmin.setVisibility(View.VISIBLE);
        creaModUserName.setVisibility(View.VISIBLE);
        creaModUserLogin.setVisibility(View.VISIBLE);
        creaModUserPassword.setVisibility(View.VISIBLE);

        addUserSubmit.setVisibility(View.VISIBLE);
        addUserCancel.setVisibility(View.VISIBLE);

        userSearch.setVisibility(View.GONE);
        submitSearch.setVisibility(View.GONE);
        selectedUserText.setVisibility(View.GONE);

        recyclerView.setVisibility(View.GONE);

        addUser.setVisibility(View.GONE);
        modifyUser.setVisibility(View.GONE);
        deleteUser.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
    }

    public static Intent modifyUsersScreenIntent(Context context,int uid){
        Intent intent = new Intent(context,ModifyUsersActivity.class);
        intent.putExtra(ModifyUsersActivity.USER_ID_KEY,uid);
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
        selectedUser = userArrayList.get(position);
        this.getIntent().putExtra(location,position);
        selectedUserText.setText("Selected User: "+selectedUser.getUserName());
        selectedUserText.setVisibility(View.VISIBLE);
    }
}
/*
   private RecyclerView recyclerView;
    private RecyclerView cartRecycler;
    private Spinner categories;         //I bet these two are gonna give me a headache, hope not
    private Spinner itemCreateSpinner;  //they weren't that bad in the end

    private TextView searchBar;
    private TextView selectedItemText;

    private EditText createItemName;
    private EditText createItemDescription;
    private EditText createItemPrice;
    private EditText createItemStock;                           //over the course of writing this entire activity, i've learned that I had not planned nearly enough
                                                        //more planning would have helped, I should have structured 90% of the entire thing rather than like a vague 50-60%
    private Button addToCart;
    private Button viewCart;
    private Button userBack;
    private Button emptyCart;

    private Button purchaseCart;
    private Button removeItemFromCart;
    private Button backFromCart;
    private Button cartEmptyCart;

    private Button adminBack;
    private Button addItem;
    private Button modifyItem;
    private Button deleteItem;

    private Button createItemConfirm;
    private Button createItemCancel;
    private Button modifyItemConfirm;
    private Button modifyItemCancel;

    private ImageButton imageButton;
 */