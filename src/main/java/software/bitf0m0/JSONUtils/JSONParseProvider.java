package software.bitf0m0.JSONUtils;
import org.json.*;

public interface JSONParseProvider {
    JSONObject parseObject() throws JSONException;
    JSONArray parseArray() throws JSONException;
}
