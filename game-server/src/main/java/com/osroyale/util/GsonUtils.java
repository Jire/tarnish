package com.osroyale.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.OptionalInt;

public enum GsonUtils {

    ;

    public static final ThreadLocal<Gson> JSON_ALLOW_NULL = ThreadLocal.withInitial(() ->
            new GsonBuilder().disableHtmlEscaping().serializeNulls().create());
    public static final ThreadLocal<Gson> JSON_PRETTY_ALLOW_NULL = ThreadLocal.withInitial(() ->
            new GsonBuilder()
                    .registerTypeAdapter(OptionalInt.class, new OptionalIntAdapter())
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .serializeNulls()
                    .create());
    public static final ThreadLocal<Gson> JSON_PRETTY_NO_NULLS = ThreadLocal.withInitial(() ->
            new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create());

    public static Gson jsonAllowNull() {
        return JSON_ALLOW_NULL.get();
    }

    public static Gson jsonPrettyAllowNull() {
        return JSON_PRETTY_ALLOW_NULL.get();
    }

    public static Gson jsonPrettyNoNulls() {
        return JSON_PRETTY_NO_NULLS.get();
    }

    public static Gson json() {
        return jsonPrettyAllowNull();
    }

}
