package janeelsmur.taxi.Utilites;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CoordinatesAPIService {

    private static final String TAG = "CoordinatesAPIService";

    public static List<Address> getAddressListFromCoordinates(Context context, double latitude, double longtitude){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            return geocoder.getFromLocation(latitude, longtitude, 1);
        } catch (IOException e){
            Log.d(TAG, "getAddressFromCoordinates: " + Log.getStackTraceString(e));
            return null;
        }
    }

    public static String getStringFromAddressList(List<Address> list) {
        Address address = list.get(0);
        //Если аддрес содержит 4 строки-индекса (а значит нашелся полный аддрес), то возвращаем его
        if(validateAddress(address)) {
            Log.d(TAG, "getStringFromAddressList: " + address.getAddressLine(0));
            return address.getAddressLine(0);
        }
        else return null;
    }

    private static boolean validateAddress(Address address){
        return address.getMaxAddressLineIndex() == 4;
    }
}
