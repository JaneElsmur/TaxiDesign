package janeelsmur.taxi.Utilites;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Set;

public class SharedPrefManager {

    public static final String FROM_LOCATION = "fromLocation";
    public static final String WHERE_LOCATION = "whereLocation";

    private SharedPreferences sharedPreferences;

    public SharedPrefManager(Context context){
        sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
    }

    public void saveFromLocation(Set<String> fromLocation){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(FROM_LOCATION, fromLocation);
        editor.commit();
    }

    public void saveWhereLocation(Set<String> whereLocation, int id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(WHERE_LOCATION + id, whereLocation);
        editor.commit();
    }

    public void deleteWhereLocation(int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(WHERE_LOCATION + id);
        editor.commit();
    }

    //Считать сохраненную локацию типа "куда"
    public Set<String> getWhereLocation(int id) {
        return sharedPreferences.getStringSet(WHERE_LOCATION + id, null);
    }

    //Считать сохраненную локацию типа "откуда"
    public Set<String> getFromLocation() {
        return sharedPreferences.getStringSet(FROM_LOCATION, null);
    }

}
