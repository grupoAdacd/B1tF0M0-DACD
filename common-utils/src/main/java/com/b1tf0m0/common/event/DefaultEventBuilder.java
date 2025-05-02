package com.b1tf0m0.common.event;

import com.b1tf0m0.common.json.JSONParse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.Instant;

public class DefaultEventBuilder {

    public String buildEvent(String sourceSource, String rawJsonData) {
        try {
            JSONParse parser = new JSONParse(rawJsonData);
            JSONArray originalData = parser.parseArray();

            JSONObject event = new JSONObject();
            event.put("ts", Instant.now().toString());
            event.put("ss", sourceSource);
            event.put("atributos", originalData);

            return event.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
