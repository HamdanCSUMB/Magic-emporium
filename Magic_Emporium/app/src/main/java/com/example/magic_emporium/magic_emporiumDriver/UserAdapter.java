package com.example.magic_emporium.magic_emporiumDriver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magic_emporium.R;
import com.example.magic_emporium.magicDataBase.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    Context context;            //holding a context like this is no bueno, can cause memory leaks
    ArrayList<User> users;
    private OnItemListener mOnItemListener;

    public UserAdapter(Context context, ArrayList<User> users, OnItemListener onItemListener){
        this.users = users;
        this.context =context;
        this.mOnItemListener=onItemListener;

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_row,parent,false);
        return new UserViewHolder(view,mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.nameText.setText("Name: "+users.get(position).getUserName());
        holder.loginText.setText("Login: "+users.get(position).getLogin());
        holder.passwordText.setText("Password: "+users.get(position).getPassword());
        if(users.get(position).isAdmin()){
            holder.adminStatusText.setText(R.string.adminstatus_admin);
        }else{
            holder.adminStatusText.setText(R.string.adminstatus_user);
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nameText;
        TextView loginText;
        TextView passwordText;
        TextView adminStatusText;
        OnItemListener onItemListener;

        public UserViewHolder(@NonNull View itemView,OnItemListener onItemListener) {
            super(itemView);
            nameText=itemView.findViewById(R.id.userUserNameCardDisplay);
            loginText=itemView.findViewById(R.id.userLoginCardDisplay);
            passwordText=itemView.findViewById(R.id.userPasswordCardDisplay);
            adminStatusText=itemView.findViewById(R.id.userAdminStatusCardDisplay);
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
