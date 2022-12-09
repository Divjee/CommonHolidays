package com.example.commonholidays;

import com.example.commonholidays.model.CommonHolidays;
import com.example.commonholidays.model.Holiday;
import com.example.commonholidays.rest.HolidayRestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class HolidayRestClientTest {

    @Spy
    HolidayRestClient holidayRestClient;
    private final LocalDate dateOne = LocalDate.of(2021, 11, 18);
    private final LocalDate dateTwo = LocalDate.of(2021, 1, 1);
    private final LocalDate dateThree = LocalDate.of(2021, 1, 18);
    private final String localNameOne = "Jaunais Gads";
    private final String localNameTwo = "Latvijas Republikas proklamēšanas diena";
    private final String getLocalNameThree = "New Year's Day";
    private final String getLocalNameFour = "Martin Luther King, Jr. Day";
    private final String nameOne = "New Year's Day";
    private final String nameTwo = "Proclamation Day of the Republic of Latvia";
    private final String nameThree = "Martin Luther King, Jr. Day";

    private List<Holiday> LVHolidays() {
        Holiday holidayOne = new Holiday(dateTwo, localNameOne, nameOne);
        Holiday holidaysTwo = new Holiday(dateOne, localNameTwo, nameTwo);

        return List.of(holidayOne, holidaysTwo);
    }

    private List<Holiday> USAHolidays() {
        Holiday holidayOne = new Holiday(dateTwo, getLocalNameThree, nameOne);
        Holiday holidaysTwo = new Holiday(dateThree, getLocalNameFour, nameThree);


        return List.of(holidayOne, holidaysTwo);
    }

    @Test
    void shouldGetCommonHolidays() {
        List<Holiday> holidaysTestListLv = LVHolidays();
        List<Holiday> holidaysTestListUs = USAHolidays();

        Mockito.doAnswer(i -> holidaysTestListLv)
                .when(holidayRestClient)
                .retrieveHolidays("2021", "LV");

        Mockito.doAnswer(i -> holidaysTestListUs)
                .when(holidayRestClient)
                .retrieveHolidays("2021", "US");

        List<CommonHolidays> result = holidayRestClient.commonHolidays("2021", "LV", "US");

        List<CommonHolidays> expected = List.of(new CommonHolidays(dateTwo, getLocalNameThree, localNameOne));

        Assertions.assertEquals(expected, result);

    }

    @Test
    public void runtimeErrorShouldBeThrownWhenExternalApiHasError() {

        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(holidayRestClient)
                .retrieveHolidays(Mockito.any(String.class), Mockito.any(String.class));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> holidayRestClient.commonHolidays("2021", "LVU", "US"));
        Assertions.assertEquals(HttpStatusCode.valueOf(404), exception.getStatusCode());
    }
}