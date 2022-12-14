package com.example.commonholidays.model;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@AllArgsConstructor
@Data
public class CommonHolidays {
    private LocalDate date;
    private String firstHolidayLocalName;
    private String secondHolidayLocalName;
}
