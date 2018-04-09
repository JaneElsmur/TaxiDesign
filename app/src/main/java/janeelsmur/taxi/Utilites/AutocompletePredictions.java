package janeelsmur.taxi.Utilites;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;


public class AutocompletePredictions extends AsyncTask<String, Integer, Integer>{

    //Constructor
    private String query;
    private GoogleApiClient googleApiClient;
    private LatLngBounds latLngBounds;
    private AutocompleteFilter autocompleteFilter;
    private ArrayList<String> results;
    private ArrayList<String> executedResults;

    public AutocompletePredictions(String query, GoogleApiClient googleApiClient, LatLngBounds bounds, AutocompleteFilter autocompleteFilter){
        this.query = query;
        this.googleApiClient = googleApiClient;
        this.latLngBounds = bounds;
        this.autocompleteFilter = autocompleteFilter;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        PendingResult<AutocompletePredictionBuffer> apiResult = Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query, latLngBounds, autocompleteFilter);
        AutocompletePredictionBuffer autocompletePredictionBuffer = apiResult.await(); //Получение буфера
        if (autocompletePredictionBuffer.getStatus().isSuccess()) {
            results = new ArrayList<>();
            try {
                String address = (String) autocompletePredictionBuffer.get(1).getPrimaryText(null);
                Log.d("Result", "doInBackground: " + address);
                results.add(address);

            } catch (IllegalStateException e) {
                Log.d("PredictionBuffer", "Буфер пустой, адреса не найдены");
            }
            autocompletePredictionBuffer.release(); //Освобождение памяти буфера
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        executedResults = results;
        Log.d("AutocompleteManager", "onPostExecute: executed!");
    }

    //Возвращение результатов
    public ArrayList<String> getResults() {
        return executedResults;
    }
}
