package com.b1tf0m0.common.json;
import org.json.*;

public interface JSONParseProvider {
    JSONObject parseObject() throws JSONException;
    JSONArray parseArray() throws JSONException;
}
