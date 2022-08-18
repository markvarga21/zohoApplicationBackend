package io.tis.zoho.timelog;

import org.springframework.stereotype.Component;

@Component
public class DateTimeConverter {
    public String convertDatePickerFormatToStandard(String datePickerDate) {
        String[] date = datePickerDate.split("/");
        String month = date[0];
        String day = date[1];
        String year = date[2];
        return String.format("%s-%s-%s", year, month, day);
    }
    public String convertTimePickerFormatToStandard(String timePickerTime) {
        return timePickerTime.replaceAll(" ", "");
    }
}
