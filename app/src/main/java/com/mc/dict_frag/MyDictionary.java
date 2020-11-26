package com.mc.dict_frag;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.internal.ContextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class MyDictionary {

    final ArrayList<String> key, sorma  ;
    final HashMap<String, String> dic ;
    final String my_seperator="=";
    final String my_extension= ".txt";

    public MyDictionary() {
        key = new ArrayList<>();
        dic = new HashMap<>();
        sorma = new ArrayList<>();
    }

    public int dont_ask_read(String filename) {

        try {
            File f = new File(filename + ".txt");
            Scanner fr = new Scanner(f);
            while (fr.hasNextLine()) {
                String sss = fr.nextLine();
               sorma.add(sss);
                }
            fr.close();
            return 0;
        } catch (FileNotFoundException e) {
            Log.d("cav", " : sorulmuycaklar okuma hatası"+e);
            return -1;
        }
    }

    public char[] dont_ask_write (String fileName, String kelime) {
        try {
            File file = new File(fileName+my_extension);
            FileWriter writer = new FileWriter(file, true);
            writer.append(kelime).append(System.getProperty("line.separator"));
            writer.flush();
            writer.close();
            return new char[]{'o', 'k'};
        } catch (IOException e) {
            Log.d("cav", "sorulmuycaklar yazma hatası"+e);
            return new char[]{'n', 'o'};
        }
    }

    public int append_to_file(String fileName, String kelime, String anlam) {
        try {
            File file = new File(fileName + my_extension);
            FileWriter writer = new FileWriter(file, true);
            writer.append(kelime).append(my_seperator).append(anlam).append(System.getProperty("line.separator"));
            writer.flush();
            writer.close();
            Log.d("cav", "tosyaya eklendi : "+fileName);
            return 0;

        } catch (IOException e) {
            Log.d("cav", "tosya yazma error : "+e.getMessage());
            return -1;
        }
    }

    public int update_on_file(String filename, String degisen_kelime, String degisen_anlam, String silincek_kelime) {

        File f = new File(filename + my_extension);
        File f_temp = new File(filename + "TEMP.txt");

        try {
            Scanner fr = new Scanner(f);
            FileWriter yaz = new FileWriter(f_temp);

            while (fr.hasNextLine()) {
                String data = fr.nextLine();
                String[] dat = data.split(my_seperator);

                if (dat[0].contentEquals(silincek_kelime)) { //equalsIgnoreCase
                    yaz.append(degisen_kelime).append(my_seperator).append(degisen_anlam).append(System.getProperty("line.separator"));
                    continue;
                }

                yaz.write(data + System.getProperty("line.separator"));
            }
            fr.close();
            yaz.close();
            if (!f.delete()) {
                Log.d("cav", "kayıt_degis delete hatası!");
                return 2;
            }
            if (!f_temp.renameTo(f)) {
                Log.d("cav", "kayıt_degis renaming hatası!");
                return 3;
            }
            return 0;
        } catch (IOException e) {
            Log.d("cav", "kayıt_silde yazma tosyası hata!");
            return -1;
        }
    }

    public int del_from_file(String filename, String silincek_kelime) {

        File f = new File(filename + my_extension);
        File f_temp = new File(filename + "TEMP.txt");

        try {
            Scanner fr = new Scanner(f);
            FileWriter yaz = new FileWriter(f_temp);
            while (fr.hasNextLine()) {
                String data = fr.nextLine();
                String[] dat = data.split(my_seperator);
                if (dat[0].contentEquals(silincek_kelime)) { //equalsIgnoreCase
                    continue;
                }
                yaz.write(data + System.getProperty("line.separator"));
            }
            fr.close();
            yaz.close();
            if (!f.delete()) {
                Log.d("cav", "kayıt_silde delete hatası!");
                return 2;
            }
            if (!f_temp.renameTo(f)) {
                Log.d("cav", "kayıt_silde renaming hatası!");
                return 3;
            }
            return 0;
        } catch (IOException e) {
            Log.d("cav", "kayıt_silde yazma tosyası hata!");
            return -1;
        }
    }

    public int load_from_file(String filename) {
        try {
            File f = new File(filename + my_extension);
            Scanner fr = new Scanner(f);
//DOSYA VAR AMA BOŞSA NAPCAZ?
            while (fr.hasNextLine()) {
                String data = fr.nextLine();
                String[] dat = data.split(my_seperator);
                if (dat[0] == null || dat[1] == null) {
                    Log.d("cav", "load file da dat[] boş çıktı!");
                    return -1;
                }
                dic.put(dat[0], dat[1]);
                //   index = nameNumberString.indexOf('!');
                //   name = nameNumberString.substring(0, index);
            }
            fr.close();
            return 0;

        } catch (FileNotFoundException e) {
            return -1;
        }
    }

    public int len() {
        return dic.size();
    }

    public boolean is_empty() {
        return dic.size() <= 0;
    }

    public String len_str() {
        return Integer.toString(dic.size());
    }

    public void add_to_dic(String a, String b) {
        this.dic.put(a, b);
    }

    public void clear() {
        key.clear();
        dic.clear();
        sorma.clear();
    }

    public void refresh_keys() {
        if (!dic.isEmpty()) {
            key.clear();
            key.addAll(dic.keySet());
        }
        Collections.sort(key);
    }
}

/*
---------------------------------------------------------------
public class ObjectIOExample {

    private static final String filepath="C:\\Users\\nikos7\\Desktop\\obj";

    public static void main(String args[]) {

        ObjectIOExample objectIO = new ObjectIOExample();

        Student student = new Student("John","Frost",22);
        objectIO.WriteObjectToFile(student);
    }

    public void WriteObjectToFile(Object serObj) {

        try {

            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
 */

/* ---------------------------------------------------------------
public class Student implements Serializable {

    //default serialVersion id
    private static final long serialVersionUID = 1L;

    private String first_name;
    private String last_name;
    private int age;

    public Student(String fname, String lname, int age){
        this.first_name = fname;
        this.last_name  = lname;
        this.age        = age;
    }

    public void setFirstName(String fname) {
        this.first_name = fname;
    }

    public String getFirstName() {
        return this.first_name;
    }

    public void setLastName(String lname) {
        this.first_name = lname;
    }

    public String getLastName() {
        return this.last_name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public String toString() {
        return new StringBuffer(" First Name: ").append(this.first_name)
                .append(" Last Name : ").append(this.last_name).append(" Age : ").append(this.age).toString();
    }

}
 */