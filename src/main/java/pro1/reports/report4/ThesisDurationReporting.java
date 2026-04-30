package pro1.reports.report4;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import pro1.DataSource;
import pro1.apiDataModel.Date;
import pro1.reports.report4.reportDataModel.ThesisDurationStats;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class ThesisDurationReporting {
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
                    if (item == null || item.datumZadani == null || item.datumOdevzdani == null) {
                        continue;
                    }
                    if (!item.datumZadani.isValid() || !item.datumOdevzdani.isValid()) {
                        continue;
                    }
                    var start = item.datumZadani.toLocalDate();
                    var end = item.datumOdevzdani.toLocalDate();
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

    private static class KvalifikacniPraceList {
        @SerializedName("kvalifikacniPrace")
        List<KvalifikacniPrace> items;
    }

    private static class KvalifikacniPrace {
        Date datumZadani;
        Date datumOdevzdani;
    }
}
