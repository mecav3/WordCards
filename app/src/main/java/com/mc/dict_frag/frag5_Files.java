package com.mc.dict_frag;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
//GET FİLES
public class frag5_Files extends Fragment {

    Listener5 mCallback5;
    private ListView mListView;
    private ArrayList<String> file_list = new ArrayList<>();
    private ArrayAdapter<String> aAdapter;

    public frag5_Files() {
    }

    public interface Listener5 {
        void change_file(String str);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback5 = ( Listener5 ) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Imylistener5 hatası");
        }
    }

    public static frag5_Files newInstance() {
        return new frag5_Files();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences pref = getActivity().getSharedPreferences("all_files", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (!pref.contains("key_myDictionary")) {
            editor.putString("key_myDictionary", "myDictionary");
            editor.apply();
        }

        Map<String, ?> allEntries = pref.getAll();
        if (allEntries != null) {
            for ( Map.Entry<String, ?> entry : allEntries.entrySet() ) {
                file_list.add( entry.getValue().toString() );
                Collections.sort(file_list);
            }
        } else {
            file_list.add("liste boşş");
        }

        mListView = getView().findViewById(R.id.liste_view_id);
        aAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, file_list);
        mListView.setAdapter(aAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            mCallback5.change_file( mListView.getItemAtPosition(position).toString() );
            }
        });
    }

    public void onDetach() {
        super.onDetach();
        mListView = null;
        file_list = null;
        aAdapter = null;
        mCallback5 = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_frag5, container, false);
    }
}