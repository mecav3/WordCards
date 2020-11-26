package com.mc.dict_frag;

import android.content.Context;
import android.widget.Toast;

import java.util.Random;

public class MyThread implements Runnable {
  Context ctx;
  String name;
  int time;
  Random r = new Random();
  
  public MyThread(String name_, Context ctx) {
    name = name_;
    time = 5 * r.nextInt(999);
    this.ctx = ctx;
  }
  
  @Override
  public void run() {
    
    try {
      Toast.makeText(ctx, name + " started", Toast.LENGTH_SHORT).show();
      Thread.sleep(time);
    } catch (InterruptedException e) {
      Toast.makeText(ctx, "tread error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    Toast.makeText(ctx, name + " sleeped", Toast.LENGTH_SHORT).show();
  }
}