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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.magic_emporium.R;
import com.example.magic_emporium.magicDataBase.Billing;
import com.example.magic_emporium.magicDataBase.BillingDao;
import com.example.magic_emporium.magicDataBase.Item;
import com.example.magic_emporium.magicDataBase.ItemDao;
import com.example.magic_emporium.magicDataBase.MagicEmporiumDatabase;
import com.example.magic_emporium.magicDataBase.User;
import com.example.magic_emporium.magicDataBase.UserDao;

import java.util.ArrayList;
import java.util.List;

public class ViewInventoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ItemAdapter.OnItemListener {
    public static final String USER_ID_KEY = "com.example.magic_emporium.magic_emporiumDriver.userId";
    public static String CONVERT_TO_USER = "com.example.magic_emporium.magic_emporiumDriver.convert";
    private static String location ="index in item adapter";
    //It was definitely a mistake to incorporate both the Admin view and user view into one activity in hindsight
    //the level of crazy convolution for no reason is insane and if I had the option to do this again I WOULD NEVER DO IT THIS WAY AGAIN
    //forced far more logic and proper tracking that wasn't needed to be made if I just seperated the two

    private User user;
    private UserDao userDao;
    private ItemDao itemDao;
    private BillingDao billingDao;
    private List<Billing> bill;
    private List<Item> items;
    private ArrayList<Item> cart;
    private ArrayList<Item> itemArrayList;
    private Item selectedItem = null;
    private ItemAdapter itemAdapter;
    private ItemAdapter cartAdapter;

    private String itemCategorySelection;              //I reached a point of laziness here where I wanted to sleep but I wanted to finish this activity

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

    private Intent useIntent;

    private boolean isAdmin;
    private boolean inCartView=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        wireUp();
        wireButtons();
        useIntent = this.getIntent();

