package com.mwbtech.utilityapp.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "mwb-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void saveObjectToSharedPreference(String serializedObjectKey, Object object) {
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        editor.putString(serializedObjectKey, serializedObject);
        pref.edit().commit();
        editor.apply();
    }

    public <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        if (pref.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(pref.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    public void clearPreferences(Context context){
        editor.clear();
        editor.commit();
    }

}
