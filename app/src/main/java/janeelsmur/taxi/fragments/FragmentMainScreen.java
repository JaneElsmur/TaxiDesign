package janeelsmur.taxi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import janeelsmur.taxi.*;
import janeelsmur.taxi.Utilites.ObjectsViewLoader;
import janeelsmur.taxi.Utilites.SharedPrefManager;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class FragmentMainScreen extends Fragment implements OnClickListener {

    private final short MAX_LOCATIONS = 5; //Максимальное число фрагментов "куда"
    private SharedPrefManager sharedPrefManager;

    //Диалоговые окна
    private final DialogFragment tariffDialog = new DialogTariff(); //Инициализация здесь позволяет сохранять отмеченный checkBox
    private final DialogFragment wishesDialog = new DialogWishes();
    private final DialogFragment timeDialog = new DialogTime();

    //Кнопки
    private ImageButton tariffButton;
    private ImageButton wishesButton;
    private LinearLayout addButton;
    private ImageButton timeButton;
    private LinearLayout removeButton;
    private Button orderButton;

    //Список c фрагментами
    private ArrayList<FragmentLocation> fragmentLocations = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, null);

        sharedPrefManager = new SharedPrefManager(getContext());

        fragmentLocations.toArray(); //Выставление очереди списка как у массива

        tariffButton = view.findViewById(R.id.tariffButton);
        wishesButton = view.findViewById(R.id.wishesButton);
        timeButton = view.findViewById(R.id.whenButton);
        addButton = view.findViewById(R.id.plusButton);
        removeButton = view.findViewById(R.id.minusButton);
        orderButton = view.findViewById(R.id.orderButton);

        tariffButton.setOnClickListener(this);
        wishesButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
        orderButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ObjectsViewLoader.cleanLocations(fragmentLocations, getFragmentManager());
        ObjectsViewLoader.loadLocations(fragmentLocations, getFragmentManager(), sharedPrefManager);
    }

    @Override
    public void onClick(View view) {
        FragmentManager fm = getFragmentManager();
        switch (view.getId()) {
            case R.id.tariffButton:
                tariffDialog.show(fm, "tariffDialog");
                break;
            case R.id.wishesButton:
                wishesDialog.show(fm, "wishesDialog");
                break;
            case R.id.whenButton:
                timeDialog.show(fm, "timeDialog");
                break;
            case R.id.plusButton:
                addWhereField();
                break;
            case R.id.minusButton:
                removeWhereField();
                break;
            case R.id.orderButton:
                Intent intent = new Intent(getContext(), OrderingInformationActivity.class);
                startActivity(intent);
        }
    }

    //Добавляет новый фрагмент "куда"
    private void addWhereField() {
        int totalLocationsAndLastId = fragmentLocations.size();
        if (totalLocationsAndLastId < MAX_LOCATIONS) {
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, null, totalLocationsAndLastId));
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.locations_container, fragmentLocations.get(totalLocationsAndLastId), "locationWhere")
                    .commit();
        }
    }

    //Удаляет фрагмент "куда"
    private void removeWhereField() {
        int totalLocations = fragmentLocations.size();
        int id = totalLocations - 1;
        if (totalLocations > 2) {
            sharedPrefManager.deleteWhereLocation(id);
            getFragmentManager().beginTransaction()
                    .remove(fragmentLocations.get(id))
                    .commit();
            fragmentLocations.remove(id);
        }
    }

}


