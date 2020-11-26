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
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;

// ADD WORD
public class frag2_Add extends Fragment {
    private static final String INDEKS_MAX = "indeks_max";
    TextInputEditText editk2, edita2;
    Listener2 mCallback2;
    private int index_max = -1;

    public frag2_Add() {
    }

    public static frag2_Add newInstance(int ind) {
        frag2_Add myFragment = new frag2_Add();
        Bundle bundle = new Bundle();
        bundle.putInt(INDEKS_MAX, ind);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu_enter: {
                String yeni_kelime = editk2.getText().toString().toLowerCase();
                String yeni_anlam = edita2.getText().toString().toLowerCase();

                if (yeni_kelime.isEmpty() || yeni_anlam.isEmpty()) {
                    Toast.makeText(getContext(), "boş bırakma", Toast.LENGTH_SHORT).show();
                    // editk2.setError("boş olmaz");
                    break;
                }
                mCallback2.onNewWordEnter(yeni_kelime, yeni_anlam);
                index_max++;
                // Toast.makeText(getContext(),"yeni "+yeni_kelime+":"+yeni_anlam+"son ind max"+index_max,Toast.LENGTH_SHORT).show();
                editk2.setText("");
                edita2.setText("");
                editk2.requestFocus();
            }
            break;

            case R.id.add_menu_iptal: {
                // Toast.makeText(getContext(),"iptal ind max"+index_max,Toast.LENGTH_SHORT).show();
                mCallback2.call_frag_1();
                hideSoftKeyboard();
                // getActivity().getFragmentManager().popBackStack();
                // getActivity().onBackPressed();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try { // if (context instanceof Imylistener) {
            mCallback2 = (frag2_Add.Listener2) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Imylistener hatası");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_frag2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editk2 = getView().findViewById(R.id.Editxt_kelime2);
        edita2 = getView().findViewById(R.id.Editxt_anlam2);

        if (getArguments() != null) {
            index_max = getArguments().getInt(INDEKS_MAX);
            editk2.requestFocus();
            showSoftKeyboard(editk2);
            //Toast.makeText(getContext(),"alınan index max"+index_max,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "frag2_Add intex hata", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDetach() {
        super.onDetach();
        editk2 = edita2 = null;
        mCallback2 = null;
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public void showSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);

    }

    public interface Listener2 {
        void onNewWordEnter(String k, String a);
        void call_frag_1();
    }
}

/*myEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);*/