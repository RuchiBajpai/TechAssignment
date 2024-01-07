package com.ruchi.api;

import com.ruchi.model.Hero;
import com.ruchi.model.HeroWithVouchers;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class HeroApi extends BaseApi{
    private final String OWE_MONEY_PATH = "hero/owe-money";
    private final String CREATE_HERO_PATH = "hero";
    private final String CREATE_HERO_WITH_VOUCHERS_PATH = "hero/vouchers";
    private final String VOUCHER_BY_PERSON_AND_TYPE_PATH = "voucher/by-person-and-type";

    @Step("get owed money for a hero")
    public Response getOwedMoney(Object natId){
        return requestSender.queryParam("natid",natId)
                .get(OWE_MONEY_PATH);
    }

    @Step("Create a working class hero")
    public Response createHero(Hero payload){
        return requestSender.body(payload)
                .post(CREATE_HERO_PATH);
    }

    @Step("Create a working class hero with vouchers")
    public Response createHeroWithVouchers(HeroWithVouchers payload){
        return requestSender.body(payload)
                .post(CREATE_HERO_WITH_VOUCHERS_PATH);
    }

    @Step("get vouchers by person and type")
    public Response getVouchersByPersonAndType(){
        return requestSender
                .get(VOUCHER_BY_PERSON_AND_TYPE_PATH);
    }
}
