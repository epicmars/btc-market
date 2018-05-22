package com.fafabtc.common.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;


/**
 * Created by jastrelax on 2018/5/20.
 */
public class BooleanTypeAdapter extends TypeAdapter<Boolean> {

    @Override
    public Boolean read(JsonReader in) throws IOException{
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return in.nextInt() == 1;
    }

    @Override
    public void write(JsonWriter out, Boolean value) throws IOException{
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value);
    }
}
