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
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

// UPDATE A WORD
public class frag3_Update extends Fragment {
    private static final String G_WORD = "gun_kel";
    private static final String G_MEAN = "gun_anl";
    private static final String G_CALLER = "gun_cagiran";
    TextInputEditText editk3, edita3;
    Button Enter_but3, Iptal_but3;
    Listener3 mCallback3;
    private int kim_cagirdi;
    private String updating_kelime, updating_anlam, silincek_kelime;

    public frag3_Update() {
    }

    public static frag3_Update newInstance(String kel, String anl,int kim_cagirdi) {
        frag3_Update myFragment = new frag3_Update();
        Bundle bundle = new Bundle();
        bundle.putString(G_WORD, kel);
        bundle.putString(G_MEAN, anl);
        bundle.putInt(G_CALLER, kim_cagirdi);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    private void get_args() {
        if (getArguments() != null) {
            updating_kelime = silincek_kelime = getArguments().getString(G_WORD);
            updating_anlam = getArguments().getString(G_MEAN);
            kim_cagirdi = getArguments().getInt(G_CALLER);
        }
        editk3.setText(updating_kelime);
        edita3.setText(updating_anlam);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {// if (context instanceof Imylistener) {
            mCallback3 = (frag3_Update.Listener3) context;
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
        return inflater.inflate(R.layout.layout_frag3, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.update_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upd_menu_enter: {
                updating_kelime = editk3.getText().toString().toLowerCase();
                updating_anlam = edita3.getText().toString().toLowerCase();

                if (updating_kelime.isEmpty() || updating_anlam.isEmpty()) {
                    Toast.makeText(getContext(), "boş bırakma", Toast.LENGTH_SHORT).show();
                    break;
                }
                hideSoftKeyboard();
                mCallback3.onUpdateWord(updating_kelime, updating_anlam, silincek_kelime);
                Toast.makeText(getContext(), "fragdan mesaj\n" + updating_kelime + "\n" + updating_anlam, Toast.LENGTH_SHORT).show();
            }
            break;

            case R.id.upd_menu_iptal: {
                hideSoftKeyboard();
                if (kim_cagirdi==1){
                    mCallback3.call_frag_1();
                }else{
                    mCallback3.call_frag_4();
                }
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editk3 = getView().findViewById(R.id.Editxt_kelime3);
        edita3 = getView().findViewById(R.id.Editxt_anlam3);
        get_args();
    }

    public void onDetach() {
        super.onDetach();
        editk3 = edita3 = null;
        Enter_but3 = Iptal_but3 = null;
        mCallback3 = null;
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public interface Listener3 {
        void onUpdateWord(String k, String a, String s);
        void call_frag_1();
        void call_frag_4();
    }
}
