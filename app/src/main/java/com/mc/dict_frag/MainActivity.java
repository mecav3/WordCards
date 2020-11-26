package com.mc.dict_frag;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements frag1.Imylistener, frag2_Add.Listener2,
                         frag3_Update.Listener3, frag4_Show.Listener4,frag5_Files.Listener5,
                                  frag7_New_File.Listener7, Unknown.UnknownListener {

    final MyDictionary d = new MyDictionary();
    int index, sor_index = 0;
    String fileNameFullPath, fileName;
    
    private Unknown mUnknown;
    
    private void longOperation() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                setTitle(msg.obj +"");
                pass_new_FileName(msg.obj +"");
                
            }
        };
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                
                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                Message msg = new Message();
               
               String yol= MainActivity.this.getExternalFilesDir(null).toString() + File.separator ;
               for (int i=0; i<5000; i++) {
                  d.append_to_file(yol + "test3000", i+"a" , "melo"+i);
               }
               msg.obj = "test3000";
               handler.sendMessage(msg);
            }
        }).start();
    }
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mUnknown = new Unknown();
        mUnknown.setUnknownListener(this);
        
        //save which fragment. in saveinstance
    }
    
    @Override
    public void displayMessage(CharSequence message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnknown.setUnknownListener(null);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main:
                call_frag_1();
                break;
            case R.id.menu_show:
                call_frag_4();
                break;
            case R.id.menu_list:
                call_frag_6();
                break;
            case R.id.menu_add:
                call_frag_2();
                break;
            case R.id.menu_addFile:
                call_frag_7();
                break;
            case R.id.menu_file:
                call_frag_5();
                break;
            case R.id.menu_dosya_share:
                dosya_send_mail();
                break;
            case R.id.menu_kelime_share:
                kelime_share();
                break;
            case R.id.menu_sub_sList:
                call_frag_8();
                break;
            case R.id.menu_sub_import_test:
                try {
                    kopi_import();
                } catch (IOException e) {
                    mesaj("GENEL hata");
                }
                break;
            case R.id.menu_sub_generate_test:
              longOperation();
               
                break;
        }
        return super.onOptionsItemSelected(item);
    }
   
    public void kopi_import() throws IOException {

        File dest = new File(getExternalFilesDir(null), "mytest.txt");
        if (dest.exists()) {
            mesaj("dosya var overwriting");
        }
        InputStream fis;

        try {
            fis = getAssets().open("mytest.txt");
            mesaj("asset opendı");
        } catch (Exception e) {
            mesaj("asset dosyası yok" + e.getMessage());
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(dest);
            mesaj("kopi başı");
            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {

                fos.write(buffer, 0, length);
            }
            mesaj("copiledi");
            fis.close();
            fos.close();

            change_file("mytest");

        } catch (FileNotFoundException e) {
            mesaj("kopi olmadu" + e.getMessage());
        }
    }

    @Override
    public void pass_new_FileName(String str) {
        SharedPreferences sP = getSharedPreferences("all_files", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        if (!sP.contains("key_mytest")) {
            editor.putString("key_mytest", "mytest");
        }
        editor.putString("key_" + str, str);
        editor.apply();
        baslangic(str);
    }

    private void baslangic(String file_) {
        ArrayList<String> key_sorma;
        fileName = file_;
        fileNameFullPath = getExternalFilesDir(null).toString() + File.separator + fileName;
        d.clear();
        if (d.load_from_file(fileNameFullPath) == -1) {
            Log.d("cav", "dosya yazma  hata!");
            mesaj("tosya yüklenemedi boş sankim\nyeni kelime ekle");
            call_frag_2();
            setTitle(fileName + "=" + d.len_str());
        } else {
            d.refresh_keys();
            call_frag_1();
            setTitle(fileName + "=" + d.len_str());

            if (d.dont_ask_read(fileNameFullPath + "_sorma") == -1) {
                mesaj("sorma boş");
                d.sorma.addAll(d.key);
                mesaj("sor size : " + d.sorma.size());
            } else {
                mesaj("sorma size : " + d.sorma.size());
                key_sorma = new ArrayList<>(d.sorma);
                d.sorma.clear();
                for (String i : d.key) {
                    if (!key_sorma.contains(i)) {
                        d.sorma.add(i);
                    }
                }
                mesaj("sor size : " + d.sorma.size());
            }
        }
    }

    @Override
    public void change_file(String str) {
        baslangic(str);
    }
    
    @Override
    public void onIndexSelected(int index) {
        if (d.is_empty()) {
            mesaj(" kayıt yok");
            return;
        }
        this.index = index;
        frag1e_gonder();
    }

    public void kayit_sil() {
        if (d.is_empty()) {
            mesaj("silincek kayıt yok ");
            return;
        }
        int ind = index;
        mesaj("**_ind " + ind + "**_index " + index);
        mesaj("" + d.del_from_file(fileNameFullPath, d.key.get(ind)));
        d.dic.remove(d.key.get(ind));
        d.refresh_keys();
        if (ind > 1) {
            index--;
        }
        mesaj("#_ind" + ind + "#_index" + index);
        setTitle(fileName + "=" + d.len_str());
        call_frag_1();
    }

    public void kayit_sil_word(String kelime) {
        if (d.is_empty()) {
            mesaj("silincek kayıt yok ");
            return;
        }
        mesaj("" + d.del_from_file(fileNameFullPath, d.dic.get(kelime)));
        d.dic.remove(d.dic.get(kelime));
        d.refresh_keys();
        setTitle(fileName + "=" + d.len_str());
        call_frag_4();
    }
    @Override
    public void onDelIndexSelected(int index) {
        if (d.is_empty()) {
            mesaj(" kayıt yok");
            return;
        }
        this.index = index;
        kayit_sil();
    }

    @Override
    public void onDelWordSelected(String kelime) {
        if (d.is_empty()) {
            mesaj(" kayıt yok");
            return;
        }
        kayit_sil_word(kelime);
    }

    @Override
    public void push_random_word() {
        if (d.sorma.isEmpty()) {
            mesaj(" sor kayıt yok");
            return;
        }
        Random r = new Random();
        sor_index = r.nextInt(getsorMaxValue() + 1);
        frag4e_gonder();
    }

    public void frag4e_gonder() {
        frag4_Show fragment = (frag4_Show) getSupportFragmentManager().findFragmentByTag("frag_tag4");
        if (fragment != null) {
            fragment.contextten_calistirilcak_frag4_metodu(d.sorma.get(sor_index), d.dic.get(d.sorma.get(sor_index)));
        } else {
            Toast.makeText(this, "frag 4 YOK", Toast.LENGTH_SHORT).show();
        }
    }

    public void onIndexDontAsk(String kelime) {
//yazdığın varmı çek et
        String fileFull = getExternalFilesDir(null).toString() + File.separator + fileName + "_sorma";
        mesaj(String.valueOf(d.dont_ask_write(fileFull, kelime))); //dosyaya yaz
        d.sorma.remove(kelime); //canlı listeden çıkar
    }

    public void frag1e_gonder() {
        frag1 fragment = (frag1) getSupportFragmentManager().findFragmentByTag("frag_tag1");
        if (fragment != null) {
            fragment.merkezden_frag1e(d.key.get(index), d.dic.get(d.key.get(index)));
        } else {
            Toast.makeText(this, "frag 1 YOK", Toast.LENGTH_SHORT).show();
        }
    }

    public void call_frag_1() {
        frag1 frag11 = frag1.newInstance(getSeekBarMaxValue());
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.layout_Frame_id, frag11, "frag_tag1")
            .addToBackStack(null)
            .commit();
    }

    public void call_frag_2() {
        frag2_Add frag22 = frag2_Add.newInstance(getSeekBarMaxValue());
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.layout_Frame_id, frag22, "frag_tag2")
            .addToBackStack(null)
            .commit();
    }

    public void call_frag_3_from_1() {
        if (d.is_empty()) {
            mesaj(" güncellenecek kayıt yok");
            return;
        }
        frag3_Update frag33 = frag3_Update.newInstance(d.key.get(index), d.dic.get(d.key.get(index)),1);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.layout_Frame_id, frag33, "frag_tag3")
            .addToBackStack(null)
            .commit();
    }
    public void call_frag_3_from_4(String kelime) {
        if (d.is_empty()) {
            mesaj(" güncellenecek kayıt yok");
            return;
        }
        frag3_Update frag33 = frag3_Update.newInstance(kelime, d.dic.get(kelime),4);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.layout_Frame_id, frag33, "frag_tag3")
            .addToBackStack(null)
            .commit();
    }
    public void call_frag_4() {

        frag4_Show frag44 = frag4_Show.newInstance();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.layout_Frame_id, frag44, "frag_tag4")
            .addToBackStack(null)
            .commit();
    }

    public void call_frag_5() {
        frag5_Files frag55 = frag5_Files.newInstance();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.layout_Frame_id, frag55, "frag_tag5")
            .addToBackStack(null)
            .commit();
    }

    public void call_frag_6() {
        if (d.is_empty()) {
            mesaj("kayıt yok ");
            return;
        }
        ArrayList<String> mylist = new ArrayList<>();

       /* list all
       for (String i : d.key) {
            mylist.add(i + "£" + d.dic.get(i));
        }*/

        for (String i : d.sorma) {//list sorulcaklar
            mylist.add(i + "£" + d.dic.get(i));
        }

        frag6_cList frag66 = frag6_cList.newInstance(mylist);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.layout_Frame_id, frag66, "frag_tag6")
            .addToBackStack(null)
            .commit();
    }

    private void call_frag_7() {
        frag7_New_File frag77 = frag7_New_File.newInstance();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.layout_Frame_id, frag77, "frag_tag7")
            .addToBackStack(null)
            .commit();
    }

    public void call_frag_8() {
        if (d.is_empty()) {
            mesaj("kayıt yok ");
            return;
        }
        ArrayList<String> mylist = new ArrayList<>();

        for (String i : d.key) {//list sorma
            if (!d.sorma.contains(i)) {
                mylist.add(i + "£" + d.dic.get(i));
            }
        }

        frag8_sList frag88 = frag8_sList.newInstance(mylist);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.layout_Frame_id, frag88, "frag_tag8")
            .addToBackStack(null)
            .commit();
    }

    private int getSeekBarMaxValue() {
        int SeekBarMaxValue = -1;
        if (d.len() >= 1) {
            SeekBarMaxValue = d.len() - 1;
        }
        return SeekBarMaxValue;
    }

    private int getsorMaxValue() {
        int sormaMaxValue = -1;
        if (d.sorma.size() >= 1) {
            sormaMaxValue = d.sorma.size() - 1;
        }
        return sormaMaxValue;
    }

    public void onUpdateWord(String k, String a, String s) {
        d.dic.remove(s);
        d.add_to_dic(k, a);
        d.refresh_keys();
        mesaj("dosya update sonuc " + d.update_on_file(fileNameFullPath, k, a, s));
        call_frag_1();
    }

    public void onNewWordEnter(String kel, String anl) {
        if (d.key.contains(kel)) {
            mesaj("bu kayıt var");
            return;
        }
        d.add_to_dic(kel, anl);
        d.sorma.add(kel);
        d.refresh_keys();
        if (d.append_to_file(fileNameFullPath, kel, anl) == -1) {
            mesaj("dosyaya yazamadı!");
        } else {
            mesaj(String.format("yeni kayıt %s", d.len_str()));
            setTitle(fileName + "=" + d.len_str());
        }
    }

    private void dosya_send_mail() {
        String s = fileNameFullPath + ".txt";
        File fileToShare = new File(s);
        if (!fileToShare.exists()) {
            mesaj("no file to Share");
            return;
        }
        mesaj(s + "sharing..");
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"mecav3@gmail.com"});
        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileToShare));
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "my_dict gönderimi");
        i.putExtra(android.content.Intent.EXTRA_TEXT, "sözlük ektedir");
        startActivity(Intent.createChooser(i, "Send file..."));
    }

    private void kelime_share() {
        if (d.is_empty()) {
            mesaj("paylaşcak kayıt yok ");
            return;
        }
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, d.key.get(index) + " = " + d.dic.get(d.key.get(index)));
        startActivity(Intent.createChooser(i, "Paylaşıver gaarin!!"));
    }

    public void mesaj(String str) {
        Toast t = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        t.show();
    }
    
    protected void onPause() {
        super.onPause();
        yaz_Prefs();
    }

    protected void onResume() {
        super.onResume();
        get_last_file();
        baslangic(fileName);
    }

    public void yaz_Prefs() {
        SharedPreferences sP = getSharedPreferences("last_used", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString("key_last_file_used", fileName);
        editor.putInt("key_last_index", index);
        editor.apply();
    }

    public void get_last_file() {
        SharedPreferences pref = getSharedPreferences("last_used", Context.MODE_PRIVATE);
        fileName = pref.getString("key_last_file_used", "mytest");
        index = pref.getInt("key_last_index", 0);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
}

/* public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                mesaj("WRITE is granted2");
                return true;
            } else {
                mesaj( "WRITE is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            mesaj( "WRITE is granted2");
            return true;
        }
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
               mesaj( "READ is granted1");
                return true;
            } else {
                mesaj("READ is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            mesaj("READ is granted1");
            return true;
        }
    }*/