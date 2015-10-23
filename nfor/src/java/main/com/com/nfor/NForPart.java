package com.com.nfor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

class NForPart<T extends Number & Comparable<T>> {

    private static final List<Class> SUPPORTED_CLASSES = new ArrayList<>();

    static {
        SUPPORTED_CLASSES.add(Byte.class);
        SUPPORTED_CLASSES.add(Short.class);
        SUPPORTED_CLASSES.add(Integer.class);
        SUPPORTED_CLASSES.add(Long.class);
        SUPPORTED_CLASSES.add(Float.class);
        SUPPORTED_CLASSES.add(Double.class);
    }

    protected Class<T> clazz;
    protected int n = 0;
    protected NForFrom<T> from;
    protected NForBy<T> by;

    NForPart() {
    }

    public static <U extends Number & Comparable<U>> NForPart<U> of(Class<U> clazz) {
        if (!SUPPORTED_CLASSES.contains(clazz)) {
            throw new IllegalArgumentException(String.format("Unsupported class %s", clazz.getSimpleName()));
        }
        NForPart<U> part = new NForPart<>();
        part.clazz = clazz;
        return part;
    }

    public NForPart<T> ofLength(int n) {
        this.n = n;
        return this;
    }

    public NForPart from(T... from) {
        if (this.n == 0) {
            this.n = from.length;
        } else if (this.n != from.length) {
            throw new IllegalArgumentException(String.format("from was the wrong length, expecting %d but received %d",
                                                             n, from.length));
        }
        this.from = new NForFrom<T>(from);
        return this;
    }

    public NForPart by(T... by) {
        if (this.n == 0) {
            this.n = by.length;
        } else if (this.n != by.length) {
            throw new IllegalArgumentException(String.format("by was the wrong length, expecting %d but received %d",
                                                             n, by.length));
        }
        this.by = new NForBy<T>(by);
        return this;
    }

    public NFor to(T... to) {
        List<NForWhile.NForWhileTerm<T>> terms = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            terms.add(new NForWhile.NForWhileTerm<T>(NForWhile.NForWhileCondition.LESS_THAN, to[i]));
        }
        return to(terms.toArray(new NForWhile.NForWhileTerm[n]));
    }

    public NFor to(NForWhile.NForWhileTerm<T>... terms) {
        if (this.n == 0) {
            this.n = terms.length;
        } else if (this.n != terms.length) {
            throw new IllegalArgumentException(String.format("terms was the wrong length, expecting %d but received %d",
                                                             n, terms.length));
        }
        if (this.from == null) {
            this.from = defaultFrom(clazz, n);
        }
        if (this.by == null) {
            this.by = defaultBy(clazz, n);
        }
        return new NFor<T>(this, new NForWhile<T>(terms));
    }

    private static <U extends Number & Comparable<U>> NForFrom<U> defaultFrom(Class clazz, int n) {
        List<U> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            if (clazz == Byte.class) {
                list.add((U) Byte.valueOf((byte) 0));
            } else if (clazz == Short.class) {
                list.add((U) Short.valueOf((short) 0));
            } else if (clazz == Integer.class) {
                list.add((U) Integer.valueOf(0));
            } else if (clazz == Long.class) {
                list.add((U) Long.valueOf(0l));
            } else if (clazz == Float.class) {
                list.add((U) Float.valueOf(0f));
            } else if (clazz == Double.class) {
                list.add((U) Double.valueOf(0d));
            } else {
                throw new UnsupportedOperationException(String.format("Unsupported class %s", clazz.getSimpleName()));
            }
        }
        return new NForFrom<U>(list.toArray((U[]) Array.newInstance(clazz, n)));
    }

    private static <U extends Number & Comparable<U>> NForBy<U> defaultBy(Class clazz, int n) {
        List<U> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            if (clazz == Byte.class) {
                list.add((U) Byte.valueOf((byte) 1));
            } else if (clazz == Short.class) {
                list.add((U) Short.valueOf((short) 1));
            } else if (clazz == Integer.class) {
                list.add((U) Integer.valueOf(1));
            } else if (clazz == Long.class) {
                list.add((U) Long.valueOf(1l));
            } else if (clazz == Float.class) {
                list.add((U) Float.valueOf(1f));
            } else if (clazz == Double.class) {
                list.add((U) Double.valueOf(1d));
            } else {
                throw new UnsupportedOperationException(String.format("Unsupported class %s", clazz.getSimpleName()));
            }
        }
        return new NForBy<U>(list.toArray((U[]) Array.newInstance(clazz, n)));
    }

}