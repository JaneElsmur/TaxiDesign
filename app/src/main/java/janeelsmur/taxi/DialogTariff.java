package janeelsmur.taxi;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import janeelsmur.taxi.Utilites.SharedPrefManager;

public class DialogTariff extends DialogFragment implements OnClickListener {

    private SharedPrefManager sharedPrefManager;

    //Массив RadioButton'ов
    /* 0 - Тариф1, 1 - Тариф2, 2 - Тариф3, 3 - Тариф4 */
    private RadioButton tariff[] = new RadioButton[4];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, 0);
        super.onCreate(savedInstanceState);

        sharedPrefManager = new SharedPrefManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_tariff, null);

        //Установка слушателей для кнопок
        view.findViewById(R.id.okTariffButton).setOnClickListener(this);
        view.findViewById(R.id.cancelTariffButton).setOnClickListener(this);


        //Инициализация всех RadioButton'ов
        tariff[0] = view.findViewById(R.id.radioButtonTariff1);
        tariff[1] = view.findViewById(R.id.radioButtonTariff2);
        tariff[2] = view.findViewById(R.id.radioButtonTariff3);
        tariff[3] = view.findViewById(R.id.radioButtonTariff4);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTariff();
    }

    /* Прослушиваем нажатия кнопок "отмена" и "ок". Если была нажата "ок", то
                передаем, какой тариф выбран, в обработчик. Если "отмена", то оставляем тот,
                который был выбран до этого. */
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.okTariffButton:
                if (tariff[0].isChecked()){
                    sharedPrefManager.saveTariff(0);
                    break;
                } else if (tariff[1].isChecked()){
                    sharedPrefManager.saveTariff(1);
                    break;
                } else if (tariff[2].isChecked()){
                    sharedPrefManager.saveTariff(2);
                    break;
                } else if (tariff[3].isChecked()) {
                    sharedPrefManager.saveTariff(3);
                    break;
                }

            case R.id.cancelTariffButton: break;
        }
        dismiss();
    }

    private void loadTariff(){
        int savedTariff = sharedPrefManager.getSavedTariff();
        switch (savedTariff){
            case 0: tariff[0].setChecked(true); break;
            case 1: tariff[1].setChecked(true); break;
            case 2: tariff[2].setChecked(true); break;
            case 3: tariff[3].setChecked(true); break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
    }
}
