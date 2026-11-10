package com.dymun.pricealert.external.itad;

import com.dymun.pricealert.external.itad.dto.ItadLookupResponse;
import com.dymun.pricealert.external.itad.dto.ItadPriceResult;
import com.dymun.pricealert.external.itad.dto.ItadSearchResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ItadClientTest {

    @Autowired
    private ItadClient itadClient;

    @Test
    void lookupTest() {
        ItadLookupResponse response = itadClient.lookup(413150);
        System.out.println(response.getGame().getTitle());
    }

    @Test
    void searchTest() {
        List<ItadSearchResult> result = itadClient.search("stard");
        for (ItadSearchResult r : result) {
            System.out.println(r.getId());
        }
    }

    @Test
    void pricesTest() {
        List<String> gameIds = List.of("018d937e-f835-710c-b95b-928e277b187e", "018d937f-33fa-722b-9933-86fb37dfb136");
        List<ItadPriceResult> result = itadClient.prices(gameIds);

        for (ItadPriceResult r : result) {
            System.out.println(r.getHistoryLow().getAll().getAmountInt());
        }
    }
}
