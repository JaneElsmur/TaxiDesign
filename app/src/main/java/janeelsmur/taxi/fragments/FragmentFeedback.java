package janeelsmur.taxi.fragments;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import janeelsmur.taxi.R;

public class FragmentFeedback extends Fragment implements View.OnClickListener {

    private ImageButton thumbsUp;
    private ImageButton thumbsDown;
    private EditText commentText;
    private Button sendButton;

    //Какая иконка отмечена (работа по типу RadioButton)
    private final short IS_THUMBSUP_CHECKED = 0;
    private final short IS_THUMBSDOWN_CHECKED = 1;
    private final boolean[] isChecked = new boolean[2];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        thumbsUp = getActivity().findViewById(R.id.thumbsUpImageButton);
        thumbsDown = getActivity().findViewById(R.id.thumbsDownImageButton);
        commentText = getActivity().findViewById(R.id.commentaryEditText);
        sendButton = getActivity().findViewById(R.id.commentSendButton);

        thumbsUp.setOnClickListener(this);
        thumbsDown.setOnClickListener(this);
        thumbsUp.callOnClick();
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.thumbsUpImageButton :
                thumbsUp.setColorFilter(R.color.colorPrimary);
                thumbsDown.clearColorFilter();
                isChecked[IS_THUMBSUP_CHECKED] = true;
                isChecked[IS_THUMBSDOWN_CHECKED] = false;
                break;
            case R.id.thumbsDownImageButton :
                thumbsDown.setColorFilter(R.color.colorPrimary);
                thumbsUp.clearColorFilter();
                isChecked[IS_THUMBSUP_CHECKED] = false;
                isChecked[IS_THUMBSDOWN_CHECKED] = true;
                break;
        }
    }

}
