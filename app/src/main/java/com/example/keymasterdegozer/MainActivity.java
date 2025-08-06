package com.example.keymasterdegozer;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.keymasterdegozer.databinding.ActivityMainBinding;
import com.example.keymasterdegozer.fragmentos.ListFragment;
import com.example.keymasterdegozer.fragmentos.SaveFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    // Instance DataStore
    private static final String DATASTORE_NAME = "password_list";
    private final Context context = this;
    RxDataStore<Preferences> dataStore = new RxPreferenceDataStoreBuilder(context, DATASTORE_NAME).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new SaveFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.save_button) {
                replaceFragment(new SaveFragment());
            }
            if (id == R.id.list_button) {
                replaceFragment(new ListFragment());
            }
            return true;
        });
        /*binding.saveButton.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }

    public void replaceFragment(Fragment fragmento){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        /*if(fragmentoDesechable!=null){
            fragmentTransaction.remove(fragmentoDesechable);
        }*/
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);
        //fragmentoDesechable = fragmento;
        fragmentTransaction.commit();
    }
}