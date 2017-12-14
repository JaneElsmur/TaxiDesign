package janeelsmur.taxi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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


public class FragmentMainScreen extends Fragment implements OnClickListener{

    private final short MAX_LOCATIONS = 5; //Максимальное число фрагментов "куда"

    //Диалоговые окна
    private final DialogFragment tariffDialog = new DialogTariff(); //Инициализация здесь позволяет сохранять отмеченный checkBox
    private final DialogFragment wishesDialog = new DialogWishes();
    private final DialogFragment timeDialog = new DialogTime();

    //Кнопки
    private ImageButton tariffButton;
    private ImageButton wishesButton;
    private ImageButton timeButton;
    private LinearLayout addButton;
    private LinearLayout removeButton;

    //Если хранить в массиве, то данные в нем сохраняются после закрытия фрагмента
    //Количество кладок "куда". Обновляется при убийстве процесса на это значение (пока что, позже будет храниться в БД)
    private short locationsCount = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentFrom from = new FragmentFrom();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.locations_container, from, "first added1")
                .commit();

        //ВРЕМЕННО, ПОКА НЕТ БД
            FragmentWhere where[] = new FragmentWhere[locationsCount];
        for (short count = 0; count < locationsCount; count++) {
            where[count] = new FragmentWhere();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.locations_container, where[count], "first added2")
                    .commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_screen, null);
    }

    @Override
    public void onStart(){
        super.onStart();
        //Инициализация кнопок и установка слушателей
        tariffButton = getActivity().findViewById(R.id.tariffButton);
        wishesButton = getActivity().findViewById(R.id.wishesButton);
        timeButton = getActivity().findViewById(R.id.whenButton);
        addButton = getActivity().findViewById(R.id.plusButton);
        removeButton = getActivity().findViewById(R.id.minusButton);
        tariffButton.setOnClickListener(this);
        wishesButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        FragmentManager fm = getFragmentManager();
        switch (view.getId()) {
            case R.id.tariffButton: tariffDialog.show(fm, "tariffDialog"); break;
            case R.id.wishesButton: wishesDialog.show(fm, "wishesDialog"); break;
            case R.id.whenButton: timeDialog.show(fm, "timeDialog"); break;
            case R.id.plusButton: addWhereField(); break;
            case R.id.minusButton: removeWhereField(); break;
        }
    }

    //Добавляет новый фрагмент "куда" и увеличивается общее кол-во фрагментов на 1
    private void addWhereField(){
        if (locationsCount < MAX_LOCATIONS) {
            FragmentWhere where = new FragmentWhere();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.locations_container, where, "location")
                    .commit();
            locationsCount++;
        }
    }

    private void removeWhereField(){
        if (locationsCount > 1) {
            getFragmentManager().beginTransaction()
                    .remove(getFragmentManager().findFragmentByTag("location"))
                    .commit();
            locationsCount--;
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("MainScreen", "onDestroyView: !");
    }
}


