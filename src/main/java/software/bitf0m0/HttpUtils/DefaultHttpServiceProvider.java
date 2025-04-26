package software.bitf0m0.HttpUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface DefaultHttpServiceProvider {
    public HttpClient createClient();
    public HttpRequest createRequest();
    public HttpResponse<String> sendRequest() throws InterruptedException, IOException;
}