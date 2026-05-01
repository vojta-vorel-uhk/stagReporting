package pro1.reports.report5;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import pro1.DataSource;
import pro1.reports.report5.reportDataModel.DepartmentExamsStats;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class DepartmentExamsStatsReporting {
    public static Object GetReport(DataSource dataSource, String katedra) {
        // Načtu termíny zkoušek pro danou katedru.
        var json = dataSource.getTerminyZkousek2(katedra);
        var termList = new Gson().fromJson(json, TermList.class);

        long realizedExamsCount = 0;
        // Učitele sbírám do množiny, aby se neopakovali v výsledném JSONu.
        var teacherIdsSet = new HashSet<Integer>();

        if (termList != null && termList.items != null) {
            for (var term : termList.items) {
                if (term == null) {
                    continue;
                }

                // Realizovanou zkoušku poznám podle kladného obsazení, které je v JSONu jako text.
                if (term.obsazeni != null) {
                    var parsed = parseIntOrNull(term.obsazeni);
                    if (parsed != null && parsed > 0) {
                        realizedExamsCount++;
                    }
                }

                // ID učitele přidám jen když opravdu existuje.
                if (term.ucitIdno != null) {
                    teacherIdsSet.add(term.ucitIdno);
                }
            }
        }

        // Množinu převedu na seřazený seznam, aby výstup byl stabilní pro testy.
        var teacherIds = new ArrayList<>(teacherIdsSet);
        teacherIds.sort(Comparator.naturalOrder());

        return new DepartmentExamsStats(realizedExamsCount, teacherIds);
    }

    private static Integer parseIntOrNull(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static class TermList {
        @SerializedName("termin")
        List<Term> items;
    }

    private static class Term {
        String obsazeni;
        Integer ucitIdno;
    }
}
