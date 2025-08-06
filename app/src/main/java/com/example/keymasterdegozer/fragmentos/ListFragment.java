package com.example.keymasterdegozer.fragmentos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.keymasterdegozer.R;
import com.example.keymasterdegozer.adapters.PasswordAdapter;
import com.example.keymasterdegozer.database.DatabaseClient;
import com.example.keymasterdegozer.database.UserPassword;
import com.example.keymasterdegozer.databinding.FragmentListBinding;

import java.util.List;
import java.util.concurrent.Executors;

public class ListFragment extends Fragment {
    private FragmentListBinding binding;
    private PasswordAdapter passwordAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

    @SuppressLint("NotifyDataSetChanged")
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
            requireActivity().runOnUiThread(() -> {
                if (!isAdded()) return;
                passwordAdapter = new PasswordAdapter(requireContext(), userPasswords);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerView.setAdapter(passwordAdapter);
                passwordAdapter.notifyDataSetChanged();
            });
        });
    }
    // Method to search by platform
    @SuppressLint("NotifyDataSetChanged")
    private void searchByPlatform(String platform) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Make a query to the database
            List<UserPassword> userPasswords = DatabaseClient.getInstance(requireContext())
                    .getAppDatabase()
                    .userPasswordDao()
                    .searchByPlatform(platform);

            // update ui with data on main thread
            requireActivity().runOnUiThread(() -> {
                if (!isAdded()) return;
                passwordAdapter = new PasswordAdapter(requireContext(), userPasswords);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerView.setAdapter(passwordAdapter);
                passwordAdapter.notifyDataSetChanged();
            });
        });
    }
}