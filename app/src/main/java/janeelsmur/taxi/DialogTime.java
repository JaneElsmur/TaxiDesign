package janeelsmur.taxi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import janeelsmur.taxi.Utilites.SharedPrefManager;

import java.util.Calendar;


public class DialogTime extends DialogFragment implements View.OnClickListener {

    private SharedPrefManager sharedPrefManager;

    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker datePicker;

    private CheckBox asSoonAsPossible;

    private TextView information;
    private TextView timeInformation;

    //Данные для отображения в TextView
    private String finalInf;

    //Отображаемые значения в минутом пикере
    private final String[] displayedTimeValues = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
    private final String[] displayedDateValues = new String[7];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, 0);
        super.onCreate(savedInstanceState);

        sharedPrefManager = new SharedPrefManager(getActivity());
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_time, null);

        datePicker = view.findViewById(R.id.datePicker);
        hourPicker = view.findViewById(R.id.hourPicker);
        minutePicker = view.findViewById(R.id.minutePicker);
        asSoonAsPossible = view.findViewById(R.id.checkBoxAsSoonAsPossible);
        information = view.findViewById(R.id.dateInformationTextView);
        timeInformation = view.findViewById(R.id.comingAfterTimeTextView);

        //Минимальное и максимальное значение часов, устанавливаем время
        Calendar calendar = Calendar.getInstance();
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        hourPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));

        //Минимальное и максимальное значение минут, допустимые значения
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(11);
        minutePicker.setDisplayedValues(displayedTimeValues);

        //Минимальное и максимальное значение даты, допустимые значения
        for(short i = 0; i<7; i++){
            displayedDateValues[i] = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + "." + Integer.toString(calendar.get(Calendar.MONTH)+1) + "." + Integer.toString(calendar.get(Calendar.YEAR));
            calendar.add(Calendar.DATE, 1);
        }
        datePicker.setMinValue(0);
        datePicker.setMaxValue(6);
        datePicker.setDisplayedValues(displayedDateValues);
        datePicker.setWrapSelectorWheel(false);

        //Установка слушателей изменения значения даты и времени и запись изменений в finalInf
        datePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                showTimeDisplay();
            }
        });
        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String AmPm;
                int hours;
                if(newVal==0){
                    hours = 0;
                    AmPm = "AM";
                }
                else if(newVal<12){
                    hours=newVal;
                    AmPm = "AM";
                }
                else if(newVal==12){
                    hours=newVal;
                    AmPm = "PM";
                }
                else {
                    hours = newVal-12;
                    AmPm = "PM";
                }
                finalInf = (displayedDateValues[datePicker.getValue()] + " " + hours + ":" + displayedTimeValues[minutePicker.getValue()] + " " + AmPm);
                information.setText(finalInf);
            }
        });
        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                showTimeDisplay();
            }
        });

        //Установка слушателя для чекбокса
        asSoonAsPossible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    datePicker.setEnabled(false);
                    hourPicker.setEnabled(false);
                    minutePicker.setEnabled(false);
                    information.setVisibility(View.INVISIBLE);
                    timeInformation.setVisibility(View.INVISIBLE);
                } else {
                    datePicker.setEnabled(true);  hourPicker.setEnabled(true); minutePicker.setEnabled(true);
                    information.setVisibility(View.VISIBLE);
                    timeInformation.setVisibility(View.VISIBLE);
                    showTimeDisplay();
                }
            }
        });

        //Установка слушателя кнопки
        view.findViewById(R.id.okTimeButton).setOnClickListener(this);
        view.findViewById(R.id.cancelTimeButton).setOnClickListener(this);

        return view;
    }

    /* Восстанавливаем прошлые значения после закрытия, если они были */
    @Override
    public void onResume() {
        super.onResume();

        //Восстановление отметки чекбокса
        asSoonAsPossible.setChecked(sharedPrefManager.isAsSoonAsPossibleChecked());

        //Логика: если первый элемент массива с сохраненными значениями = TIME_IS_NOT_SAVED, то данные не были сохранены
        if(sharedPrefManager.getSavedTime()[0]!=SharedPrefManager.TIME_IS_NOT_SAVED) loadData();
        //Если отмечена "как можно скорее", то отключаем выбор времени, иначе выводим диспей с выбранным временем
        if (asSoonAsPossible.isChecked()) {
            datePicker.setEnabled(false);  hourPicker.setEnabled(false); minutePicker.setEnabled(false);
        } else {
            showTimeDisplay();
        }
    }

    public void onClick(View view){
        switch (view.getId()){

            case R.id.okTimeButton:
                //Сохраняем выставленные значения
                saveData();
                break;

            case R.id.cancelTimeButton:
                break;
        }
        dismiss();
    }

    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
    }

    //Выводит полную дату снизу выбора времени
    private void showTimeDisplay(){
        String AmPm;
        int hours;
        int hourValue = hourPicker.getValue();
        if(hourValue==0){
            hours = 0;
            AmPm = "AM";
        }
        else if(hourValue<12){
            hours=hourValue;
            AmPm = "AM";
        }
        else if(hourValue==12){
            hours=hourValue;
            AmPm = "PM";
        }
        else {
            hours = hourValue-12;
            AmPm = "PM";
        }
        finalInf = (displayedDateValues[datePicker.getValue()] + " " + hours + ":" + displayedTimeValues[minutePicker.getValue()] + " " + AmPm);
        information.setVisibility(View.VISIBLE);
        information.setText(finalInf);
    }

    //Сохраняет введенные значения
    private void saveData() {
        int[] pickValues = new int[3];
        pickValues[0] = datePicker.getValue();
        pickValues[1] = hourPicker.getValue();
        pickValues[2] = minutePicker.getValue();
        sharedPrefManager.saveTime(pickValues, asSoonAsPossible.isChecked(), finalInf);
    }

    //Выставляет в пикерах введенные до этого значения
    private void loadData() {
        int[] pickValues = sharedPrefManager.getSavedTime();
        datePicker.setValue(pickValues[0]);
        hourPicker.setValue(pickValues[1]);
        minutePicker.setValue(pickValues[2]);
    }

}
