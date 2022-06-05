package com.javarush.task.task37.task3707;

import java.io.*;
import java.util.*;

public class AmigoSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, Serializable {

    private static final Object PRESENT = new Object();
    private transient HashMap<E,Object> map;

    public AmigoSet() {
        this.map = new HashMap<>();
    }

    public AmigoSet(Collection<? extends E> collection) {
        this.map = new HashMap<>(Math.max((int) (collection.size() / .75f)+ 1,16));
        addAll(collection);
    }
    @Override
    public Iterator<E> iterator(){
       return   map.keySet().iterator();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            AmigoSet<E> newSet = (AmigoSet<E>) super.clone();
            newSet.map = (HashMap<E, Object>) map.clone();
            return newSet;
        }catch (Exception e){
            throw new InternalError();
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean add(E e) {
       return map.put(e,PRESENT) == null;
    }

    private void writeObject(ObjectOutputStream objectOuput) throws IOException {
        try {
            objectOuput.defaultWriteObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        objectOuput.writeInt(HashMapReflectionHelper.<Integer>callHiddenMethod(map,"capacity"));
        objectOuput.writeFloat(HashMapReflectionHelper.<Float>callHiddenMethod(map,"loadFactor"));
        objectOuput.writeInt(map.size());

        for(E e: map.keySet()){
            objectOuput.writeObject(e);
        }
    }

    private void readObject(ObjectInputStream objectInput) throws IOException, ClassNotFoundException {
        try {
            objectInput.defaultReadObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        int capacity = objectInput.readInt();
        float loadFactor = objectInput.readFloat();

        map = new HashMap<>(capacity,loadFactor);

        int size = objectInput.readInt();

        for (int i = 0; i < size; i++) {
            E e = (E) objectInput.readObject();
            map.put(e,PRESENT);
        }
    }


}
