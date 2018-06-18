package com.airwallex.mamo;

import java.util.LinkedList;

public class MamoPadImpl<E> extends LinkedList<E> implements MamoPad<E> {
    @Override
    public void makeNote(E content) {
        this.push(content);
    }

    @Override
    public E readLatest() {
        this.pop();
        return this.pop();
    }

    @Override
    public int getSize() {
        return this.size();
    }
}
