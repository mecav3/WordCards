package com.mc.dict_frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

// CUSTOM LİST
public class frag8_sList extends Fragment {

    ListView mListView;
    ArrayList<String> keyz;
    private MyAdaptor aAdapter;

    public frag8_sList() {
    }

    public static frag8_sList newInstance(ArrayList<String> k) {
        frag8_sList myFragment = new frag8_sList();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("keys", k);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    private void get_args() {
        if (getArguments() != null) {
            keyz = getArguments().getStringArrayList("keys");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_frag5, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = getView().findViewById(R.id.liste_view_id);
        get_args();
        aAdapter = new MyAdaptor(getActivity(), keyz);
        mListView.setAdapter(aAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Toast.makeText(getContext(), "" + mListView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDetach() {
        super.onDetach();
        mListView = null;
        keyz = null;
        aAdapter = null;
    }

}