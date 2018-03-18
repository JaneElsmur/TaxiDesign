package janeelsmur.taxi;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import janeelsmur.taxi.Utilites.CoordinatesAPIService;
import janeelsmur.taxi.Utilites.ListViewAdapter;
import janeelsmur.taxi.Utilites.SharedPrefManager;
import janeelsmur.taxi.fragments.FragmentLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnterLocation extends AppCompatActivity implements
        View.OnClickListener,
        ListView.OnItemClickListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMarkerClickListener,
        TextWatcher {

    private final DialogFragment favoritesDialog = new DialogFavorites();

    //Сохраненные значения
    private int mode;
    private int id;

    private GoogleMap gMap;
    private SupportMapFragment supportMapFragment;
    private MarkerOptions markerOptions;
    private Marker marker;
    private String address; //Адресс, который указывается на карте

    private LinearLayout favoriteButtonImageView;
    private ListView addressesListView;
    private ImageView closeSearchBtn;
    private EditText searchEditText;

    //Клавиатура
    InputMethodManager inputMethodManager;

    private ArrayList<String> arrayList = new ArrayList<>(); //Список с адресами
    private ListViewAdapter listViewAdapter; //Адаптер ListView
    private final String[] testAddresses = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_location);

        mode = getIntent().getExtras().getInt("mode");
        id = getIntent().getExtras().getInt("id");

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //Google Maps
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.enteringLocationToolbar);
        toolbar.setTitle(R.string.address_entering);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        favoriteButtonImageView = (LinearLayout) findViewById(R.id.favoritesButton);
        addressesListView = (ListView) findViewById(R.id.addressesListView);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        closeSearchBtn = (ImageView) findViewById(R.id.cancelSearchingBtn);

        searchViewInitialize(testAddresses);

        searchEditText.addTextChangedListener(this);
        searchEditText.setOnClickListener(this);
        closeSearchBtn.setOnClickListener(this);
        favoriteButtonImageView.setOnClickListener(this);
        addressesListView.setOnItemClickListener(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap; //Объект Google Maps
        gMap.setOnMapClickListener(this);
        gMap.setOnMarkerDragListener(this);

        //Установка масштаба (корректного при запуске, минимального и максимальго)
        gMap.setMaxZoomPreference(18.0f);
        gMap.setMinZoomPreference(10.0f);
        gMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));

        //Маркер на Казани
        LatLng kazan = new LatLng(55.78874, 49.12214);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(kazan));

        //Установка границ карты (в данном случае - Казань)
        LatLng firstPoint = new LatLng(55.66318, 48.84705);
        LatLng secondPoint = new LatLng(55.94413, 49.39499);
        LatLngBounds boundsKazan = new LatLngBounds(firstPoint, secondPoint);
        gMap.setLatLngBoundsForCameraTarget(boundsKazan);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Изменение позиции маркера
        if(marker != null ) marker.remove(); //Удаление старого маркера, если он был
        markerOptions = new MarkerOptions().position(latLng).draggable(true);
        marker = gMap.addMarker(markerOptions); //Объект маркера

        // Перевод координат в адрес
        List<Address> addressList = CoordinatesAPIService.getAddressListFromCoordinates(this, latLng.latitude, latLng.longitude);
        address = CoordinatesAPIService.getStringFromAddressList(addressList);

        // Открытие SearchView с полученным адресом, если он не null
        if(address != null) {
            searchEditText.setText(address);
        }
    }

    /*-----------Интерфейс OnMarkerDragListener--------------*/
    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        List<Address> addressList = CoordinatesAPIService.getAddressListFromCoordinates(this, marker.getPosition().latitude, marker.getPosition().longitude);
        address = CoordinatesAPIService.getStringFromAddressList(addressList);
        if(address != null) {
            searchEditText.setText(address);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    /*-------------------------------------------------------*/



    /*----------Интерфейс TextWatcher--------------*/

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setSearchMode(true);
        listViewAdapter.filter(s.toString());
    }
    /*------------------------------------------------*/




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //Если пользовател в режиме поиска, выходим из него
        finish();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        switch (v.getId()) {
            case R.id.favoritesButton:
                favoritesDialog.show(fm, "favoritesDialog");
                break;

            case R.id.cancelSearchingBtn:
                setSearchMode(false);
                break;

            case R.id.searchEditText:
                setSearchMode(true);
                break;
        }
    }

    private void searchViewInitialize(String[] addresses) {
        //Запись адресов из массива в список arrayList
        Collections.addAll(arrayList, addresses);
        //Инициализация адаптера
        listViewAdapter = new ListViewAdapter(this, arrayList);
        addressesListView.setAdapter(listViewAdapter);
    }

    //Включение, выключение режима поиска
    private void setSearchMode(boolean isSearchMode) {
        if(isSearchMode) {
            addressesListView.setVisibility(View.VISIBLE);
            closeSearchBtn.setVisibility(View.VISIBLE);
        }
        else {
            addressesListView.setVisibility(View.GONE);
            closeSearchBtn.setVisibility(View.GONE);
            inputMethodManager.hideSoftInputFromWindow(closeSearchBtn.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /*Интерфейс ListView. Здесь идет обработка нажатия на адресс, после чего он сохраняется в Shared Preferences*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Здесь должно открываться окно, где пользователь может ввести подъезд и объект, к которому подъехать и комментарий.

//
//        TextView textView = view.findViewById(R.id.addressInSearchTextView);
//
//        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
//        String savedLocation = textView.getText().toString();
//
//        switch (mode){
//            //Если режим поиска адреса был "из", то запись адреса соответсвтенно будет в "из"
//            case FragmentLocation.MODE_FROM:
//                sharedPrefManager.saveFromLocation(savedLocation);
//                break;
//
//            case FragmentLocation.MODE_WHERE:
//                sharedPrefManager.saveWhereLocation(savedLocation, this.id);
//                break;
//        }
//        finish();
//        Log.d("SavedLocation", "onItemClick: " + "mode:" + mode + " id:" + this.id + " location:" + savedLocation);
    }
    /*------------------------------------*/

}
