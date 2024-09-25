package com.example.password_manager.util;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;

public class DataStoreUtil {
    private final Preferences.Key<String> USER_KEY = PreferencesKeys.stringKey("user_key");
    private final Preferences.Key<String> PASSWORD_KEY = PreferencesKeys.stringKey("password_key");


    public Preferences.Key<String> getUSER_KEY() {
        return USER_KEY;
    }

    public Preferences.Key<String> getPASSWORD_KEY() {
        return PASSWORD_KEY;
    }
}
