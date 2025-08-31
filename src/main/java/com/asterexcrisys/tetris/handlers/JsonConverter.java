package com.asterexcrisys.tetris.handlers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonConverter<T> implements Converter<T> {

    private static final Logger LOGGER = Logger.getLogger(JsonConverter.class.getName());

    private final File file;
    private final ObjectMapper mapper;

    public JsonConverter(String file) {
        this.file = new File(Objects.requireNonNull(file));
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        initializeFile();
    }

    @Override
    public void serialize(T object) throws IOException {
        mapper.writeValue(file, object);
    }

    @Override
    public T deserialize(Class<T> type) throws IOException {
        return mapper.readValue(file, type);
    }

    private void initializeFile() {
        if (Files.exists(file.toPath())) {
            return;
        }
        try {
            Files.createFile(file.toPath());
            Files.write(file.toPath(), "{}".getBytes());
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
        }
    }

}