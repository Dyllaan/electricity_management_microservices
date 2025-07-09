package com.louisfiges.common.http;

import java.util.Optional;

public interface ServiceInterface<T, D extends DAO> {

    D create(D o);

    D read(T i);

    D update(D o);

    void delete(T i);

    Optional<D> find(T i);

    boolean exists(T i);
}
