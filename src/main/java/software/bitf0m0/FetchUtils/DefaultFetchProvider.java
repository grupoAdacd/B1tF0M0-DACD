package software.bitf0m0.FetchUtils;

import java.time.LocalDateTime;

public interface DefaultFetchProvider {
    public String fetchInformation();
    public String fetchWhenInformation(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
