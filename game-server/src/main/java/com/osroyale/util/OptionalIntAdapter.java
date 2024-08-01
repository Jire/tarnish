package com.osroyale.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.OptionalInt;

public class OptionalIntAdapter extends TypeAdapter<OptionalInt> {

    @Override
    public void write(JsonWriter out, OptionalInt value) throws IOException {
        if (value.isPresent()) {
            out.value(value.getAsInt());
        } else {
            out.nullValue();
        }
    }

    @Override
    public OptionalInt read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return OptionalInt.empty();
        } else {
            return OptionalInt.of(in.nextInt());
        }
    }
}
