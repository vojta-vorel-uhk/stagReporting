package pro1.reports.report4;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import pro1.DataSource;
import pro1.reports.report4.reportDataModel.ThesisDurationStats;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ThesisDurationReporting {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d.M.yyyy");

    public static Object[] GetReport(DataSource dataSource, String katedra, String[] years) {
        var result = new ThesisDurationStats[years.length];

        for (int i = 0; i < years.length; i++) {
            var year = years[i];
            var json = dataSource.getKvalifikacniPrace(year, katedra);
            var data = new Gson().fromJson(json, KvalifikacniPraceList.class);
            long totalDays = 0;
            long count = 0;

            if (data != null && data.items != null) {
                for (var item : data.items) {
                    var start = parseDateValue(item != null ? item.datumZadani : null);
                    var end = parseDateValue(item != null ? item.datumOdevzdani : null);
                    if (start != null && end != null) {
                        totalDays += ChronoUnit.DAYS.between(start, end);
                        count++;
                    }
                }
            }

            long average = count == 0 ? 0 : Math.round((double) totalDays / count);
            result[i] = new ThesisDurationStats(year, average);
        }

        return result;
    }

    private static LocalDate parseDateValue(DateValue value) {
        if (value == null || value.value == null || value.value.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(value.value, DATE_FORMAT);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private static class KvalifikacniPraceList {
        @SerializedName("kvalifikacniPrace")
        List<KvalifikacniPrace> items;
    }

    private static class KvalifikacniPrace {
        DateValue datumZadani;
        DateValue datumOdevzdani;
    }

    private static class DateValue {
        String value;
    }
}
