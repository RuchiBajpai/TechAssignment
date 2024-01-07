package com.ruchi;

import com.ruchi.api.HeroApi;
import com.ruchi.model.HeroWithVouchers;
import com.ruchi.model.Voucher;
import com.ruchi.utils.DBUtil;
import com.ruchi.utils.NumberUtil;
import com.ruchi.utils.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;

public class UserStory4Tests {

    private HeroApi heroApi = new HeroApi();
    @Test
    @DisplayName("US4:AC1 : Creation of a working class hero with vouchers")
    public void createHeroTest_AC1_With_Vouchers_happy_scenario(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);

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

    @Test
    @DisplayName("US4:AC3 : Hero cannot be created if the vouchers is null ")
    public void createHeroTest_AC3_With_Vouchers_not_possible_with_null_vouchers(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);

        HeroWithVouchers heroWithVouchers = HeroWithVouchers.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("FEMALE")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(10)
                .salary(2000)
                .taxPaid(100)
                .vouchers(null)
                .build();

        heroApi.createHeroWithVouchers(heroWithVouchers)
                .then()
                .statusCode(400)
                .body("errorMsg", equalTo("vouchers cannot be null or empty"));
    }

    @Test
    @DisplayName("US4:AC3 : Hero cannot be created if the vouchers is empty ")
    public void createHeroTest_AC3_With_Vouchers_not_possible_with_empty_vouchers(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);

        HeroWithVouchers heroWithVouchers = HeroWithVouchers.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("FEMALE")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(10)
                .salary(2000)
                .taxPaid(100)
                .vouchers(new ArrayList<>())
                .build();

        heroApi.createHeroWithVouchers(heroWithVouchers)
                .then()
                .statusCode(400)
                .body("errorMsg", equalTo("vouchers cannot be null or empty"));
    }

    @Test
    @DisplayName("US4:AC4 : Vouchers are getting created in the DB table VOUCHERS")
    public void createHeroTest_AC4_With_Vouchers_happy_scenario(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);

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

        String heroId = getWorkingHeroIDFromDB(randonNatId);

        String sqlQueryForVouchers = "SELECT * FROM `vouchers` where working_class_hero_id='" + heroId + "';";
        ResultSet rs = DBUtil.getInstance().executeQuery(sqlQueryForVouchers);

        try {
            if (rs != null && rs.next()) {
                // At least one record exists
                String name = rs.getString("name");
                String voucher_type = rs.getString("voucher_type");
                Assertions.assertEquals("VOUCHER 1", name);
                Assertions.assertEquals("TRAVEL", voucher_type);
            } else {
                Assertions.fail("No record found in the DB in the VOUCHERS table");
            }
        } catch (SQLException e) {
            Assertions.fail("Error while connecting to the DB : " + e.getMessage());
        }
    }

    @Test
    @DisplayName("US4:AC5 : If any validation fails like wrong Gender, nothing is persisted for both hero and vouchers")
    public void createHeroTest_AC5_With_Vouchers_nothing_get_persisted_in_case_of_errors(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);

        Voucher voucher = Voucher.builder()
                .voucherName("VOUCHER 1")
                .voucherType("TRAVEL")
                .build();

        HeroWithVouchers heroWithVouchers = HeroWithVouchers.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("ABCD")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(10)
                .salary(2000)
                .taxPaid(100)
                .vouchers(Arrays.asList(voucher))
                .build();

        heroApi.createHeroWithVouchers(heroWithVouchers)
                .then()
                .statusCode(400);

        String sqlQueryForHero = "SELECT * FROM `working_class_heroes` where natid='" + randonNatId + "';";
        ResultSet rs = DBUtil.getInstance().executeQuery(sqlQueryForHero);
        String id = null;
        try {
            if (rs != null && rs.next()) {
                Assertions.fail("Records are still getting persisted in hero table even if the validation failed.");
            }
        } catch (SQLException e) {
            Assertions.fail("Error while connecting to the DB : " + e.getMessage());
        }
    }

    private String getWorkingHeroIDFromDB(String natId){
        String sqlQueryForHero = "SELECT * FROM `working_class_heroes` where natid='" + natId + "';";
        ResultSet rs = DBUtil.getInstance().executeQuery(sqlQueryForHero);
        String id = null;
        try {
            if (rs != null && rs.next()) {
                id = rs.getString("id");
            } else {
                Assertions.fail("No record found in the DB for the working hero");
            }
        } catch (SQLException e) {
            Assertions.fail("Error while connecting to the DB : " + e.getMessage());
        }finally {
            return id;
        }
    }
}
