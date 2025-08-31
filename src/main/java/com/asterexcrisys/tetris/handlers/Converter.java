package com.asterexcrisys.tetris.handlers;

import java.io.IOException;

public interface Converter<T> {

    void serialize(T object) throws IOException;

    T deserialize(Class<T> type) throws IOException;

}