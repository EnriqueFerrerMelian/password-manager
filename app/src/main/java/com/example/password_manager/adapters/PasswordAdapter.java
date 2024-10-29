package com.example.password_manager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.password_manager.R;
import com.example.password_manager.database.UserPassword;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder>{
    List<UserPassword> passwordList;
    public PasswordAdapter (List<UserPassword> passwordList) {
        this.passwordList = passwordList;
    }
    @NonNull
    @Override
    public PasswordAdapter.PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_password, parent, false);
        return new PasswordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordAdapter.PasswordViewHolder holder, int position) {
        UserPassword userPassword = passwordList.get(position);
        holder.account.setText(userPassword.getAccount());
        holder.source.setText(userPassword.getPlatform());
        holder.password.setText(userPassword.getPassword());
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    public static class  PasswordViewHolder extends RecyclerView.ViewHolder {
        TextView account;
        TextView source;
        TextView password;

        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            account = itemView.findViewById(R.id.account);
            source = itemView.findViewById(R.id.source);
            password = itemView.findViewById(R.id.password);
        }
    }
}
