package com.oocourse.spec3.main;

public interface NoticeMessage extends Message {
    /*@ public instance model non_null String string;
      @ public instance model non_null int socialValue;
      @*/

    //@ invariant socialValue == string.length();

    //@ ensures \result == string;
    public /*@pure@*/ String getString();
}
