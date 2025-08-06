package com.example.keymasterdegozer.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keymasterdegozer.R;
import com.example.keymasterdegozer.database.DatabaseClient;
import com.example.keymasterdegozer.database.UserPassword;

import java.util.List;
import java.util.concurrent.Executors;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder>{
    List<UserPassword> passwordList;
    private final Context context;

    public PasswordAdapter (Context context, List<UserPassword> passwordList) {
        this.passwordList = passwordList;
        this.context = context;
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
        String platform = userPassword.getPlatform().toLowerCase();
        switch (platform) {
            case "facebook":
                holder.platformIcon.setImageResource(R.drawable.ic_facebook);
                break;
            case "google":
                holder.platformIcon.setImageResource(R.drawable.ic_google);
                break;
            case "tumblr":
                holder.platformIcon.setImageResource(R.drawable.ic_tumblr);
                break;
            case "outlook":
                holder.platformIcon.setImageResource(R.drawable.ic_outlook);
                break;
            case "trello":
                holder.platformIcon.setImageResource(R.drawable.ic_trello);
                break;
            default:
                holder.platformIcon.setImageResource(R.drawable.ic_platform);
        }
        holder.btnEdit.setOnClickListener(v -> {
            UserPassword item = passwordList.get(holder.getAdapterPosition());

            // Crear contenedor
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 10); // Ajusta el padding si hace falta

            EditText inputPlatform = new EditText(context);
            inputPlatform.setHint("Plataforma");
            inputPlatform.setText(item.platform);
            layout.addView(inputPlatform);

            EditText inputAccount = new EditText(context);
            inputAccount.setHint("Cuenta");
            inputAccount.setText(item.account);
            layout.addView(inputAccount);

            EditText inputPassword = new EditText(context);
            inputPassword.setHint("Nueva contraseña");
            inputPassword.setText(item.password);
            layout.addView(inputPassword);


// Construir el diálogo
            new AlertDialog.Builder(context)
                    .setTitle("Editar registro")
                    .setView(layout)
                    .setPositiveButton("Guardar", (dialog, which) -> {
                        String newPlatform = inputPlatform.getText().toString().trim();
                        String newAccount = inputAccount.getText().toString().trim();
                        String newPassword = inputPassword.getText().toString().trim();

                        if (!newPassword.isEmpty() && !newAccount.isEmpty()) {
                            item.platform = newPlatform;
                            item.account = newAccount;
                            item.password = newPassword;

                            Executors.newSingleThreadExecutor().execute(() -> {
                                DatabaseClient.getInstance(context)
                                        .getAppDatabase()
                                        .userPasswordDao()
                                        .update(item);

                                new Handler(Looper.getMainLooper()).post(() -> {
                                    notifyItemChanged(holder.getAdapterPosition());
                                });
                            });
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        holder.btnDelete.setOnClickListener(v -> {
            UserPassword item = passwordList.get(holder.getAdapterPosition());


            Executors.newSingleThreadExecutor().execute(() -> {
                DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .userPasswordDao()
                        .delete(item);

                // Quitar de la lista y actualizar UI
                new Handler(Looper.getMainLooper()).post(() -> {
                    passwordList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                });
            });
        });
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    public static class  PasswordViewHolder extends RecyclerView.ViewHolder {
        TextView account;
        TextView source;
        TextView password;
        ImageButton btnEdit;
        ImageButton btnDelete;
        ImageView platformIcon;

        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            account = itemView.findViewById(R.id.account);
            source = itemView.findViewById(R.id.source);
            password = itemView.findViewById(R.id.password);
            platformIcon = itemView.findViewById(R.id.platformIcon);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
