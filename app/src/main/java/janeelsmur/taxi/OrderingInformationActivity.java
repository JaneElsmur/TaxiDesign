package janeelsmur.taxi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import janeelsmur.taxi.Utilites.ObjectsViewLoader;
import janeelsmur.taxi.Utilites.SharedPrefManager;
import janeelsmur.taxi.fragments.FragmentMyListItem;

public class OrderingInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering_information);

        // Тулбар
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ordering_information);
        toolbar.setTitle(R.string.order);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPrefManager = new SharedPrefManager(this);

        //Слушатель для кнопки
        findViewById(R.id.finalOrderButton).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Прогрузка пожеланий, тарифа и локаций.
        ObjectsViewLoader.loadWishes(getSupportFragmentManager(), sharedPrefManager, this);
        ObjectsViewLoader.loadTariff(getSupportFragmentManager(), sharedPrefManager, this);
        ObjectsViewLoader.loadLocationsInFinalOrderingInformation(getSupportFragmentManager(), sharedPrefManager, this);

        //Прогрузка времени прибытия
        FragmentMyListItem timeItemFragment;
        if (!sharedPrefManager.isAsSoonAsPossibleChecked() && sharedPrefManager.getSavedTimeAsText()!=null)
            timeItemFragment = FragmentMyListItem.newInstance(sharedPrefManager.getSavedTimeAsText());
        else timeItemFragment = FragmentMyListItem.newInstance(getResources().getString(R.string.as_soon_as_possible));
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.date_of_arrival_container, timeItemFragment)
                .commit();
    }

    /**Обработка тулбара*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    //--------------------

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finalOrderButton:
                finish();
                Toast.makeText(this, "Заказ принят! Ждите СМС уведомления/звонка оператора.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
