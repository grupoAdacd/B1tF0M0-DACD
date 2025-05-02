package com.b1tf0m0.common.fetch.DefaultBinanceFetchClasses;

import java.time.LocalDateTime;

public interface DefaultBinanceFetchProvider {
    String fetchWhenInformation(long startDateTime, long endDateTime);
}
