package io.scalecube.storages.mvstore;

import java.io.IOException;

public interface Storage<K, V> {

    void write(K k, V v) throws IOException;

    V read(K k) throws IOException;

    void close();
}
