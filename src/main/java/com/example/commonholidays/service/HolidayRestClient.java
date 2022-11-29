package com.example.commonholidays.service;

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

    private final WebClient.Builder builder;

    public HolidayRestClient(WebClient.Builder builder) {
        this.builder = builder;
    }

    public WebClient.Builder getWebClient() {
        return builder;
    }

    public List<Holiday> retrieveHolidays(String year, String countryCode) {
        try {
            return getWebClient().build().get().uri("https://date.nager.at/api/v3/PublicHolidays/" + year + "/" + countryCode + "")
                    .retrieve()
                    .bodyToFlux(Holiday.class)
                    .collectList()
                    .block();
        } catch (WebClientException ex) {
            log.error(" the response body is {}", ex.getMessage());
            log.error("WebClientResponseException in retrieveHolidays", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Exception in retrieveHolidays", ex);
            throw ex;
        }
    }

    public boolean checkIfHolidaysMatch(Holiday h1, Holiday h2) {
        return h1.getDate().equals(h2.getDate()) && h1.getName().equals(h2.getName());
    }

    public List<CommonHolidays> getCommonHolidays(List<Holiday> h1, List<Holiday> h2) {
        List<CommonHolidays> commonHolidays = new ArrayList<>();
        for (Holiday i : h1) {
            for (Holiday j : h2) {
                if (checkIfHolidaysMatch(i, j)) {
                    commonHolidays.add(new CommonHolidays(j.getDate(), j.getLocalName(), i.getLocalName()));
                }
            }
        }
        return commonHolidays;
    }

    public List<CommonHolidays> commonHolidays(String year, String countryCode1, String countryCode2) {
        List<Holiday> holidays1 = retrieveHolidays(year, countryCode1);
        List<Holiday> holidays2 = retrieveHolidays(year, countryCode2);

        return getCommonHolidays(holidays1, holidays2);
    }
}
