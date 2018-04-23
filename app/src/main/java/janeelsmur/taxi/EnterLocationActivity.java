package janeelsmur.taxi;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import janeelsmur.taxi.Utilites.CoordinatesAPIService;

import java.util.List;

public class EnterLocationActivity extends AppCompatActivity implements
        View.OnClickListener,
        ListView.OnItemClickListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMarkerClickListener
        //TextWatcher,
        {

    //Активность с избранными адресами возвращает этот результат, если адрес был выбран из избранных
    public static final int RESULT_ADDRESS_PICKED_FROM_FAVORITES = 10000;

    //Сохраненные значения
    private int mode;
    private int id;

    //Диалоговое окно с окончательным выбором адреса
    private DialogEnterAddressWhenChosen addressEnteringDialog;

    //Карта
    private GoogleMap gMap;
    private SupportMapFragment supportMapFragment;
    private MarkerOptions markerOptions;
    private Marker marker;
    private String address; //Адресс, который указывается на карте
    private LatLngBounds latLngBounds; //Края карты

    /*Собственное предугадывание места на Google API.
            Пока отключено, так как есть фрагмент гугла
    private GoogleApiClient googleApiClient;
    //Поток с прогрузкой адресов
    private AutocompletePredictions autocompletePredictionsThread;
    //Тригер создания нового потока
    private boolean isAutoPredThreadCreated = false; */

    //Фильтер поиска
    private AutocompleteFilter autocompleteFilter;
    //Фрагмент поиска мест. Пока будет использоваться он. Если нужно будет изменить, уберите фрагмент и код работы с ним,
    //                                                                           так же выствите в лэйауте видимость search_field и уберите комментарии "Пока отключено"
    private PlaceAutocompleteFragment placeAutocompleteFragment;

    //Аддрес, который был выбран последним. Он идет в диалоговое окно с окончательным выбором адреса!!!
    private String latestPlace;

    private LinearLayout favoriteButtonImageView;
    private ListView addressesListView;
    private ImageView closeSearchBtn;
    private EditText searchEditText;
    private FloatingActionButton pickAddressFab;

    //Клавиатура
    InputMethodManager inputMethodManager;

    //Пока отключено, так как есть фрагмент гугла
    //Тригер для блокировки поиска по адресу. Активируется, если адресс выбран через карту.
    //Деактивируется, если текст мануально введен в поиск
    //private boolean isPickedByMap = false;

    //ListView Пока отключено, так как есть фрагмент гугла
    //private ListViewAdapter listViewAdapter; //Адаптер ListView

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
        //Установка границ карты (в данном случае - Казань)
        LatLng firstPoint = new LatLng(55.66318, 48.84705);
        LatLng secondPoint = new LatLng(55.94413, 49.39499);
        latLngBounds = new LatLngBounds(firstPoint, secondPoint);

//        //Client for predictions. Пока отключено, так как есть фрагмент гугла
//        googleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, this)
//                .build();

        //Фильтер для предугадывания
        autocompleteFilter = new AutocompleteFilter
                .Builder()
                .setCountry("RU")
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();

        //Тулбар
        Toolbar toolbar = (Toolbar) findViewById(R.id.enteringLocationToolbar);
        toolbar.setTitle(R.string.address_entering);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        favoriteButtonImageView = (LinearLayout) findViewById(R.id.favoritesButton);
        addressesListView = (ListView) findViewById(R.id.addressesListView);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        closeSearchBtn = (ImageView) findViewById(R.id.cancelSearchingBtn);
        pickAddressFab = (FloatingActionButton) findViewById(R.id.pickAddressFab); pickAddressFab.hide();

        //Выставление слушателей
        searchEditText.setOnClickListener(this);
        closeSearchBtn.setOnClickListener(this);
        pickAddressFab.setOnClickListener(this);
        //searchEditText.addTextChangedListener(this); Пока отключено, так как есть фрагмент гугла
        favoriteButtonImageView.setOnClickListener(this);
        addressesListView.setOnItemClickListener(this);

        //Инициализация адаптера ListView в SearchView. Пока отключено
        //searchViewInitialize();

        //Place autocomplete fragment
        placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        placeAutocompleteFragment.setHint(getResources().getString(R.string.search));
        placeAutocompleteFragment.setFilter(autocompleteFilter);
        placeAutocompleteFragment.setBoundsBias(latLngBounds);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng placePosition = place.getLatLng();
                // Изменение позиции маркера
                if(marker != null) marker.setPosition(placePosition); //Удаление старого маркера, если он был
                else {
                    markerOptions = new MarkerOptions().position(placePosition).draggable(true);
                    marker = gMap.addMarker(markerOptions); //Объект маркера
                }
                //Перемещение камеры
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 18.0f));
                //Показ плавующей кнопки
                latestPlace = place.getName().toString();
                pickAddressFab.show();
            }

            @Override
            public void onError(Status status) {

            }
        });
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
        //Границы карты
        gMap.setLatLngBoundsForCameraTarget(latLngBounds);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Изменение позиции маркера
        if(marker != null) marker.setPosition(latLng); //Удаление старого маркера, если он был
        else {
            markerOptions = new MarkerOptions().position(latLng).draggable(true);
            marker = gMap.addMarker(markerOptions); //Объект маркера
            pickAddressFab.show();
        }

        // Перевод координат в адрес
        List<Address> addressList = CoordinatesAPIService.getAddressListFromCoordinates(this, latLng.latitude, latLng.longitude);
        address = CoordinatesAPIService.getStringFromAddressList(addressList);

