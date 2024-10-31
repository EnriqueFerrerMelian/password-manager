package com.example.password_manager;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.password_manager.databinding.ActivityMainBinding;
import com.example.password_manager.util.DataStoreUtil;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import kotlinx.coroutines.flow.Flow;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Context context = this;
    private static Fragment fragmentoDesechable = null;
    SharedPreferences sharedPreferences;
    private static final String SP_NAME= "credentials";
    private static final String USER_KEY= "user_key";
    private static final String PASSWORD_KEY= "password_key";
    private static final String HELPMESSAGE = "helpmessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        sharedPreferences = getSharedPreferences(SP_NAME,MODE_PRIVATE);
        String user = sharedPreferences.getString(USER_KEY, null);
        String password = sharedPreferences.getString(PASSWORD_KEY, null);
        boolean helpMess= sharedPreferences.getBoolean(HELPMESSAGE, true);
        if(!helpMess){
            binding.helpMessageCard.setVisibility(View.GONE);
        }
        setContentView(binding.getRoot());

        binding.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Objects.requireNonNull(binding.user.getText()).toString().isEmpty() || !Objects.requireNonNull(binding.password.getText()).toString().isEmpty()){
                    if (user == null || password == null){
                        savePreferences(binding.user.getText().toString(), Objects.requireNonNull(binding.password.getText()).toString());
                        goHome();
                    } else {
                        if (Objects.requireNonNull(binding.user.getText()).toString().equals(user) &&
                                Objects.requireNonNull(binding.password.getText()).toString().equals(password)) {
                            writeToast("Welcome.", context);
                            goHome();
                        } else {
                            writeToast("Wrong user or password.", context);
                        }
                    }
                } else {
                    writeToast("You can't have an empty input.", context);
                }
            }
        });
        binding.closeHelpButton.setOnClickListener(v->{
            closeHelpMessage();
            binding.helpMessageCard.setVisibility(View.GONE);
        });

    }
    public static void writeToast(String texto, Context context){
        Toast.makeText(context, texto, Toast.LENGTH_LONG).show();
    }
    public void replaceFragment(Fragment fragmento){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragmentoDesechable!=null){
            fragmentTransaction.remove(fragmentoDesechable);
        }
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);
        fragmentoDesechable = fragmento;
        fragmentTransaction.commit();
    }
    public void goHome(){
        Intent intent = new Intent(MainActivity.this, ActivityContainer.class);
        startActivity(intent);
    }
    public void savePreferences(String user, String password){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_KEY, binding.user.getText().toString());
        editor.putString(PASSWORD_KEY, binding.password.getText().toString());
        editor.commit();
        writeToast("Welcome " + user, context);
    }
    public void closeHelpMessage(){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HELPMESSAGE, false);
        editor.commit();
    }
}