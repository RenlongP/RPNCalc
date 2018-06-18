package com.airwallex.mamo;

public interface MamoPad<E> {
    void makeNote(E content);

    E readLatest();

    int getSize();
}
