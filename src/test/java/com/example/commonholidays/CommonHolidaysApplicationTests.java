package com.example.commonholidays;

import com.example.commonholidays.model.CommonHolidays;
import com.example.commonholidays.model.Holiday;
import com.example.commonholidays.rest.HolidayRestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommonHolidaysApplicationTests {
    @Autowired
    private HolidayRestClient holidayRestClient;

    @Test
    void getCommonHolidaysTest() {
        List<CommonHolidays> commonHolidays = new ArrayList<>();
        Holiday holiday2 = new Holiday(LocalDate.of(2021, 12, 25), "Christmas-Eve", "Christmas");
        Holiday holiday3 = new Holiday(LocalDate.of(2021, 12, 25), "Christmas-Eve", "Christmas");
        commonHolidays.add(new CommonHolidays(holiday2.getDate(), holiday2.getLocalName(), holiday3.getLocalName()));
        List<Holiday> holiday2List = List.of(holiday2);
        List<Holiday> holiday3List = List.of(holiday3);

        assertEquals(commonHolidays, holidayRestClient.getCommonHolidays(holiday2List, holiday3List));
    }
}
