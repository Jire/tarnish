package com.osroyale.datastructure;

import java.util.Iterator;

public class IterableDualNodeQueueIterator implements Iterator {
    IterableDualNodeQueue queue;

    DualNode head;

    DualNode last = null;

    IterableDualNodeQueueIterator(IterableDualNodeQueue var1) {
        this.queue = var1;
        this.head = this.queue.sentinel.previousDual;
        this.last = null;
    }

    public Object next() {
        DualNode var1 = this.head;
        if (var1 == this.queue.sentinel) {
            var1 = null;
            this.head = null;
        } else {
            this.head = var1.previousDual;
        }
        this.last = var1;
        return var1;
    }

    public boolean hasNext() {
        return this.queue.sentinel != this.head;
    }

    public void remove() {
        this.last.removeDual();
        this.last = null;
    }
}