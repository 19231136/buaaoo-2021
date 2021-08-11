package com.oocourse.spec3.main;

public interface EmojiMessage extends Message {
    /*@ public instance model non_null int emojiId;
      @ public instance model non_null int socialValue;
      @*/

    //@ invariant socialValue == emojiId;

    //@ ensures \result == emojiId;
    public /*@pure@*/ int getEmojiId();
}
