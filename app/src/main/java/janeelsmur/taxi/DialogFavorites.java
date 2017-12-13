package janeelsmur.taxi;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.*;

public class DialogFavorites extends DialogFragment{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(STYLE_NO_TITLE, 0);
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_under_construction, null);
            return view;
        }

        @Override
        public void onStart() {
            super.onStart();
            //Определение размера экрана и растягивание диалогово окна до него
            Point displaySize = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(displaySize);
            getDialog().getWindow().setLayout(displaySize.x, displaySize.y);
        }
    }
