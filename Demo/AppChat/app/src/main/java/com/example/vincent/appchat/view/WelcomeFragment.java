package com.example.vincent.appchat.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.vincent.appchat.utils.Constants;
import com.example.vincent.appchat.utils.Session;
import com.example.vincent.appchat.R;
import com.example.vincent.appchat.listener.AppListener;

/**
 * Created by vincent on 18/6/2016.
 */
public class WelcomeFragment extends Fragment {

    private final String TAG = WelcomeFragment.class.getSimpleName();
    private AppListener appListener;
    private Session session;
    private int selectedPos = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appListener = context instanceof AppListener ? (AppListener)context : null;
    }

    public static WelcomeFragment getInstance(){
        return new WelcomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);

        session = Session.getInstance(getContext());
        Button enter = (Button)rootView.findViewById(R.id.enter);
        Spinner accounts = (Spinner)rootView.findViewById(R.id.accounts);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Constants.names);
        accounts.setAdapter(adapter);
        accounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setId(Constants.ids[selectedPos]);
                session.setName(Constants.names[selectedPos]);
                appListener.onChangeFragment();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(session.getId() != -1) appListener.onChangeFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
