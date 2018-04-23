package janeelsmur.taxi;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import janeelsmur.taxi.Utilites.ObjectsViewLoader;
import janeelsmur.taxi.Utilites.SharedPrefManager;
import janeelsmur.taxi.fragments.FragmentFavoriteAddress;
import java.util.ArrayList;

public class ActivityFavoriteAddresses extends AppCompatActivity implements View.OnClickListener, DialogFavoriteAddressController.ActivityNotification {

    private ArrayList<FragmentFavoriteAddress> favoriteAddresses = new ArrayList<>();
    private SharedPrefManager sharedPrefManager;

    private int mode;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_addresses);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.cloaeFavoritesFab);
        floatingActionButton.setOnClickListener(this);

        mode = getIntent().getExtras().getInt("mode");

        sharedPrefManager = new SharedPrefManager(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadFavorites();
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    private void loadFavorites() {
        ObjectsViewLoader.clearFavoriteAddresses(favoriteAddresses, getSupportFragmentManager());
        ObjectsViewLoader.loadFavoritesAddresses(favoriteAddresses, getSupportFragmentManager(), sharedPrefManager, mode);
    }

    @Override
    public void onNotificationReceived(String notification) {
        loadFavorites();
    }
}
