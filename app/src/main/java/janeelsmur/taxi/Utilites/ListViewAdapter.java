package janeelsmur.taxi.Utilites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import janeelsmur.taxi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<String> addresses; //Адреса, которые будут отображаться

    public ListViewAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        addresses = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public String getItem(int position) {
        return addresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final TextView textView;
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_in_list_view_via_address_search, null);
            // Locate the TextView
            textView = view.findViewById(R.id.addressInSearchTextView);
            view.setTag(textView);
        } else {
            textView = (TextView) view.getTag();
        }
        // Set the result into TextView
        textView.setText(addresses.get(position));
        return view;
    }

    // Filter Class
    public void show(ArrayList<String> predictionsResults) {
        addresses.clear();
        addresses.addAll(predictionsResults);
        notifyDataSetChanged();
    }

}
