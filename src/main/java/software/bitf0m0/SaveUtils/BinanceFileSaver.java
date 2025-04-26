package software.bitf0m0.SaveUtils;

import software.bitf0m0.Api.BinanceApi;
import org.json.JSONArray;
import software.bitf0m0.JSONUtils.JSONParse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BinanceFileSaver extends DefaultFileSaver implements DefaultFileSaverProvider {

    private static final String DATA_LAKE_PATH = "/home/d4rk/Desktop/BitF0M0-master/src/main/DataLake/DataLake/";
    private Map<String, StringBuilder> quarterlyData;

    public BinanceFileSaver(String data, String where, LocalDateTime now, LocalDateTime name) {
        super(data, where, now, name, DATA_LAKE_PATH);
        this.quarterlyData = new HashMap<>();
    }

    public boolean saveAllKlineData() {
        try {
            ArrayList<String> responses = BinanceApi.obtainFullResponse();
            for (String response : responses) {
                processKlineData(response);
            }
            saveQuarterlyFiles();
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar los datos de klines: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public void processKlineData(String jsonData) {
        try {
            JSONParse jsonParse = new JSONParse(jsonData);
            JSONArray klines = jsonParse.parseArray();
            for (int i = 0; i < klines.length(); i++) {
                JSONArray klineArray = klines.getJSONArray(i);
                long openTimeMillis = klineArray.getLong(0);
                Instant instant = Instant.ofEpochMilli(openTimeMillis);
                LocalDateTime klineDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

                int year = klineDateTime.getYear();
                int quarter = klineDateTime.get(IsoFields.QUARTER_OF_YEAR);
                String quarterKey = year + "Q" + quarter;
                if (!quarterlyData.containsKey(quarterKey)) {
                    quarterlyData.put(quarterKey, new StringBuilder());
                }
                quarterlyData.get(quarterKey).append(klineArray.toString()).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error al procesar los datos de klines: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void saveQuarterlyFiles() throws IOException {
        for (Map.Entry<String, StringBuilder> entry : quarterlyData.entrySet()) {
            String quarterKey = entry.getKey();
            String quarterData = entry.getValue().toString();

            int year = Integer.parseInt(quarterKey.substring(0, 4));
            int quarter = Integer.parseInt(quarterKey.substring(5));
            int month = (quarter - 1) * 3 + 1;
            LocalDateTime quarterStartDate = LocalDateTime.of(year, month, 1, 0, 0);
            BinanceFileSaver fileSaver = new BinanceFileSaver(
                    quarterData,
                    "binance2_klines/" + year,
                    LocalDateTime.now(),
                    quarterStartDate
            );

            File directory = new File(DATA_LAKE_PATH + fileSaver.where);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = fileSaver.createFile();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Data saved: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                writer.write("\n------------------------------------------\n");
                writer.write(quarterData);
                writer.write("\n------------------------------------------\n\n");
                System.out.println("\nWriting into: " + file.getName() + "\n");
            }
        }
    }

    @Override
    public String createFileName() {
        int quarter = this.filename.get(IsoFields.QUARTER_OF_YEAR);
        int year = this.filename.getYear();
        return "Q" + quarter + "_" + year + ".txt";
    }

    public static boolean saveKlinesToQuarterlyFiles() {
        BinanceFileSaver saver = new BinanceFileSaver("", "binance2_klines", LocalDateTime.now(), LocalDateTime.now());
        return saver.saveAllKlineData();
    }
}