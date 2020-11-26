package com.mc.dict_frag;

public class Unknown {
  
  public Unknown() {}
  
  public interface UnknownListener {
    void displayMessage(CharSequence message);
  }
  
  private UnknownListener mUnknownListener;
  
  public void setUnknownListener(UnknownListener listener) {
    mUnknownListener = listener;
  }
  
  private void doSomething() {
    if(mUnknownListener != null) {
      mUnknownListener.displayMessage("interface mesaj");
    }
  }
}
