package com.example.commonholidays;

import com.example.commonholidays.model.CommonHolidays;
import com.example.commonholidays.model.Holiday;
import com.example.commonholidays.service.HolidayRestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommonHolidaysApplicationTests {
    WebClient.Builder builder = WebClient.builder();
    HolidayRestClient holidayRestClient = new HolidayRestClient(builder);

    @Test
    void retrieveAllHolidays() {
        String year = "2022";
        String countryId = "LV";
        List<Holiday> holidayList = holidayRestClient.retrieveHolidays(year, countryId);
        assertTrue(holidayList.size() > 0);
    }

    @Test
    void retrieveAllHolidays_notFound() {
        String year = "2022";
        String countryId = "QWN";
        Assertions.assertThrows(WebClientException.class, () -> holidayRestClient.retrieveHolidays(year, countryId));
    }

    @Test
    void checkIfHolidaysMatchTest() {
        Holiday holiday1 = new Holiday(LocalDate.of(2020, 4, 12), "Lieldienas", "Easter");
        Holiday holiday2 = new Holiday(LocalDate.of(2021, 12, 25), "Christmas-Eve", "Christmas");
        Holiday holiday3 = new Holiday(LocalDate.of(2021, 12, 25), "Christmas-Eve", "Christmas");
        assertFalse(holidayRestClient.checkIfHolidaysMatch(holiday1, holiday2));
        assertTrue(holidayRestClient.checkIfHolidaysMatch(holiday2, holiday3));
    }

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
