package janeelsmur.taxi;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DialogCallOperator extends DialogFragment implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_call_operator, null);

        view.findViewById(R.id.cancelCallButton).setOnClickListener(this);
        view.findViewById(R.id.callOperatorButton).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
