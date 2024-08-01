package com.osroyale.datastructure;

import java.util.Iterator;

public class IterableNodeHashTableIterator implements Iterator {

    IterableNodeHashTable hashTable;

    Node head;

    int index;

    Node last = null;

    IterableNodeHashTableIterator(IterableNodeHashTable var1) {
        this.hashTable = var1;
        this.start();
    }

    void start() {
        this.head = this.hashTable.buckets[0].previous;
        this.index = 1;
        this.last = null;
    }

    public Object next() {
        Node var1;
        if (this.hashTable.buckets[this.index - 1] != this.head) {
            var1 = this.head;
            this.head = var1.previous;
            this.last = var1;
            return var1;
        } else {
            do {
                if (this.index >= this.hashTable.size) {
                    return null;
                }
                var1 = this.hashTable.buckets[this.index++].previous;
            } while (var1 == this.hashTable.buckets[this.index - 1]);
            this.head = var1.previous;
            this.last = var1;
            return var1;
        }
    }

    public boolean hasNext() {
        if (this.hashTable.buckets[this.index - 1] != this.head) {
            return true;
        } else {
            while (this.index < this.hashTable.size) {
                if (this.hashTable.buckets[this.index++].previous != this.hashTable.buckets[this.index - 1]) {
                    this.head = this.hashTable.buckets[this.index - 1].previous;
                    return true;
                }
                this.head = this.hashTable.buckets[this.index - 1];
            }
            return false;
        }
    }

    public void remove() {
        this.last.remove();
        this.last = null;
    }
}