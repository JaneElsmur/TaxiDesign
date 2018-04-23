package janeelsmur.taxi.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import janeelsmur.taxi.R;

public class FragmentMyListItem extends Fragment {

    private String text;


    public static FragmentMyListItem newInstance(@NonNull String text) {

        Bundle args = new Bundle();

        args.putString("text", text);

        FragmentMyListItem fragment = new FragmentMyListItem();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.text = getArguments().getString("text");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_simple_item, null);

        TextView textView = view.findViewById(R.id.item);
        textView.setText(text);

        return view;
    }
}
