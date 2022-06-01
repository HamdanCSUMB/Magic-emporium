package com.example.magic_emporium.magic_emporiumDriver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magic_emporium.R;
import com.example.magic_emporium.magicDataBase.Billing;

import java.util.ArrayList;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.BillingViewHolder> {

    Context context;
    ArrayList<Billing> bills;
    private OnItemListener mOnItemListener;

    public BillingAdapter(Context context,ArrayList<Billing> bills,OnItemListener onItemListener){
        this.context=context;
        this.bills=bills;
        this.mOnItemListener=onItemListener;
    }

    @NonNull
    @Override
    public BillingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.bill_row,parent,false);
        return new BillingViewHolder(view,mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BillingViewHolder holder, int position) {
        holder.userUserId.setText("User ID: "+bills.get(position).getUser_id());
        holder.userUserName.setText("User Name: "+bills.get(position).getUser_name());
        holder.itemPurchaseDateText.setText("Date: "+bills.get(position).getDate().toString());
        holder.itemQuantityText.setText("Quantity: "+bills.get(position).getItem_quantity());
        holder.itemPricePerUnitText.setText("PPU: "+bills.get(position).getItem_price());
        holder.itemNameText.setText("Item Name: "+bills.get(position).getItem_name());

    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public class BillingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView itemNameText;
        TextView itemQuantityText;
        TextView itemPricePerUnitText;
        TextView itemPurchaseDateText;
        TextView userUserName;
        TextView userUserId;
        OnItemListener onItemListener;

        public BillingViewHolder(@NonNull View itemView,OnItemListener onItemListener) {
            super(itemView);
            itemNameText=itemView.findViewById(R.id.billItemNameCardDisplay);
            itemQuantityText=itemView.findViewById(R.id.billItemQuantity);
            itemPricePerUnitText=itemView.findViewById(R.id.billPricePerUnit);
            itemPurchaseDateText=itemView.findViewById(R.id.billItemPurchaseDate);
            userUserName=itemView.findViewById(R.id.billUserName);
            userUserId=itemView.findViewById(R.id.billUserId);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }


    public interface OnItemListener{
        void onItemClick(int position);

    }

}
