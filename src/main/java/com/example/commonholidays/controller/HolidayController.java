package com.example.commonholidays.controller;

import com.example.commonholidays.service.HolidayRestClient;
import com.example.commonholidays.model.CommonHolidays;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("myperfectapp")
public class HolidayController {

    private final HolidayRestClient holidayRestClient;

    public HolidayController(HolidayRestClient holidayRestClient) {
        this.holidayRestClient = holidayRestClient;
    }

    @GetMapping("{year}/{firstCountry}/{secondCountry}")
    public List<CommonHolidays> getCommonHolidays(@PathVariable String year, @PathVariable String firstCountry, @PathVariable String secondCountry) {
        return holidayRestClient.commonHolidays(year, firstCountry, secondCountry);
    }
}
