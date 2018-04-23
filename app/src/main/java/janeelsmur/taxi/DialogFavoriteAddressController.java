package janeelsmur.taxi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import janeelsmur.taxi.Utilites.SharedPrefManager;

public class DialogFavoriteAddressController extends DialogFragment implements View.OnClickListener {

    private ActivityNotification listener;

    private int id;
    private SharedPrefManager sharedPrefManager;

    private TextView deleteButton;

    public static DialogFavoriteAddressController newInstance(int id) {

        Bundle args = new Bundle();

        args.putInt("id", id);

        DialogFavoriteAddressController fragment = new DialogFavoriteAddressController();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ActivityNotification) context;
        } catch (ClassCastException e){
            Log.d("ActivityNotification", Log.getStackTraceString(e));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id = getArguments().getInt("id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_favorite_address_controller, null);

        deleteButton = view.findViewById(R.id.delete_favorite_address_btn);
        deleteButton.setOnClickListener(this);

        sharedPrefManager = new SharedPrefManager(getActivity());

        return view;
    }

    @Override
    public void onClick(View v) {
        sharedPrefManager.deleteAddressFromFavorites(id);
        dismiss();
        listener.onNotificationReceived("FragmentDeleted!");
    }

    public interface ActivityNotification{
        public void onNotificationReceived(String notification);
    }

}
