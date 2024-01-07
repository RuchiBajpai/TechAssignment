package com.ruchi;

import com.ruchi.api.HeroApi;
import com.ruchi.model.HeroWithVouchers;
import com.ruchi.model.Voucher;
import com.ruchi.utils.NumberUtil;
import com.ruchi.utils.StringUtil;
import io.qameta.allure.Allure;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserStory6Tests {
    private HeroApi heroApi = new HeroApi();
    @Test
    @DisplayName("US6:AC1 : Get Vouchers by person and type")
    public void createHeroTest_AC1_With_Vouchers_happy_scenario(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);
        createHeroWithVoucher(randonNatId, randonName);


        Response response = heroApi.getVouchersByPersonAndType()
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPath = new JsonPath(response.asString());

        List<Map<String, ?>> vouchersDataList = jsonPath.getList("data");

        Optional<Map<String, ?>> firstElement = vouchersDataList.stream()
                .filter(map -> randonName.equals(map.get("name")))
                .findFirst();

        Assertions.assertTrue(firstElement.isPresent());
        Allure.addAttachment("Voucher found", firstElement.get().toString());
        Assertions.assertEquals("TRAVEL", firstElement.get().get("voucherType"));
        Assertions.assertEquals(1, firstElement.get().get("count"));
    }

    private void createHeroWithVoucher(String randonNatId, String randonName){
        Voucher voucher = Voucher.builder()
                .voucherName("VOUCHER 1")
                .voucherType("TRAVEL")
                .build();

        HeroWithVouchers heroWithVouchers = HeroWithVouchers.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("FEMALE")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(10)
                .salary(2000)
                .taxPaid(100)
                .vouchers(Arrays.asList(voucher))
                .build();

        heroApi.createHeroWithVouchers(heroWithVouchers)
                .then()
                .statusCode(200);
    }
}
