package com.example.commonholidays.model;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@AllArgsConstructor
@Data
public class CommonHolidays {
    private LocalDate date;
    private String commonHoliday1LocalName;
    private String commonHoliday2LocalName;
}
