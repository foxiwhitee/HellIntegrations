package foxiwhitee.hellmod.utils;

import java.util.Objects;

public class PairKV<K, V> {
    private K key;

    private V value;

    public PairKV(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PairKV))
            return false;
        PairKV<?, ?> pairKV = (PairKV<?, ?>)o;
        return (Objects.equals(getKey(), pairKV.getKey()) && Objects.equals(getValue(), pairKV.getValue()));
    }

    public int hashCode() {
        return Objects.hash(new Object[] { getKey(), getValue() });
    }
}

