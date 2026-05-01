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
        // Pro každý rok spočítám průměrnou délku kvalifikačních prací zvlášť.
        var result = new ThesisDurationStats[years.length];

        for (int i = 0; i < years.length; i++) {
            var year = years[i];
            // Načtu práce za konkrétní rok a katedru.
            var json = dataSource.getKvalifikacniPrace(year, katedra);
            var data = new Gson().fromJson(json, KvalifikacniPraceList.class);
            long totalDays = 0;
            long count = 0;

            if (data != null && data.items != null) {
                for (var item : data.items) {
                    // Beru jen úplné a validně zapsané datumy, jinak záznam přeskočím.
                    if (item == null || item.datumZadani == null || item.datumOdevzdani == null) {
                        continue;
                    }
                    if (!item.datumZadani.isValid() || !item.datumOdevzdani.isValid()) {
                        continue;
                    }

                    // Rozdíl počítám v dnech přes ChronoUnit, aby výsledek seděl na testy.
                    var start = item.datumZadani.toLocalDate();
                    var end = item.datumOdevzdani.toLocalDate();
                    if (start != null && end != null) {
                        totalDays += ChronoUnit.DAYS.between(start, end);
                        count++;
                    }
                }
            }

            // Průměr zaokrouhlím stejně jako v zadání a testech.
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
