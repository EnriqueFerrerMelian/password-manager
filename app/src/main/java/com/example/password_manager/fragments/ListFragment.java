package com.example.password_manager.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.password_manager.R;
import com.example.password_manager.database.DatabaseClient;
import com.example.password_manager.database.UserPassword;
import com.example.password_manager.databinding.FragmentListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ListFragment extends Fragment {
    private static FragmentListBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sortSpinner.setAdapter(adapter);
        binding.sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        // Order by plataforma
                        loadSortedData("platform");
                        break;
                    case 1:
                        // Order by entidad
                        loadSortedData("account");
                        break;
                    /*case 2:
                        // Order by ID
                        loadSortedData("id");
                        break;*/
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
    }
    // Method to load the table
    private void getAll() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<UserPassword> userPasswords = DatabaseClient.getInstance(requireContext())
                    .getAppDatabase()
                    .userPasswordDao()
                    .getAllUserPasswords();
            requireActivity().runOnUiThread(() -> {
                // Aquí es donde puedes modificar la UI
                for (UserPassword userPassword: userPasswords){
                    binding.list.append(userPassword.getPlatform() + ": \n" + userPassword.getAccount() + " - " + userPassword.getPassword() + "\n\n");
                }
            });

        });
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
                    break;
                case "account":
                    userPasswords = DatabaseClient.getInstance(requireContext())
                            .getAppDatabase()
                            .userPasswordDao()
                            .getAllUserPasswordsByAccount();
                    break;
                default:
                    userPasswords = DatabaseClient.getInstance(requireContext())
                            .getAppDatabase()
                            .userPasswordDao()
                            .getAllUserPasswords(); // default value
                    break;
            }

            // Update view on the main thread
            requireActivity().runOnUiThread(() -> {
                // Clean and update ui with ordered data
                binding.list.setText(""); // clean previus view
                for (UserPassword userPassword : userPasswords) {
                    binding.list.append(userPassword.getPlatform() + ": \n" + userPassword.getAccount() + " - " + userPassword.getPassword() + "\n\n");
                }
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
                binding.list.setText(""); // Clean previus view
                if (userPasswords.isEmpty()) {
                    binding.list.append("No data found\n");
                } else {
                    for (UserPassword userPassword : userPasswords) {
                        binding.list.append(userPassword.getPlatform() + ": \n" + userPassword.getAccount() + " - " + userPassword.getPassword() + "\n\n");
                    }
                }
            });
        });
    }
}