package com.mc.dict_frag;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.Random;

// addToBackStack()
// ANA EKRAN
public class frag1 extends Fragment {
    private static final String INDEKS_MAX = "indeks_max";
    TextView tk, ta;
    Button geri, ileri;
    SeekBar mySeekBar;
    Imylistener mCallback;
    private int index_, index_max;

    public frag1() {
    }

    public static frag1 newInstance(int ind) {
        frag1 myFragment = new frag1();
        Bundle bundle = new Bundle();
        bundle.putInt(INDEKS_MAX, ind);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    public void merkezden_frag1e(String kel, String anl) {
        String kelime = kel.toUpperCase();
        SpannableString ss = new SpannableString(kelime + " _" + index_ + "_");
        ss.setSpan(new ForegroundColorSpan(Color.RED), kelime.length(), ss.length(), 0);
        ss.setSpan(new AbsoluteSizeSpan(24), kelime.length(), ss.length(), 0);
        tk.setText(ss);
        ta.setText(anl);
    }

    private void get_args() {
        if (getArguments() != null) {
            index_max = getArguments().getInt(INDEKS_MAX);
            if (index_max == -1) {
                tk.setText("kayıt yok");
            } else {
                mCallback.onIndexSelected(get_last_index());
            }
        }
    }

    public int get_last_index() {
        SharedPreferences pref = getActivity().getSharedPreferences("last_used", Context.MODE_PRIVATE);
        index_ = pref.getInt("key_last_index", 0);
        if (index_ > index_max) {
            index_ = 0;
        }
        return index_;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {// if (context instanceof Imylistener) {
            mCallback = (Imylistener) context;
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
        return inflater.inflate(R.layout.layout_frag1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tk = getView().findViewById(R.id.textView_kelime4);
        ta = getView().findViewById(R.id.textView_anlam4);
        geri = getView().findViewById(R.id.button_geri);
        ileri = getView().findViewById(R.id.button_ileri);
        mySeekBar = getView().findViewById(R.id.myseekBar);

        registerForContextMenu(tk);

        get_args();

        if (index_max > -1) {
            mySeekBar.setMax(index_max);
        } else {
            mySeekBar.setMax(0);
        }
        mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar mySeekBar, int progressValue, boolean fromUser) {
                if (fromUser) {
                    index_ = progressValue;
                    mCallback.onIndexSelected(index_);
                }
            }
            public void onStartTrackingTouch(SeekBar mySeekBar) {
            }
            public void onStopTrackingTouch(SeekBar mySeekBar) {
            }
        });

        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index_--;
                if (index_ < 0) {
                    index_ = index_max;
                }
                mCallback.onIndexSelected(index_);
            }
        });

        ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index_++;
                if (index_ > index_max) {
                    index_ = 0;
                }
                mCallback.onIndexSelected(index_);
            }
        });

        tk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index_max == -1) {
                    Toast.makeText(getActivity(), " kayıt yok ", Toast.LENGTH_SHORT).show();
                    return;
                }
                Random r = new Random();
                index_ = r.nextInt(index_max + 1);
                if (index_ > index_max) {
                    index_ = 0;
                }
                mCallback.onIndexSelected(index_);
            }
        });

        ta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index_max == -1) {
                    Toast.makeText(getActivity(), " kayıt yok ", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                mCallback.call_frag_3_from_1();
                return true;
            case R.id.contex_menu_sil:
                alert_goster();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onDetach() {
        super.onDetach();
        tk = ta = geri = ileri = null;
        mCallback = null;
        mySeekBar = null;
    }

    public void alert_goster() {
        if (index_max == -1) {
            Toast.makeText(getActivity(), "silincek kayıt yok ", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(getActivity())
                //  .setTitle("Kayıt Silmece")
                .setMessage("Silmek istiyormusun Son Kararın mı ? ")
                //.setIcon(R.drawable.my_icon)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mCallback.onDelIndexSelected(index_);
                    }
                })
                .setNegativeButton("İptal", null)
                .show();
    }

    public interface Imylistener {
        void onIndexSelected(int index);
        void onDelIndexSelected(int index);
        void call_frag_3_from_1();
    }
}
