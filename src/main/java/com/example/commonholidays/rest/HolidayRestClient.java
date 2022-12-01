package com.example.commonholidays.rest;

import com.example.commonholidays.model.CommonHolidays;
import com.example.commonholidays.model.Holiday;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HolidayRestClient {
    private final WebClient webClient;

    public HolidayRestClient() {
       this.webClient =  WebClient.create();
    }

    public List<Holiday> retrieveHolidays(String year, String countryCode) {
        try {
            return webClient.get().uri("https://date.nager.at/api/v3/PublicHolidays/" + year + "/" + countryCode + "")
                    .retrieve()
                    .bodyToFlux(Holiday.class)
                    .collectList()
                    .block();
        } catch (WebClientException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public boolean checkIfHolidaysMatch(Holiday firstHoliday, Holiday secondHoliday) {
        return firstHoliday.getDate().equals(secondHoliday.getDate());
    }

    public List<CommonHolidays> getCommonHolidays(List<Holiday> firstHoliday, List<Holiday> secondHoliday) {
        List<CommonHolidays> commonHolidays = new ArrayList<>();
        for (Holiday i : firstHoliday) {
            for (Holiday j : secondHoliday) {
                if (checkIfHolidaysMatch(i, j)) {
                    commonHolidays.add(new CommonHolidays(j.getDate(), j.getLocalName(), i.getLocalName()));
                }
            }
        }
        return commonHolidays;
    }
    public List<CommonHolidays> commonHolidays(String year, String firstCountryCode, String secondCountryCode) {
        List<Holiday> holidayListOne = retrieveHolidays(year, firstCountryCode);
        List<Holiday> holidayListTwo = retrieveHolidays(year, secondCountryCode);

        return getCommonHolidays(holidayListOne, holidayListTwo);
    }
}
