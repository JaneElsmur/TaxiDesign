package janeelsmur.taxi;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class DialogWishes extends DialogFragment implements OnClickListener {

    //Массив RadioButton'ов
    /* 0 - оплата наличными, 1 - оплата через сб онлайн, 2 - оплата банковской картой */
    private RadioButton payWay[] = new RadioButton[3];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, 0);
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_wishes, null);

        //Установка слушателей для кнопок
        view.findViewById(R.id.okButton2).setOnClickListener(this);


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

    /* Прослушиваем нажатия кнопок "отмена" и "ок". Если была нажата "ок", то
        передаем, какой тариф выбран, в обработчик. Если "отмена", то оставляем тот,
        который был выбран до этого. */
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.okButton:
                if (payWay[0].isChecked()){
                    /*Передаем это в обработчик (доделать)*/
                    break;
                }
                if (payWay[1].isChecked()){
                    /*Передаем это в обработчик (доделать)*/
                    break;
                }
                // В РАЗРАБОТКЕ
                // if (payWay[2].isChecked()){
                //    /*Передаем это в обработчик (доделать)*/
                //break;
                //}
        }
        dismiss();
    }

    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        //сохранение отмеченных полей в БД
    }
}
