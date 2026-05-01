package pro1.reports.report3;

import com.google.gson.Gson;
import pro1.DataSource;
import pro1.apiDataModel.ActionsList;
import pro1.reports.report3.reportDataModel.DepartmentWeekdayStats;

public class DepartmentWeekdaysReporting {
    public static Object[] GetReport(DataSource dataSource, String rok, String katedra, String[] days) {
        // Načtu rozvrhové akce pro danou katedru a rok.
        var actionsJson = dataSource.getRozvrhByKatedra(rok, katedra);
        var actionsList = new Gson().fromJson(actionsJson, ActionsList.class);

        // Výsledek skládám přesně v pořadí, v jakém přišly dny v parametru days.
        var result = new DepartmentWeekdayStats[days.length];

        for (int i = 0; i < days.length; i++) {
            var day = days[i];
            long count = 0;

            // Každý den počítám zvlášť, aby se zachovalo pořadí i jednoduchá kontrola.
            if (actionsList != null && actionsList.items != null) {
                for (var action : actionsList.items) {
                    if (action != null && action.dayShort != null && action.dayShort.equals(day)) {
                        count++;
                    }
                }
            }
            result[i] = new DepartmentWeekdayStats(day, count);
        }

        return result;
    }
}

