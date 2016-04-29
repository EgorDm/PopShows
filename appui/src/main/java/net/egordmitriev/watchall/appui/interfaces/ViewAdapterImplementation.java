package net.egordmitriev.watchall.appui.interfaces;

import java.util.Collection;

/**
 * Created by EgorDm on 4/29/2016.
 */
public interface ViewAdapterImplementation<T> {
    void set(final Collection<? extends T> collection);

    boolean add(T var1);
    void add(int var1, T var2);
    boolean addAll(final Collection<? extends T> collection);

    boolean remove(T var1);
    T remove(int var1);

    boolean contains(T var1);
    void clear();
}
