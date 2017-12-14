package janeelsmur.taxi;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import janeelsmur.taxi.fragments.FragmentFeedback;
import janeelsmur.taxi.fragments.FragmentMainScreen;
import janeelsmur.taxi.fragments.FragmentProfile;
import janeelsmur.taxi.fragments.FragmentUnderConstruction;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentMainScreen mainScreen;
    private FragmentProfile profile;
    private FragmentUnderConstruction underConstruction;
    private FragmentFeedback feedback;
    private Toolbar toolbar;

    private final DialogFragment callOperatorDialog = new DialogCallOperator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация тулбара
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Инизиализация всех фрагментов
        mainScreen = new FragmentMainScreen();
        profile = new FragmentProfile();
        underConstruction = new FragmentUnderConstruction();
        feedback = new FragmentFeedback();

        // Инициализация всей навигационной панели и кнопки её вывода на тулбаре
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.closeDrawer(GravityCompat.START);

        //Вызов окна с заказом
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, mainScreen);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.order) {

            if (getSupportFragmentManager().findFragmentById(R.id.container) != mainScreen) {
                toolbar.setTitle(R.string.order);
                transaction.replace(R.id.container, mainScreen);
            }

        } else if (id == R.id.profile) {

            if (getSupportFragmentManager().findFragmentById(R.id.container) != profile) {
                toolbar.setTitle(R.string.profile);
                transaction.replace(R.id.container, profile);
            }

        } else if (id == R.id.favorite_addresses) {

            if (getSupportFragmentManager().findFragmentById(R.id.container) != underConstruction) {
                toolbar.setTitle(R.string.favorite_addresses);
                transaction.replace(R.id.container, underConstruction);
            }

        } else if (id == R.id.bank_card) {

            if (getSupportFragmentManager().findFragmentById(R.id.container) != underConstruction) {
                toolbar.setTitle(R.string.bank_card);
                transaction.replace(R.id.container, underConstruction);
            }

        } else if (id == R.id.i_have_a_problem) {

        } else if (id == R.id.call_to_operator) {
            callOperatorDialog.show(getSupportFragmentManager(), "CallOperatorDialog");

        } else if (id == R.id.feedback) {
            if (getSupportFragmentManager().findFragmentById(R.id.container) != feedback) {
                toolbar.setTitle(R.string.feedback);
                transaction.replace(R.id.container, feedback);
            }
        } transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    } //Вызывается при выборе элементов в меню

}
