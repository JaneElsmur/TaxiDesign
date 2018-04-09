package janeelsmur.taxi.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import janeelsmur.taxi.*;
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

        tariffButton.setOnClickListener(this);
        wishesButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() { //Здесь прогружаются фрагменты мест. Если есть старые записи, то прогружаются по ним. Если нет, то создаются 2 пустых фрагмента.
        super.onStart();
        cleanLocations();
        loadLocations();
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

    private void cleanLocations() {
        for (FragmentLocation fragmentLocation : fragmentLocations) {
            getFragmentManager()
                    .beginTransaction()
                    .remove(fragmentLocation)
                    .commit();
        }
        fragmentLocations.clear();
    }

    private void loadLocations() {
        //Сохраненное значение адреса "откуда" готовое для вывода
        String[] fromAddressStrings = sharedPrefManager.getFromLocation();
        String savedFrom = getFromString(fromAddressStrings);

        //Сохраненное значение первого адреса "куда" готовое для вывода
        String[] firstWhereAddressStrings = sharedPrefManager.getWhereLocation(1);
        String savedFirstWhere = getWhereString(firstWhereAddressStrings);

        if (savedFrom.length() != 0 && savedFirstWhere.length() != 0) { //Прогрузка всех сохраненных данных
            Log.d(TAG, "loadLocations: Прогрузка всего");
            //Добавление первого "от куда"
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_FROM, savedFrom, 0));
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.locations_container, fragmentLocations.get(0))
                    .commit();

            //Добавление первого "куда"
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, savedFirstWhere, 1));
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.locations_container, fragmentLocations.get(1))
                    .commit();

            //Добавление остальных "куда", если они есть
            int id = 2;
            while (sharedPrefManager.getWhereLocation(id) != null) {
                String[] whereAddressStrings = sharedPrefManager.getWhereLocation(id);
                String savedWhere = getWhereString(whereAddressStrings);

                fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, savedWhere, id));
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.locations_container, fragmentLocations.get(id))
                        .commit();
                id++;
            }

        } else if (fromAddressStrings != null && firstWhereAddressStrings == null) { //Прогрузка только "откуда" и пустого "куда", если сохранен только "куда"
            Log.d(TAG, "loadLocations: Прогрузка только \"откуда\" и пустого \"куда\", если сохранен только \"куд");
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_FROM, savedFrom, 0));
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, null, 1));
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.locations_container, fragmentLocations.get(0))
                    .add(R.id.locations_container, fragmentLocations.get(1))
                    .commit();

        } else if (firstWhereAddressStrings != null) { //Прогрузка только "куда" и пустого "откуда", если он не сохранен
            //Добавление первого "куда" и пустого "откуда"
            Log.d(TAG, "loadLocations: Прогрузка только \"куда\" и пустого \"откуда\", если он не сохранен");
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_FROM, null, 0));
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, savedFirstWhere, 1));
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.locations_container, fragmentLocations.get(0))
                    .add(R.id.locations_container, fragmentLocations.get(1))
                    .commit();

            //Добавление остальных "куда", если они есть
            int id = 2;
            while (sharedPrefManager.getWhereLocation(id) != null) {
                String[] whereAddressSet = sharedPrefManager.getWhereLocation(id);
                String savedWhere = getWhereString(whereAddressSet);

                fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, savedWhere, id));
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.locations_container, fragmentLocations.get(id))
                        .commit();
                id++;
            }
        }

        else { //Если нет сохраненных данных, просто добавить 2 фрагмента
            Log.d(TAG, "loadLocations: Если нет сохраненных данных, просто добавить 2 фрагмента");
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_FROM, null, 0));
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, null, 1));

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.locations_container, fragmentLocations.get(0), "locationFrom")
                    .add(R.id.locations_container, fragmentLocations.get(1), "locationWhere")
                    .commit();
        }
    }

    /** Здесь составляется строка, которая будет выводиться в фрагменте "откуда"
     *
     * @param fromAddressStrings - массив с данными адреса, где:
     *                           0 - улица (возможно, что будет записана с домом, в этом случае в индексе 1 будет записан null, если пользователь не записал другое значение)
     *                           1 - дом
     *                           2 - подъезд/объект, к которому нужно подъехать
     *                           3 - комментарий
     * @return Строка адреса
     */
    private String getFromString(String[] fromAddressStrings){
        StringBuilder savedFromBuilder = new StringBuilder();
        if(fromAddressStrings != null) {
            savedFromBuilder.append(fromAddressStrings[0]);
            if(!fromAddressStrings[0].contains(",")) { //Если улица не содержит дом
                savedFromBuilder.append(", ");
                savedFromBuilder.append(fromAddressStrings[1]);
            }
            if(fromAddressStrings[2]!=null) {
                savedFromBuilder.append(", ");
                savedFromBuilder.append(fromAddressStrings[2]);
            }
        }
        return savedFromBuilder.toString(); //Сохраненное значение готовое для вывода
    }

    /** Здесь составляется строка, которая будет выводиться в фрагменте "куда"
     *
     * @param whereAddressStrings - массив с данными адреса, где:
     *                           0 - улица (возможно, что будет записана с домом, в этом случае в индексе 1 будет записан null, если пользователь не записал другое значение)
     *                           1 - дом
     *                           2 - подъезд/объект, к которому нужно подъехать
     *                           3 - комментарий
     * @return Строка адреса
     */
    private String getWhereString(String[] whereAddressStrings){
        StringBuilder savedWhereBuilder = new StringBuilder();
        if(whereAddressStrings != null) {
            savedWhereBuilder.append(whereAddressStrings[0]);
            if(!whereAddressStrings[0].contains(",")) {
                savedWhereBuilder.append(", ");
                savedWhereBuilder.append(whereAddressStrings[1]);
            }
            if(whereAddressStrings[2]!=null) {
                savedWhereBuilder.append(",");
                savedWhereBuilder.append(whereAddressStrings[2]);
            }
        }
        return savedWhereBuilder.toString();
    }
}


