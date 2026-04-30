package pro1.reports.report1;

import com.google.gson.Gson;
import pro1.DataSource;
import pro1.apiDataModel.BooksList;
import pro1.apiDataModel.TeacherCoursesList;
import pro1.reports.report1.reportDataModel.CourseBook;

import java.util.ArrayList;
import java.util.List;

public class TeacherBooksReporting {

    public static List<CourseBook> GetReport(DataSource dataSource, String rok, int ucitIdno, String katedra){
        var coursesJson = dataSource.getPredmetyByUcitel(rok, ucitIdno, katedra);
        var courses = new Gson().fromJson(coursesJson, TeacherCoursesList.class);

        var reportItems = new ArrayList<CourseBook>();

        if (courses != null && courses.items != null) {
            for (var course : courses.items) {
                if (course == null || course.code == null) {
                    continue;
                }
                var booksJson = dataSource.getLiteraturaPredmetu(course.code, katedra);
                var books = new Gson().fromJson(booksJson, BooksList.class);
                if (books == null || books.items == null) {
                    continue;
                }
                for (var book : books.items) {
                    if (book == null || book.title == null) {
                        continue;
                    }
                    reportItems.add(new CourseBook(book.title, book.author, course.code));
                }
            }
        }

        return reportItems;
    }
}
