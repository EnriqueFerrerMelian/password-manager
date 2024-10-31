package com.example.password_manager.fragments;

import static android.content.Context.MODE_PRIVATE;
import static android.icu.lang.UProperty.LOWERCASE;
import static androidx.core.content.ContextCompat.getSystemService;
import static java.util.FormattableFlags.UPPERCASE;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.password_manager.MainActivity;
import com.example.password_manager.database.DatabaseClient;
import com.example.password_manager.database.UserPassword;
import com.example.password_manager.databinding.FragmentSaveBinding;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.Executors;

public class SaveFragment extends Fragment {
    private static FragmentSaveBinding binding;
    SharedPreferences sharedPreferences;
    private static final String SP_NAME= "credentials";
    private static final String HELPMESSAGE1 = "helpmessage1";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";
    // Random number generator, more secure than random
    private static final SecureRandom random = new SecureRandom();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSaveBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = requireActivity().getSharedPreferences(SP_NAME,MODE_PRIVATE);
        boolean helpMess1= sharedPreferences.getBoolean(HELPMESSAGE1, true);
        if(!helpMess1){
            binding.helpMessageCard1.setVisibility(View.GONE);
        }
        helpAssistant();
        binding.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Objects.requireNonNull(binding.password.getText()).toString().isEmpty()) {
                    copyToClipboard(binding.password.getText().toString().trim());
                }

            }
        });
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.requireNonNull(binding.platform.getText()).toString().isEmpty() ||
                        Objects.requireNonNull(binding.account.getText()).toString().isEmpty() ||
                        Objects.requireNonNull(binding.password.getText()).toString().isEmpty()) {
                    writeToast("Inputs can't be empty", requireContext());
                } else {
                    savePassword(binding.account.getText().toString(), binding.password.getText().toString(), binding.platform.getText().toString().toUpperCase());
                }
            }
        });
        binding.generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean includeSpecial = binding.includeSpecialChars.isChecked();
                String password = generatePassword(includeSpecial);
                binding.password.setText(password);
            }
        });

    }

    public static void writeToast(String texto, Context context){
        Toast.makeText(context, texto, Toast.LENGTH_LONG).show();
    }
    // Method to save an item on the Database
    private void savePassword(String account, String password, String platform) {
        Executors.newSingleThreadExecutor().execute(() -> {
            UserPassword userPassword = new UserPassword(account, password, platform);
            DatabaseClient.getInstance(requireContext()).getAppDatabase()
                    .userPasswordDao().insert(userPassword);
            requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Item saved", Toast.LENGTH_SHORT).show());
        });
        binding.platform.setText("");
        binding.account.setText("");
        binding.password.setText("");
    }
    // Method to create a random password
    private static String generatePassword(boolean includeSpecial) {
        String characterSet = UPPERCASE + LOWERCASE + NUMBERS;

        if (includeSpecial) {
            characterSet += SPECIAL_CHARACTERS;
        }

        StringBuilder password = new StringBuilder(15);
        for (int i = 0; i < 15; i++) {
            int randomIndex = random.nextInt(characterSet.length());
            password.append(characterSet.charAt(randomIndex));
        }

        return password.toString();
    }

    private void copyToClipboard(String password) {
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Contraseña", password);
        clipboard.setPrimaryClip(clip);
        writeToast("copied to clipboard", requireContext());
    }

    public void helpAssistant(){

        androidx.cardview.widget.CardView[] helpCards = {
                binding.helpMessageCard1,
                binding.helpMessageCard2,
                binding.helpMessageCard3,
                binding.helpMessageCard4,
                binding.helpMessageCard5
        };

        android.widget.ImageButton[] closeButtons = {
                binding.closeHelpButton1,
                binding.closeHelpButton2,
                binding.closeHelpButton3,
                binding.closeHelpButton4,
                binding.closeHelpButton5
        };

        for (int i = 0; i < closeButtons.length; i++) {
            final int index = i;

            closeButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helpCards[index].setVisibility(View.GONE);
                    saveSettings();
                    if (index + 1 < helpCards.length) {
                        helpCards[index + 1].setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
    public void saveSettings(){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HELPMESSAGE1, false);
        editor.apply();
    }
}