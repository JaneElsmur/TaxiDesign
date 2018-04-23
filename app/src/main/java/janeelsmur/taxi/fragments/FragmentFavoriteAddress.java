package janeelsmur.taxi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import janeelsmur.taxi.DialogEnterAddressWhenChosen;
import janeelsmur.taxi.DialogFavoriteAddressController;
import janeelsmur.taxi.R;
import janeelsmur.taxi.Utilites.ObjectsViewLoader;

public class FragmentFavoriteAddress extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private String[] savedFavoriteAddress;
    private int idOfFavoriteFragment;
    private int mode;
    private int idOfLocation;

    private TextView locationTextView;
    private LinearLayout linearLayout;

    public static FragmentFavoriteAddress newInstance(String[] savedFavoriteAddress, int idOfFavoriteFragment, int mode) {

        Bundle args = new Bundle();

        args.putStringArray("savedFavoriteAddress", savedFavoriteAddress);
        args.putInt("idOfFavoriteFragment", idOfFavoriteFragment);
        args.putInt("mode", mode);

        FragmentFavoriteAddress fragment = new FragmentFavoriteAddress();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedFavoriteAddress = getArguments().getStringArray("savedFavoriteAddress");
        this.idOfFavoriteFragment = getArguments().getInt("idOfFavoriteFragment");
        this.mode = getArguments().getInt("mode");
        this.idOfLocation = getActivity().getIntent().getExtras().getInt("idOfLocation");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_favorite_address, null);

        locationTextView = view.findViewById(R.id.favorite_location_TextView);
        linearLayout = view.findViewById(R.id.open_favorite_dialog_btn);

        String text = ObjectsViewLoader.getLocationString(savedFavoriteAddress);
        locationTextView.setText(text);

        linearLayout.setOnClickListener(this);
        linearLayout.setOnLongClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        DialogEnterAddressWhenChosen dialogEnterAddressWhenChosen = DialogEnterAddressWhenChosen.newInstance(idOfLocation, mode, true,null, savedFavoriteAddress);
        dialogEnterAddressWhenChosen.show(getActivity().getSupportFragmentManager(), "DialogEnteringAddress");
    }

    @Override
    public boolean onLongClick(View v) {
        DialogFavoriteAddressController dialogFavoriteAddressController = DialogFavoriteAddressController.newInstance(idOfFavoriteFragment);
        dialogFavoriteAddressController.show(getActivity().getSupportFragmentManager(), "favoritesController");
        return false;
    }
}
