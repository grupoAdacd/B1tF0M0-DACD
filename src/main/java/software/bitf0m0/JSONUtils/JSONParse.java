package software.bitf0m0.JSONUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParse implements JSONParseProvider{

    private final String jsonString;

    public JSONParse(String jsonString) {
        this.jsonString = jsonString;
    }

    @Override
    public JSONObject parseObject() throws JSONException {
        return new JSONObject(this.jsonString);
    }

    @Override
    public JSONArray parseArray() throws JSONException {
        return new JSONArray(this.jsonString);
    }
}
