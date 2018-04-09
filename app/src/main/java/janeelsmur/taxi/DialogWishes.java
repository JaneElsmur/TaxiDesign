package janeelsmur.taxi;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import janeelsmur.taxi.Utilites.SharedPrefManager;

import java.util.ArrayList;

public class DialogWishes extends DialogFragment implements OnClickListener {

    private SharedPrefManager sharedPrefManager;

    //Массив RadioButton'ов
    /* 0 - оплата наличными, 1 - оплата через сб онлайн, 2 - оплата банковской картой */
    private RadioButton payWay[] = new RadioButton[3];

    //Список CheckBox'ов
    // 0 - Нужна сдача
    // 1 - Нужно детское кресло
    // 2 - Некурящий салон
    // 3 - Провоз животкного
    // 4 - I don't speck Russian
    // 5 - Нужен пустой багажник
    // 6 - Нужен универсал
    // 7 - Нужен чек
    private static final int WISHES_COUNT = 8;
    private CheckBox[] checkBoxes = new CheckBox[WISHES_COUNT];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, 0);
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_wishes, null);

        sharedPrefManager = new SharedPrefManager(getActivity());

        //Установка слушателей для кнопок
        view.findViewById(R.id.okWishesButton).setOnClickListener(this);
        view.findViewById(R.id.cancelWishesButton).setOnClickListener(this);

        //Инициализация всех CheckBox'ов
        checkBoxes[0] = ((CheckBox)view.findViewById(R.id.changeNeededCheckBox));
        checkBoxes[1] = ((CheckBox)view.findViewById(R.id.needBabyChairCheckBox));
        checkBoxes[2] = ((CheckBox)view.findViewById(R.id.nonSmokingCheckBox));
        checkBoxes[3] = ((CheckBox)view.findViewById(R.id.petTransportationCheckBox));
        checkBoxes[4] = ((CheckBox)view.findViewById(R.id.notRusCheckBox));
        checkBoxes[5] = ((CheckBox)view.findViewById(R.id.emptyTrunkCheckBox));
        checkBoxes[6] = ((CheckBox)view.findViewById(R.id.wagonCheckBox));
        checkBoxes[7] = ((CheckBox)view.findViewById(R.id.needCheckCheckBox));

        //Инициализация всех RadioButton'ов
        payWay[0] = view.findViewById(R.id.readyMoneyRadioButton);
        payWay[1] = view.findViewById(R.id.sberbankOnlineRadioButton);
        //payWay[2] = view.findViewById(id кнопки оплаты по банковской карте); //В РАЗРАБОТКЕ

        //Как только выбираем тариф, отметка выбора на других RadioButton'ах снимается (Есть ли способ компактнее?)
        payWay[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    payWay[1].setChecked(false); //payWay[2].setChecked(false);
                }
            }
        });
        payWay[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    payWay[0].setChecked(false); //payWay[2].setChecked(false);
                }
            }
        });
        // В РАЗРАБОТКЕ
        /* payWay[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    payWay[0].setChecked(false); payWay[1].setChecked(false);
                }
            }
        }); */
        //-------------------------------------------------------------------------------------------------------
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWishes(WISHES_COUNT);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.okWishesButton:
                if (payWay[0].isChecked()){
                    //Передаем это в обработчик (доделать
                }
                if (payWay[1].isChecked()){
                    //Передаем это в обработчик (доделать)
                }
                // В РАЗРАБОТКЕ
                // if (payWay[2].isChecked()){
                //}
                sharedPrefManager.saveWishes(checkBoxes, WISHES_COUNT);
                dismiss();
                break;

            case R.id.cancelWishesButton:
                dismiss();
        }
    }

    private void loadWishes(int wishesCount){
        boolean[] checks = sharedPrefManager.getWishes(wishesCount);
        for(int i = 0; i<wishesCount; i++) {
            checkBoxes[i].setChecked(checks[i]);
        }
    }

    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
    }
}
