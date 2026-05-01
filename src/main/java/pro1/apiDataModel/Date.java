package pro1.apiDataModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Date {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d.M.yyyy");

    public String value;

    public boolean isValid() {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(value, DATE_FORMAT);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    public LocalDate toLocalDate() {
        if (!isValid()) {
            return null;
        }
        return LocalDate.parse(value, DATE_FORMAT);
    }
}

