package com.example.commonholidays.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Holiday {
    private LocalDate date;
    private String localName;
    private String name;
}
