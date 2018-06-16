package com.airwallex.mamo;

public interface MamoPad<E> {
    public void makeNote(E content);

    public E readLatest();

    public int getSize();
}
