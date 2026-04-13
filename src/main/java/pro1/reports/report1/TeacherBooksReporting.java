package pro1.reports.report1;

import pro1.DataSource;
import pro1.reports.report1.reportDataModel.CourseBook;

import java.util.ArrayList;
import java.util.List;

public class TeacherBooksReporting {

    public static List<CourseBook> GetReport(DataSource dataSource, String rok, int ucitIdno, String katedra){
        var coursesJson = dataSource.getPredmetyByUcitel(rok, ucitIdno, katedra  );

        // TODO 1.1: Převeď coursesJson na objekt typu apiDataModel.TeacherCoursesList.
        // TODO 1.2: Doplň nutné atributy do třídy apiDataModel.TeacherCourse

        var reportItems = new ArrayList<CourseBook>();

        // TODO 1.3: Pro každý předmět získej z dataSource ještě seznam knih. Pro každou z nich přidej prvek do reportItems.

        return reportItems;
    }
}
