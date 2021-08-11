package com.oocourse.spec3.main;

public interface RedEnvelopeMessage extends Message {
    /*@ public instance model non_null int money;
      @ public instance model non_null int socialValue;
      @*/

    //@ invariant socialValue == money * 5;

    //@ ensures \result == money;
    public /*@pure@*/ int getMoney();
}
