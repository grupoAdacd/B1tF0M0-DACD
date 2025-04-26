package software.bitf0m0.SaveUtils;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public interface DefaultFileSaverProvider {
    public final DateTimeFormatter FILE_NAME_FORMAT = DateTimeFormatter.ofPattern("MMddyyyy");
    public final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public String createFileName();
    public String createPathToFile();
    public File createFile();
    public void writeToFile() throws IOException;
}
