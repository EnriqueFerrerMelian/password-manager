package com.example.password_manager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.password_manager.databinding.ActivityContainerBinding;
import com.example.password_manager.databinding.ActivityMainBinding;
import com.example.password_manager.fragments.ListFragment;
import com.example.password_manager.fragments.SaveFragment;

public class ActivityContainer extends AppCompatActivity {
    ActivityContainerBinding binding;
    private static Fragment fragmentoDesechable = null;
    // Instanciate DataStore
    private static final String DATASTORE_NAME = "password_list";
    private final Context context = this;
    RxDataStore<Preferences> dataStore = new RxPreferenceDataStoreBuilder(context, DATASTORE_NAME).build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //deleteDatabase("UserPasswordDatabase");
        replaceFragment(new SaveFragment());
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new SaveFragment());
            }
        });
        binding.listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new ListFragment());
            }
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
}