package janeelsmur.taxi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import janeelsmur.taxi.DialogEnterAddressWhenChosen;
import janeelsmur.taxi.EnterLocationActivity;
import janeelsmur.taxi.R;


public class FragmentLocation extends Fragment implements View.OnClickListener {

    public static final int MODE_FROM = 0;
    public static final int MODE_WHERE = 1;

    //Сохраняемые значения
    private int id;
    private int mode;
    private String location;

    //Выставление формы локации. Откуда или Куда
    public static FragmentLocation newInstance(int mode, @Nullable String location, int id) {
        Bundle args = new Bundle();

        args.putInt("mode", mode);
        args.putInt("id", id);
        if(location != null) args.putString("location", location); //Если пользователь уже указал локацию

        FragmentLocation fragment = new FragmentLocation();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, null);

        TextView locationTextView = view.findViewById(R.id.locationTextView);

        id = getArguments().getInt("id");
        mode = getArguments().getInt("mode");
        location = getArguments().getString("location"); //Если локация указана, то она будет отображена

        switch (mode) {
            case MODE_FROM:
                if(location == null) locationTextView.setText(R.string.from);
                else {
                    String fromShortText = getResources().getString(R.string.from_short);
                    locationTextView.setText(fromShortText + " " + location);
                }
                break;

            case MODE_WHERE:
                if(location == null) locationTextView.setText(R.string.where);
                else {
                    String whereShortText = getResources().getString(R.string.where_short);
                    locationTextView.setText(whereShortText + " " + location);
                }
                break;
        }

        view.findViewById(R.id.location_layout_button).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), EnterLocationActivity.class);

        intent.putExtra("id", id);
        intent.putExtra("mode", mode);
        if (location != null) intent.putExtra("location", location);

        startActivity(intent);
    }

    /** Геттер id'шника */
    public int getLocationId() {
        return id;
    }

    /** Геттер режима. 0 - откуда
     *                 1 - куда
     */
    public int getMode() {
        return mode;
    }

    /** Геттер записанной локации. null, если нет */
    public String getLoacation() {
        return location;
    }

}


