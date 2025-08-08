package com.example.keymasterdegozer.fragmentos;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.keymasterdegozer.R;
import com.example.keymasterdegozer.adapters.IconSpinnerAdapter;
import com.example.keymasterdegozer.database.DatabaseClient;
import com.example.keymasterdegozer.database.UserPassword;
import com.example.keymasterdegozer.databinding.FragmentSaveBinding;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.Executors;

public class SaveFragment extends Fragment {
    private int selectedIconResId;
    private FragmentSaveBinding binding;
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
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        // Lista de drawables
        Integer[] iconResIds = {
                R.drawable.ic_google,
                R.drawable.ic_facebook,
                R.drawable.ic_server,
                R.drawable.ic_platform,
                R.drawable.ic_outlook,
                R.drawable.ic_tumblr,
                R.drawable.ic_trello
        };
        IconSpinnerAdapter adapter = new IconSpinnerAdapter(requireContext(), iconResIds);
        binding.iconSpinner.setAdapter(adapter);

        // Valor inicial
        selectedIconResId = iconResIds[0];

        binding.iconSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIconResId = iconResIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
                String accountText = binding.account.getText() != null ? binding.account.getText().toString() : "";
                String passwordText = binding.password.getText() != null ? binding.password.getText().toString() : "";
                String platformText = binding.platform.getText() != null ? binding.platform.getText().toString() : "";
                String notaText = binding.nota.getText() != null ? binding.nota.getText().toString() : "";

                if (platformText.isEmpty() || accountText.isEmpty() || passwordText.isEmpty()) {
                    writeToast("Ningún espacio obligatorio puede estar vacío.", requireContext());
                    return;
                }
                UserPassword userPassword = new UserPassword(
                        accountText,
                        passwordText,
                        platformText,
                        selectedIconResId);
                userPassword.setNotas(notaText);
                savePassword(userPassword);
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

    private void copyToClipboard(String password) {
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Contraseña", password);
        clipboard.setPrimaryClip(clip);
        writeToast("Elemento copiado al portapapeles.", requireContext());
    }

    public static void writeToast(String texto, Context context){
        Toast.makeText(context, texto, Toast.LENGTH_LONG).show();
    }

    private void savePassword(UserPassword userPassword) {
        Executors.newSingleThreadExecutor().execute(() -> {
            DatabaseClient.getInstance(requireContext()).getAppDatabase()
                    .userPasswordDao().insert(userPassword);
            requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Guardado!", Toast.LENGTH_SHORT).show());
        });
        binding.platform.setText("");
        binding.account.setText("");
        binding.password.setText("");
        binding.nota.setText("");
        System.out.println(userPassword);
    }

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

    public void saveSettings(){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HELPMESSAGE1, false);
        editor.apply();
    }
}