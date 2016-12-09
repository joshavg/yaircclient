package de.joshavg.yaircclient;

import java.util.ArrayList;

public class RingBuffer<T> extends ArrayList<T> {

    private int position;

    private int size;

    @Override
    public boolean add(final T e) {
        ++this.size;
        final boolean b = super.add(e);
        this.position = super.indexOf(e);
        return b;
    }

    public T next() {
        ++this.position;
        if (this.position >= this.size) {
            this.position = 0;
        }

        if (this.size == 0) {
            this.position = 0;
            return null;
        }

        return get(this.position);
    }

    public T prev() {
        --this.position;
        if (this.position < 0) {
            this.position = this.size - 1;
        }

        if (this.size == 0) {
            this.position = 0;
            return null;
        }

        return get(this.position);
    }

    @Override
    public T remove(final int index) {
        --this.size;
        return super.remove(index);
    }

    @Override
    public boolean remove(final Object o) {
        --this.size;
        return super.remove(o);
    }

}
