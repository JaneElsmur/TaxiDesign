package janeelsmur.taxi;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class EnterLocation extends AppCompatActivity implements View.OnClickListener{

    private final DialogFragment favoritesDialog = new DialogFavorites();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_location);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Установка слушателей для кнопок
        findViewById(R.id.favoritesButton).setOnClickListener(this);
        findViewById(R.id.backButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        switch (v.getId()){
            case R.id.backButton: onBackPressed(); break;
            case R.id.favoritesButton: favoritesDialog.show(fm, "favoritesDialog"); break;
        }

    }
}
