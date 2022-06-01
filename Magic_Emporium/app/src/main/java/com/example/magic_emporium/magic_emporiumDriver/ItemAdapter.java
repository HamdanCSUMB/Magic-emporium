package com.example.magic_emporium.magic_emporiumDriver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magic_emporium.R;
import com.example.magic_emporium.magicDataBase.Item;

import java.util.ArrayList;
/*
can't really say I know whats exactly going on here, just followed the steps from
https://www.youtube.com/watch?v=18VcnYN5_LM
and applied them to my project
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
//    String[] names,descriptions;
//    Integer[] stocks;
//    Double[] prices;
    Context context;
    ArrayList<Item> items;
    private OnItemListener mOnItemListener;

//    public ItemAdapter(Context context, String[] name, String[] description, Integer[] stock, Double[] price){
//        this.context = context;
//        this.names = name;
//        this.descriptions = description;
//        this.stocks = stock;
//        this.prices = price;
//
//    }
    public ItemAdapter(Context context, ArrayList<Item> items,OnItemListener onItemListener){
        this.items=items;
        this.context=context;
        this.mOnItemListener=onItemListener;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_row,parent,false);
        return new MyViewHolder(view,mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.nameText.setText("name: "+names[position]);
//        holder.priceText.setText("price: "+prices[position]);
//        holder.stockText.setText("stock: "+stocks[position]);
//        holder.descriptionText.setText("description: "+descriptions[position]);
        holder.nameText.setText("name: "+items.get(position).getItemName());
        holder.priceText.setText("price: "+items.get(position).getItemPrice());
        holder.stockText.setText("stock: "+items.get(position).getItemStock());
        holder.descriptionText.setText("description: "+items.get(position).getItemDescription());

    }

    @Override
    public int getItemCount() {
//        return names.length;
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameText;
        TextView descriptionText;
        TextView stockText;
        TextView priceText;
        OnItemListener onItemListener;


        public MyViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            nameText=itemView.findViewById(R.id.itemNameCardDisplay);
            descriptionText=itemView.findViewById(R.id.itemDescriptionCardDisplay);
            stockText=itemView.findViewById(R.id.itemStockCardDisplay);
            priceText=itemView.findViewById(R.id.itemPriceCardDisplay);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }
    public interface OnItemListener{
        void onItemClick(int position);

    }
}
