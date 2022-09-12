package com.lev_prav.client.utility;

import com.lev_prav.common.exceptions.IllegalValueException;

@FunctionalInterface
public interface Reader<T> {
    T read() throws IllegalValueException;
}
