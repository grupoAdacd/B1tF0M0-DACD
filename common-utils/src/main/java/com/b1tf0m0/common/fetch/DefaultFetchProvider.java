package com.b1tf0m0.common.fetch;

import java.time.LocalDateTime;

public interface DefaultFetchProvider {
    public String fetchInformation();
    public String fetchWhenInformation(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
