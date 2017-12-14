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
import java.util.Calendar;


public class DialogTime extends DialogFragment implements View.OnClickListener {

    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker datePicker;

    private CheckBox asSoonAsPossible;

    private TextView information;
    private TextView timeInformation;

    //Данные для отображения в TextView
    private String finalInf;
    private String finalTimeInf;
    private String date;
    private String minuts;
    private int hours;

    //Отображаемые значения в минутах и дате
    private final String[] displayedTimeValues = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
    private final String[] displayedDateValues = new String[7];

    //Сохраняемые данные
    private final short DATA_INDEX = 0;
    private final short HOURS_INDEX = 1;
    private final short MINUTES_INDEX = 2;
    private final int EMPTY = 555;
    private final int[] timeValues_DataBaseValue = {EMPTY, EMPTY, EMPTY};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, 0);
        super.onCreate(savedInstanceState);
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
                saveData();
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
                saveData();
            }
        });
        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                showTimeDisplay();
                saveData();
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
        view.findViewById(R.id.okButton3).setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Восстанавливаем прошлые значения после закрытия, если они были
        if(timeValues_DataBaseValue[0]!=EMPTY) loadData();
        //Если отмечена "как можно скорее", то отключаем выбор времени, иначе выводим диспей с выбранным временем
        if (asSoonAsPossible.isChecked()){
            datePicker.setEnabled(false);  hourPicker.setEnabled(false); minutePicker.setEnabled(false);
        } else {
            showTimeDisplay();
        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case(R.id.okButton3) : dismiss(); break;
        }
    }

    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
    }

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
    } //Делает дисплей видимым и выводит на него все значения

    private void saveData(){
        timeValues_DataBaseValue[DATA_INDEX] = datePicker.getValue();
        timeValues_DataBaseValue[HOURS_INDEX] = hourPicker.getValue();
        timeValues_DataBaseValue[MINUTES_INDEX] = minutePicker.getValue();
    } //Сохраняет введенные значения

    private void loadData(){
            datePicker.setValue(timeValues_DataBaseValue[DATA_INDEX]);
            hourPicker.setValue(timeValues_DataBaseValue[HOURS_INDEX]);
            minutePicker.setValue(timeValues_DataBaseValue[MINUTES_INDEX]);
    } //Выставляет в пикерах введенные до этого значения

}
