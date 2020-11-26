package com.mc.dict_frag;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

// SHOW RANDOM WORDS
// addToBackStack()
public class frag4_Show extends Fragment {
    TextView tk4;
    Button btndontask;
    Listener4 mCallback4;

    private int say = 0;
    private String anlam ;
    private String kelime ;

    public frag4_Show() {
        this.anlam = "aha da cevap";
        this.kelime = "önce question\n press for meaning";
    }

    public static frag4_Show newInstance() {
        return new frag4_Show();
    }

    public void contextten_calistirilcak_frag4_metodu(String kel, String anl) {
        this.kelime = kel;
        this.anlam = anl;
        String kelime = kel.toUpperCase();
        SpannableString ss = new SpannableString(kelime + "_");
        ss.setSpan(new ForegroundColorSpan(Color.RED), kelime.length(), ss.length(), 0);
        ss.setSpan(new AbsoluteSizeSpan(24), kelime.length(), ss.length(), 0);
        tk4.setText(ss);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {// if (context instanceof Imylistener) {
            mCallback4 = (Listener4) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Imylistener hatası");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_frag4, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tk4 = getView().findViewById(R.id.textView_kelime4);

        registerForContextMenu(tk4);

        btndontask = getView().findViewById(R.id.btnDontAsk);
        contextten_calistirilcak_frag4_metodu(kelime, anlam);

        tk4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                say++;
                if (say % 2 == 0) {
                   mCallback4.push_random_word();
                } else {
                    tk4.setText(anlam);
                }
            }
        });

        btndontask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback4.onIndexDontAsk(kelime);
            }
        });
    }

    public void onDetach() {
        super.onDetach();
        tk4 = null;
        mCallback4 = null;
    }

    public interface Listener4 {
        void push_random_word( );
        void onIndexDontAsk(String kelime);
        void call_frag_3_from_4(String kelime);
        void onDelWordSelected(String kelime);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.my_contex_menu, menu);
        //  menu.setHeaderTitle("...");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.contex_menu_update:
                mCallback4.call_frag_3_from_4( kelime);
                return true;
            case R.id.contex_menu_sil:
                alert_goster();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void alert_goster() {

        new AlertDialog.Builder(getActivity())
                //  .setTitle("Kayıt Silmece")
                .setMessage("Silmek istiyormusun?\n" + kelime)
                //.setIcon(R.drawable.my_icon)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mCallback4.onDelWordSelected(kelime);
                    }
                })
                .setNegativeButton("İptal", null)
                .show();
    }
}