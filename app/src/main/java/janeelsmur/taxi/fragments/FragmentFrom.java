package janeelsmur.taxi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import janeelsmur.taxi.EnterLocation;
import janeelsmur.taxi.R;


public class FragmentFrom extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_from, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.fromButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), EnterLocation.class);
        startActivity(intent);
    }
}


