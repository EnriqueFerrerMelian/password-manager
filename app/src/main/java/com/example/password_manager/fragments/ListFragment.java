package com.example.password_manager.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.password_manager.R;
import com.example.password_manager.adapters.PasswordAdapter;
import com.example.password_manager.database.DatabaseClient;
import com.example.password_manager.database.UserPassword;
import com.example.password_manager.databinding.FragmentListBinding;
import com.example.password_manager.util.Password;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ListFragment extends Fragment {
    private static FragmentListBinding binding;
    private PasswordAdapter passwordAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        // spinner management
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sortSpinner.setAdapter(adapter);
        binding.sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        // Order by default
                        loadSortedData("default");
                        break;
                    case 1:
                        // Order by plataforma
                        loadSortedData("platform");
                        break;
                    case 2:
                        // Order by entidad
                        loadSortedData("account");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //getAll();
            }
        });
        binding.searchButton.setOnClickListener(v -> {
            String query = binding.searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                searchByPlatform(query); // Call search method
            }
        });


        return binding.getRoot();
    }

    private void loadSortedData(String orderBy) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<UserPassword> userPasswords;

            // Check the database by criteria
            switch (orderBy) {
                case "platform":
                    userPasswords = DatabaseClient.getInstance(requireContext())
                            .getAppDatabase()
                            .userPasswordDao()
                            .getAllUserPasswordsByPlatform();
                    System.out.println("Printing platform: ");
                    break;
                case "account":
                    System.out.println("Printing account");
                    userPasswords = DatabaseClient.getInstance(requireContext())
                            .getAppDatabase()
                            .userPasswordDao()
                            .getAllUserPasswordsByAccount();
                    System.out.println("Printing account: ");
                    break;
                default:
                    System.out.println("Printing default");
                    userPasswords = DatabaseClient.getInstance(requireContext())
                            .getAppDatabase()
                            .userPasswordDao()
                            .getAllUserPasswords(); // default value
                    System.out.println("Printing default: " + userPasswords.size());
                    break;
            }
            requireActivity().runOnUiThread(() -> {
                System.out.println("passwordAdapter: " + userPasswords.size());
                passwordAdapter = new PasswordAdapter(userPasswords);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerView.setAdapter(passwordAdapter);
                passwordAdapter.notifyDataSetChanged();

            });
        });
    }
    // Method to search by platform
    private void searchByPlatform(String platform) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Make a query to the database
            List<UserPassword> userPasswords = DatabaseClient.getInstance(requireContext())
                    .getAppDatabase()
                    .userPasswordDao()
                    .searchByPlatform(platform);

            // update ui with data on main thread
            requireActivity().runOnUiThread(() -> {
                passwordAdapter = new PasswordAdapter(userPasswords);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerView.setAdapter(passwordAdapter);
                passwordAdapter.notifyDataSetChanged();
            });
        });
    }
}