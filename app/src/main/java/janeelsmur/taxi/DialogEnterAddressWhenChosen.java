package janeelsmur.taxi;

import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import janeelsmur.taxi.Utilites.SharedPrefManager;
import janeelsmur.taxi.fragments.FragmentLocation;

public class DialogEnterAddressWhenChosen extends DialogFragment implements View.OnClickListener{

    //Сохраненные значения
    private int mode;
    private int id;
    private String location;

    private Button ok;
    private Button cancel;
    private TextView locationTextView;
    private EditText additionEditText;
    private EditText commentEditText;
    private EditText houseEditText;
    private CheckBox addInFavorites;

    public static DialogEnterAddressWhenChosen newInstance(int id, int mode, String location) {
        Bundle args = new Bundle();

        args.putInt("mode", mode);
        args.putInt("id", id);
        args.putString("location", location);

        DialogEnterAddressWhenChosen fragment = new DialogEnterAddressWhenChosen();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, 0);
        super.onCreate(savedInstanceState);

        mode = getArguments().getInt("mode");
        id = getArguments().getInt("id");
        location = getArguments().getString("location");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_entering_address_when_chosed, null);

        locationTextView = (TextView) view.findViewById(R.id.chosen_address);
        houseEditText = (EditText) view.findViewById(R.id.houseEditText);
        additionEditText = (EditText) view.findViewById(R.id.additions_to_address);
        commentEditText = (EditText) view.findViewById(R.id.comment_to_address);
        ok = (Button) view.findViewById(R.id.acceptAddressBtn);
        cancel = (Button) view.findViewById(R.id.cancelAddressBtn);
        addInFavorites = (CheckBox) view.findViewById(R.id.addInFavoritesCheckBox);


        //Выставление выбранного аддреса
        locationTextView.setText(location);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //Кнопка Ок
            case R.id.acceptAddressBtn:
                SharedPrefManager sharedPrefManager = new SharedPrefManager(getActivity());

                String house = houseEditText.getText().toString(); //Дом
                String addition = additionEditText.getText().toString(); // Подъезд/объект, к которому подъехать
                String comment = commentEditText.getText().toString(); // Комментарий

                //Формирование сета строк (сохраненный аддрес)
                String[] addressToSave = {location, house, addition, comment};

                switch (mode) {
                    //Если режим - из
                    case FragmentLocation.MODE_FROM:
                        sharedPrefManager.saveFromLocation(addressToSave);
                        break;

                    //Если режим - куда
                    case FragmentLocation.MODE_WHERE:
                        sharedPrefManager.saveWhereLocation(addressToSave, id);
                        break;
                }
                dismiss();
                getActivity().finish();

            //Кнопка отмены
            case R.id.cancelAddressBtn:
                dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        commentEditText.setText(null);
        houseEditText.setText(null);
        additionEditText.setText(null);
        super.onDismiss(dialog);
    }
}