//        // Открытие SearchView с полученным адресом, если он не null
//          Пока отключено, так как есть фрагмент гугла. Вместо этого в этот фрагмент записывается полученный адрес
        if(address != null) {
//            isPickedByMap = true;
//            //searchEditText.setText(address);
            latestPlace = address;
            placeAutocompleteFragment.setText(address);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favoritesButton:
                Intent intent = new Intent(this, ActivityFavoriteAddresses.class);
                intent.putExtra("mode", mode);
                intent.putExtra("idOfLocation", id);
                startActivityForResult(intent, RESULT_ADDRESS_PICKED_FROM_FAVORITES);
                break;

            case R.id.pickAddressFab:
                addressEnteringDialog = DialogEnterAddressWhenChosen.newInstance(id, mode, false, latestPlace, null);
                addressEnteringDialog.show(getSupportFragmentManager(), "DialogEnteringAddress");
                break;

                //Работа с собственным search_view
//            case R.id.cancelSearchingBtn:
//                setSearchMode(false);
//                break;
//
//            case R.id.searchEditText:
//                setSearchMode(true);
//                break;
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

            //isPickedByMap = true; Пока отключено, так как есть фрагмент гугла
            //searchEditText.setText(address);

            placeAutocompleteFragment.setText(address);
            latestPlace = address;
            pickAddressFab.show();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }
    /*-------------------------------------------------------*/

    /*----------Интерфейс TextWatcher--------------*/
    //Пока отключено, так как есть фрагмент гугла
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//        Пока отключено, так как есть фрагмент гугла
//        if (!isPickedByMap) {
//            setSearchMode(true);
//            ArrayList<String> results;
//            //При первом создании потока
//            if(!isAutoPredThreadCreated) {
//                autocompletePredictionsThread = new AutocompletePredictions(s.toString(), googleApiClient, latLngBounds, autocompleteFilter);
//                isAutoPredThreadCreated = true;
//                autocompletePredictionsThread.execute();
//            }
//            //При последующих
//            else if(autocompletePredictionsThread.getStatus() == AsyncTask.Status.FINISHED) {
//                results = autocompletePredictionsThread.getResults();
//                listViewAdapter.show(results);
//                autocompletePredictionsThread = new AutocompletePredictions(s.toString(), googleApiClient, latLngBounds, autocompleteFilter);
//                autocompletePredictionsThread.execute();
//            }
//        }
//        isPickedByMap = false;
//    }
    /*------------------------------------------------*/

    //Стрелка на тулбаре
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    //Обработка нажатия на стрелку
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_ADDRESS_PICKED_FROM_FAVORITES) finish();
    }

            //Обработка нажатия на аппаратную кнопку "назад"
    @Override
    public void onBackPressed() {
        //Если пользовател в режиме поиска, выходим из него
        finish();
    }

    //Инициализация адаптера listView. Пока отключена, так как есть фрагмент гугла
//    private void searchViewInitialize() {
//        //Инициализация адаптера
//        listViewAdapter = new ListViewAdapter(this);
//        addressesListView.setAdapter(listViewAdapter);
//    }

    //Включение, выключение режима поиска. Пока отключено, так как есть фрагмент гугла
//    private void setSearchMode(boolean isSearchMode) {
//        if(isSearchMode) {
//            addressesListView.setVisibility(View.VISIBLE);
//            closeSearchBtn.setVisibility(View.VISIBLE);
//        }
//        else {
//            addressesListView.setVisibility(View.GONE);
//            closeSearchBtn.setVisibility(View.GONE);
//            inputMethodManager.hideSoftInputFromWindow(closeSearchBtn.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }

    //Если подключиться к сервисам гугла не удалось. Пока отключено, так как есть фрагмент гугла
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.d("Google Services", "onConnectionFailed: Connection Failed!!!");
//    }

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
