package software.bitf0m0.SaveUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DefaultFileSaver implements DefaultFileSaverProvider{
    private final String DataLakeDirectory;
    public LocalDateTime filename;
    public String where;
    public String data;
    public LocalDateTime now = LocalDateTime.now();

    public DefaultFileSaver(String data, String where, LocalDateTime now, LocalDateTime name, String DataLakeDirectory) {
        this.DataLakeDirectory = DataLakeDirectory;
        this.filename = name;
        this.where = where;
        this.data = data;
        this.now = now;
    }

    @Override
    public String createFileName() {
        return this.filename.format(FILE_NAME_FORMAT) + ".txt";
    }

    @Override
    public String createPathToFile() {
        return DataLakeDirectory + File.separator + this.where + File.separator + createFileName();
    }

    @Override
    public File createFile() {
        return new File(createPathToFile());
    }


    @Override
    public void writeToFile() throws IOException {
        if(createFile().exists()) {
            FileWriter writer = new FileWriter(createFile(), true);
            writer.write("\nData saved: "+this.now.format(DateTimeFormatter.ofPattern("dd-mm-yyyy hh:mm:ss")) + "\n");
            writer.write("\n\n------------------------------------------\n\n");
            writer.write(this.data);
            writer.write("\n------------------------------------------\n\n");
            System.out.println("\nWriting into: " + createFileName() + "\n");
        }
    }
}
