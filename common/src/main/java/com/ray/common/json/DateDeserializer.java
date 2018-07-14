package com.ray.common.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.ray.common.util.Dates;

import java.lang.reflect.Type;
import java.util.Date;

public final class DateDeserializer implements JsonDeserializer<Date> {

    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            return new Date(json.getAsJsonPrimitive().getAsLong());
        } else if (json.isJsonObject()) {
            return Dates.parse(json.getAsString());
        } else {
            return null;
        }
    }
}