package janeelsmur.taxi;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class DialogTariff extends DialogFragment implements OnClickListener {

    //Массив RadioButton'ов
    /* 0 - Тариф1, 1 - Тариф2, 2 - Тариф3, 3 - Тариф4 */
    private RadioButton tariff[] = new RadioButton[4];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, 0);
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_tariff, null);

        //Установка слушателей для кнопок
        view.findViewById(R.id.okButton).setOnClickListener(this);


        //Инициализация всех RadioButton'ов
        tariff[0] = view.findViewById(R.id.radioButtonTariff1);
        tariff[1] = view.findViewById(R.id.radioButtonTariff2);
        tariff[2] = view.findViewById(R.id.radioButtonTariff3);
        tariff[3] = view.findViewById(R.id.radioButtonTariff4);

        //Как только выбираем тариф, отметка выбора на других RadioButton'ах снимается (Есть ли способ компактнее?)
        tariff[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tariff[1].setChecked(false); tariff[2].setChecked(false); tariff[3].setChecked(false);
                }
            }
        });
        tariff[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tariff[0].setChecked(false); tariff[2].setChecked(false); tariff[3].setChecked(false);
                }
            }
        });
        tariff[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tariff[0].setChecked(false); tariff[1].setChecked(false); tariff[3].setChecked(false);
                }
            }
        });
        tariff[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tariff[0].setChecked(false); tariff[1].setChecked(false); tariff[2].setChecked(false);
                }
            }
        });
        //-------------------------------------------------------------------------------------------------------
        return view;
    }

    /* Прослушиваем нажатия кнопок "отмена" и "ок". Если была нажата "ок", то
        передаем, какой тариф выбран, в обработчик. Если "отмена", то оставляем тот,
        который был выбран до этого. */
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.okButton:
                if (tariff[0].isChecked()){
                    /*Передаем это в обработчик (доделать)*/
                    break;
                }
                if (tariff[1].isChecked()){
                    /*Передаем это в обработчик (доделать)*/
                    break;
                }
                if (tariff[2].isChecked()){
                    /*Передаем это в обработчик (доделать)*/
                    break;
                }
                if (tariff[3].isChecked()) {
                    /*Передаем это в обработчик (доделать)*/
                    break;
                }
        }
        dismiss();
    }

    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        //сохранение отмеченных полей в БД
    }
}
