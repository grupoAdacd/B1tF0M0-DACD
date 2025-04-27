package com.b1tf0m0.common.api;

public class ApiUrlBuilder implements DefaultApiUrlProvider {
    public final String baseUrl;
    public final String specify;
    public ApiUrlBuilder(String BaseUrl, String Specify) {
        this.baseUrl = BaseUrl;
        this.specify = Specify;
    }
    @Override
    public String createApiUrl() {
        return this.baseUrl + this.specify;
    }
}
