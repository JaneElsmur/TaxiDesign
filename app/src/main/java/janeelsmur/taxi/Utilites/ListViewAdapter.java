package janeelsmur.taxi.Utilites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import janeelsmur.taxi.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<String> addresses;
    private ArrayList<String> arrayList;

    public ListViewAdapter(Context context, List<String> addresses) {
        mContext = context;
        this.addresses = addresses;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(addresses);
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
    public void filter(String charText) {
        charText = charText.toLowerCase();
        addresses.clear();
        if (charText.length() == 0) {
            //Ничего не введено
        } else {
            for (String address : arrayList) {
                if (address.toLowerCase().contains(charText)) {
                    addresses.add(address);
                }
            }
            if (addresses.size() == 0) addresses.add("Ничего не найдено");
        }
        notifyDataSetChanged();
    }

}
