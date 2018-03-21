package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainMenuFragment extends Fragment {
    private Button btnClasses, btnEnroll, btnProfile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        btnClasses = (Button) v.findViewById(R.id.classes_button);
        btnEnroll = (Button) v.findViewById(R.id.enroll_button);
        btnProfile = (Button) v.findViewById(R.id.profile_button);

        btnClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO: implement onclick listeners
//                MyClasses myClasses = new MyClasses();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myClasses, "nextFrag").addToBackStack(null).commit();
            }
        });

        btnEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassListFragment fragment = new ClassListFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "toEnroll").addToBackStack(null).commit();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }
}

