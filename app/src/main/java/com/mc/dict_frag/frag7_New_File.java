package com.mc.dict_frag;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// ADD NEW FILE
public class frag7_New_File extends Fragment {
    Listener7 mCallback7;
    EditText ed7;

    public frag7_New_File() {
    }

    public static frag7_New_File newInstance() {
        return new frag7_New_File();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback7 = (Listener7) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Imylistener7 hatası");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_frag7_newfile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ed7 = getView().findViewById(R.id.textView7);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.addf_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addf_menu_enter: {
                String filename = ed7.getText().toString().trim();
                if (!filename.isEmpty()) {
                    mCallback7.pass_new_FileName(filename);
                }
                hideSoftKeyboard();
            }
            break;

            case R.id.addf_menu_iptal: {
                hideSoftKeyboard();
                mCallback7.call_frag_1();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDetach() {
        super.onDetach();
        ed7 = null;
        mCallback7 = null;
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public interface Listener7 {
        void pass_new_FileName(String str);
        void call_frag_1();
    }
}