        //setting up the recycler view
        itemArrayList = new ArrayList<Item>(items);
        itemAdapter = new ItemAdapter(this,itemArrayList,this);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cart = new ArrayList<Item>();
        cartAdapter = new ItemAdapter(this,cart,this);
        cartRecycler.setAdapter(cartAdapter);
        cartRecycler.setLayoutManager(new LinearLayoutManager(this));


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.itemCategories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
        categories.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> creationAdapter = ArrayAdapter.createFromResource(this,R.array.itemCreationCategories, android.R.layout.simple_spinner_item);
        creationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCreateSpinner.setAdapter(creationAdapter);
        itemCreateSpinner.setOnItemSelectedListener(this);


    }

    private void wireUp(){

        userDao = Room.databaseBuilder(this, MagicEmporiumDatabase.class,MagicEmporiumDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .userDao();
        itemDao = Room.databaseBuilder(this, MagicEmporiumDatabase.class,MagicEmporiumDatabase.DB_NAME) //initialize our two DAOs
                .allowMainThreadQueries()
                .build()
                .itemDao();
        billingDao = Room.databaseBuilder(this, MagicEmporiumDatabase.class,MagicEmporiumDatabase.DB_NAME) //initialize our two DAOs
                .allowMainThreadQueries()
                .build()
                .billingDao();
        int user_id = this.getIntent().getIntExtra(USER_ID_KEY,-1);
        user = userDao.findByUserId(user_id);
        if(user.isAdmin()&&this.getIntent().getBooleanExtra(ViewInventoryActivity.CONVERT_TO_USER,false)){
            isAdmin=false;

        }else{
            isAdmin=user.isAdmin();
        }
        if(isAdmin){
            items = itemDao.getAll();
        }else{
            items=itemDao.loadAllNonOOS();
        }

        bill = new ArrayList<Billing>();

        this.searchBar=findViewById(R.id.searchBar);

        this.addToCart=findViewById(R.id.AddToCart);
        this.viewCart=findViewById(R.id.viewCart);
        userBack=findViewById(R.id.backCancel);
        adminBack=findViewById(R.id.adminBackCancel);
        this.emptyCart=findViewById(R.id.emptyCart);
        this.addItem=findViewById(R.id.addItem);
        this.modifyItem=findViewById(R.id.modifyItem);
        this.deleteItem=findViewById(R.id.deleteItem);

        this.imageButton=findViewById(R.id.itemSearchButton);

        this.selectedItemText=findViewById(R.id.selectedItemText);

        //hidden till item creation / modification
        createItemDescription=findViewById(R.id.adminItemDescriptionEditText);
        createItemName=findViewById(R.id.adminItemNameEditText);
        createItemPrice=findViewById(R.id.adminItemPriceEditText);
        createItemStock=findViewById(R.id.adminItemStockEditText);

        createItemCancel=findViewById(R.id.adminItemCreateCancel);
        createItemConfirm=findViewById(R.id.adminItemCreateSubmit);
        modifyItemCancel=findViewById(R.id.adminItemModifyCancel);
        modifyItemConfirm=findViewById(R.id.adminItemModifySubmit);

        purchaseCart=findViewById(R.id.purchaseCart);
        removeItemFromCart=findViewById(R.id.removeItemFromCart);
        backFromCart=findViewById(R.id.backFromCart);
        cartEmptyCart=findViewById(R.id.cartEmptyCart);

        itemCreateSpinner=findViewById(R.id.adminItemCategorySpinner);

        //end of major hidden items
        cartRecycler=findViewById(R.id.cartRecyclerView);
        recyclerView=findViewById(R.id.recyclerView);
        categories=findViewById(R.id.dropDown);
    }
    private void wireButtons(){
        if(isAdmin){
            adminBack.setVisibility(View.VISIBLE);
            addItem.setVisibility(View.VISIBLE);
            modifyItem.setVisibility(View.VISIBLE);
            deleteItem.setVisibility(View.VISIBLE);
            adminBack.setOnClickListener(v -> {
                Intent intent = AdminHomeScreenActivity.adminHomeScreenIntent(ViewInventoryActivity.this,user.getUid());
                startActivity(intent);
            });
            addItem.setOnClickListener(v -> setItemCreationView());
            deleteItem.setOnClickListener(v -> {
                if (selectedItem == null) {
                    selectedItemText.setText(R.string.item_deleted);
                }else{
                    itemDao.delete(selectedItem);
                    itemArrayList.remove(useIntent.getIntExtra(location,-1));
                    itemAdapter.notifyItemRemoved(useIntent.getIntExtra(location,-1));
                    selectedItem=null;
                }

            });
            createItemCancel.setOnClickListener(v -> adminBaseView());
            createItemConfirm.setOnClickListener(v -> createConfirm());
            modifyItem.setOnClickListener(v -> {
                if(selectedItem==null){
                    selectedItemText.setText(R.string.selectModifyItem);
                }else{
                    setItemModifyView();
                }
            });
            modifyItemCancel.setOnClickListener(v -> {
                createItemDescription.setText("");
                createItemName.setText("");
                createItemPrice.setText("");
                createItemStock.setText("");
                adminBaseView();
            });
            modifyItemConfirm.setOnClickListener(v -> modifyConfirm());
        }else{
            addToCart.setVisibility(View.VISIBLE);
            viewCart.setVisibility(View.VISIBLE);
            userBack.setVisibility(View.VISIBLE);
            emptyCart.setVisibility(View.VISIBLE);
            addToCart.setOnClickListener(v -> {
                    addItemToCart();
            });
            viewCart.setOnClickListener(v -> {
                cartView();
                selectedItem=null;
            });
            userBack.setOnClickListener(v -> {
                Intent intent = HomeScreenActivity.homeScreenIntent(ViewInventoryActivity.this,user.getUid());
                startActivity(intent);
            });
            emptyCart.setOnClickListener(v -> {
                cart.clear();
                cartAdapter.notifyDataSetChanged();
            });
            backFromCart.setOnClickListener(v->{
                userView();
            });
            cartEmptyCart.setOnClickListener(v->{
                cart.clear();
                cartAdapter.notifyDataSetChanged();
            });
            removeItemFromCart.setOnClickListener(v->{
                if(selectedItem!=null){
                    for(int i = 0; i<cart.size(); i++){                 //look through the crrt to see if item exists in it already
                        if(cart.get(i).getItemName()==selectedItem.getItemName()
                                &&cart.get(i).getItemDescription()==selectedItem.getItemDescription()
                                &&cart.get(i).getItemPrice()==selectedItem.getItemPrice()){
                            cart.get(i).setItemStock(cart.get(i).getItemStock()-1);
                            cartAdapter.notifyItemChanged(i);
                            if(cart.get(i).getItemStock()==0){ //if it does then add 1 of it return to end function
                                cart.remove(cart.get(i));
                                cartAdapter.notifyItemRemoved(i);
                                selectedItem=null;
                                break;
                            }
                        }
                    }
                }
            });
            purchaseCart.setOnClickListener(v->{
                for(int i = 0; i < cart.size(); i++){
                    for(int k = 0; k<items.size();k++){
                        if(cart.get(i).getItemName()==items.get(k).getItemName()
                                &&cart.get(i).getItemDescription()==items.get(k).getItemDescription()
                                &&cart.get(i).getItemPrice()==items.get(k).getItemPrice()){
                            items.get(k).setItemStock(items.get(k).getItemStock()-cart.get(i).getItemStock());
                            itemDao.update(items.get(k));
                        }
                    }
                    bill.add(new Billing(user.getUid(),cart.get(i).getItemName(),cart.get(i).getItemStock(),cart.get(i).getItemPrice(),user.getUserName()));
                }
                itemAdapter.notifyDataSetChanged();
                for(int i = 0; i< bill.size();i++){
                    billingDao.insert(bill.get(i));
                }
                cart.clear();
                cartAdapter.notifyDataSetChanged();
                userView();
            });
        }
        imageButton.setOnClickListener(v -> search());

    }


    private void addItemToCart(){
        if(selectedItem!=null){
            boolean tempBool = false;
            for(int i = 0; i<cart.size(); i++){                 //look through the crrt to see if item exists in it already
                if(cart.get(i).getItemName()==selectedItem.getItemName()&&cart.get(i).getItemDescription()==selectedItem.getItemDescription()&&cart.get(i).getItemPrice()==selectedItem.getItemPrice()){
                    tempBool = true;
                    if(cart.get(i).getItemStock()<selectedItem.getItemStock()){ //if it does then add 1 of it return to end function
                        cart.get(i).setItemStock(cart.get(i).getItemStock()+1);
                        cartAdapter.notifyItemChanged(i);
                        return;
                    }
                }
            }
            if(tempBool){
                selectedItemText.setText(R.string.maximum_allowed);
                return;
            }
            Item temp = new Item(selectedItem.getItemName(),selectedItem.getItemCategory(),selectedItem.getItemDescription(),selectedItem.getItemPrice(),1);
            cart.add(temp);
        }
    }


    public static Intent viewInventoryScreenIntent(Context context, int uid){
        Intent intent = new Intent(context, ViewInventoryActivity.class);
        intent.putExtra(ViewInventoryActivity.USER_ID_KEY,uid);
        return intent;
    }
    public static Intent viewInventoryScreenIntentConvert(Context context, int uid){
        Intent intent = new Intent(context, ViewInventoryActivity.class);
        intent.putExtra(ViewInventoryActivity.USER_ID_KEY,uid);
        intent.putExtra(ViewInventoryActivity.CONVERT_TO_USER,true);
        return intent;
    }

    @Override
    public void onItemClick(int position) {
        if(inCartView){
            selectedItem=cart.get(position);
        }else{
            selectedItem = itemArrayList.get(position);
        }
        this.getIntent().putExtra(location,position);
        selectedItemText.setText("Selected Item: "+selectedItem.getItemName());
        selectedItemText.setVisibility(View.VISIBLE);
    }

    public void search(){
        String searchText = searchBar.getText().toString();
        ArrayList<Item> searchedItems = new ArrayList<>();
        for (int i = 0; i<items.size();i++){
            if(items.get(i).getItemName().toLowerCase().contains(searchText.toLowerCase())){
                searchedItems.add(items.get(i));
            }
        }
        itemArrayList.clear();
        itemArrayList.addAll(searchedItems);
        itemAdapter.notifyDataSetChanged();
    }
    public void searchCategory(String category){
        ArrayList<Item> searchedItems = new ArrayList<>();
        for (int i = 0; i<items.size();i++){
            if(items.get(i).getItemCategory().toLowerCase().contains(category.toLowerCase())){
                searchedItems.add(items.get(i));
            }
        }
        itemArrayList.clear();
        itemArrayList.addAll(searchedItems);
        itemAdapter.notifyDataSetChanged();
    }
    public void originalDisplay(){
        ArrayList<Item> searchedItems = new ArrayList<>();
        searchedItems.addAll(items);
        itemArrayList.clear();
        itemArrayList.addAll(searchedItems);
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String category = parent.getItemAtPosition(position).toString();
        if(category.equals("Any")){
            originalDisplay();
        }
        else{
            itemCategorySelection=category;         //this is beyond lazy, need to change this to some sort of putextra if statement if the xtra is true then do w/e prob do the puts in the addclick and on the cancel click
            if(recyclerView.getVisibility()==View.VISIBLE){
                searchCategory(category);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void cartView() {
        cartRecycler.setVisibility(View.VISIBLE);
        cartEmptyCart.setVisibility(View.VISIBLE);
        backFromCart.setVisibility(View.VISIBLE);
        removeItemFromCart.setVisibility(View.VISIBLE);
        purchaseCart.setVisibility(View.VISIBLE);

        addToCart.setVisibility(View.GONE);
        viewCart.setVisibility(View.GONE);
        userBack.setVisibility(View.GONE);
        emptyCart.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        searchBar.setVisibility(View.INVISIBLE);
        imageButton.setVisibility(View.INVISIBLE);
        categories.setVisibility(View.INVISIBLE);
        inCartView=true;

    }
    private void userView(){
        cartRecycler.setVisibility(View.GONE);
        cartEmptyCart.setVisibility(View.GONE);
        backFromCart.setVisibility(View.GONE);
        removeItemFromCart.setVisibility(View.GONE);
        purchaseCart.setVisibility(View.GONE);

        addToCart.setVisibility(View.VISIBLE);
        viewCart.setVisibility(View.VISIBLE);
        userBack.setVisibility(View.VISIBLE);
        emptyCart.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        searchBar.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.VISIBLE);
        categories.setVisibility(View.VISIBLE);
        inCartView=false;
    }

    private void setItemCreationView(){
        categories.setVisibility(View.GONE);
        searchBar.setVisibility(View.GONE);
        imageButton.setVisibility(View.GONE);

        selectedItemText.setVisibility(View.GONE);

        adminBack.setVisibility(View.GONE);
        addItem.setVisibility(View.GONE);
        modifyItem.setVisibility(View.GONE);
        deleteItem.setVisibility(View.GONE);

        recyclerView.setVisibility(View.GONE);

        createItemConfirm.setVisibility(View.VISIBLE);
        createItemCancel.setVisibility(View.VISIBLE);
        createItemStock.setVisibility(View.VISIBLE);
        createItemPrice.setVisibility(View.VISIBLE);
        createItemName.setVisibility(View.VISIBLE);
        createItemDescription.setVisibility(View.VISIBLE);

        itemCreateSpinner.setVisibility(View.VISIBLE);
    }
    private void setItemModifyView(){
        categories.setVisibility(View.GONE);
        searchBar.setVisibility(View.GONE);
        imageButton.setVisibility(View.GONE);

        selectedItemText.setVisibility(View.GONE);

        adminBack.setVisibility(View.GONE);
        addItem.setVisibility(View.GONE);
        modifyItem.setVisibility(View.GONE);
        deleteItem.setVisibility(View.GONE);

        recyclerView.setVisibility(View.GONE);

        modifyItemConfirm.setVisibility(View.VISIBLE);
        modifyItemCancel.setVisibility(View.VISIBLE);
        createItemStock.setVisibility(View.VISIBLE);
        createItemPrice.setVisibility(View.VISIBLE);
        createItemName.setVisibility(View.VISIBLE);
        createItemDescription.setVisibility(View.VISIBLE);

        createItemName.setText(selectedItem.getItemName());
        createItemDescription.setText(selectedItem.getItemDescription());
        createItemStock.setText(String.valueOf(selectedItem.getItemStock()));
        createItemPrice.setText(String.valueOf(selectedItem.getItemPrice()));

        itemCreateSpinner.setVisibility(View.VISIBLE);
    }
    private void adminBaseView(){
        categories.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.VISIBLE);

        selectedItemText.setVisibility(View.VISIBLE);

        adminBack.setVisibility(View.VISIBLE);
        addItem.setVisibility(View.VISIBLE);
        modifyItem.setVisibility(View.VISIBLE);
        deleteItem.setVisibility(View.VISIBLE);

        recyclerView.setVisibility(View.VISIBLE);

        createItemConfirm.setVisibility(View.GONE);
        createItemCancel.setVisibility(View.GONE);
        createItemStock.setVisibility(View.GONE);
        createItemPrice.setVisibility(View.GONE);
        createItemName.setVisibility(View.GONE);
        createItemDescription.setVisibility(View.GONE);

        modifyItemConfirm.setVisibility(View.GONE);
        modifyItemCancel.setVisibility(View.GONE);

        itemCreateSpinner.setVisibility(View.GONE);

    }
    private void createConfirm(){
        if(validCreateInput()){
        Item temp;
        temp = new Item(createItemName.getText().toString(),
                itemCategorySelection,
                createItemDescription.getText().toString(),
                Double.parseDouble(createItemPrice.getText().toString()),
                Integer.parseInt(createItemStock.getText().toString()));
        itemDao.insert(temp);
        itemArrayList.add(temp);
        itemAdapter.notifyDataSetChanged();
        adminBaseView();
        }
    }
    private void modifyConfirm(){
        if(validCreateInput()){
            int tempIndex = itemArrayList.indexOf(selectedItem);
            selectedItem.setItemCategory(itemCategorySelection);
            selectedItem.setItemName(createItemName.getText().toString());
            selectedItem.setItemDescription(createItemDescription.getText().toString());
            selectedItem.setItemPrice(Double.parseDouble(createItemPrice.getText().toString()));
            selectedItem.setItemStock(Integer.parseInt(createItemStock.getText().toString()));
            itemDao.update(selectedItem);
            itemArrayList.set(tempIndex,selectedItem);
            itemAdapter.notifyItemChanged(tempIndex);
            adminBaseView();
        }
    }
    private boolean validCreateInput(){
        if(!createItemDescription.equals("")&&!createItemName.equals("")){
            return true;
        }
        return false;
    }
}
