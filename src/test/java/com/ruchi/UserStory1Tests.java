package com.ruchi;

import com.ruchi.api.HeroApi;
import com.ruchi.model.Hero;
import com.ruchi.utils.DBUtil;
import com.ruchi.utils.NumberUtil;
import com.ruchi.utils.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.Matchers.*;


public class UserStory1Tests {
    private HeroApi heroApi = new HeroApi();
    @Test
    @DisplayName("US1:AC1 : Creation of a single working class hero via API call")
    public void createHeroTest_AC1_HappyScenario(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);
        Hero hero = Hero.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("FEMALE")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(10)
                .salary(2000)
                .taxPaid(100)
                .build();

        heroApi.createHero(hero)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("US1:AC2 : Verify Invalid Nat ID")
    public void createHeroTest_AC2_invalid_natId(){
        String randonNatId = "natid-" + 99999999;
        String randonName = StringUtil.generateRandomString(6, 12);
        Hero hero = Hero.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("FEMALE")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(10)
                .salary(2000)
                .taxPaid(100)
                .build();

        heroApi.createHero(hero)
                .then()
                .statusCode(400)
                .body("errorMsg", equalTo("Invalid natid"));
    }

    @Test
    @DisplayName("US1:AC2 : Verify Invalid Name")
    public void createHeroTest_AC2_invalid_name(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = "";
        Hero hero = Hero.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("FEMALE")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(10)
                .salary(2000)
                .taxPaid(100)
                .build();

        heroApi.createHero(hero)
                .then()
                .statusCode(400)
                .body("errorMsg", containsString("Name must be between 1 and 100 characters"))
                .body("errorMsg", containsString("Name cannot be blank"))
                .body("errorMsg", containsString("Invalid name"));
    }

    @Test
    @DisplayName("US1:AC2 : Verify Invalid Gender")
    public void createHeroTest_AC2_invalid_gender(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);
        Hero hero = Hero.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("ABCD")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(10)
                .salary(2000)
                .taxPaid(100)
                .build();

        heroApi.createHero(hero)
                .then()
                .statusCode(400)
                .body("errorMsg", equalTo("Invalid gender"));
    }

    @Test
    @DisplayName("US1:AC2 : Verify Invalid Salary")
    public void createHeroTest_AC2_invalid_salary(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);
        Hero hero = Hero.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("MALE")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(10)
                .salary(-10)
                .taxPaid(100)
                .build();

        heroApi.createHero(hero)
                .then()
                .statusCode(400)
                .body("errorMsg", equalTo("Salary must be greater than or equals to zero"));
    }

    @Test
    @DisplayName("US1:AC2 : Verify Invalid TaxPaid")
    public void createHeroTest_AC2_invalid_taxpaid(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);
        Hero hero = Hero.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("MALE")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(10)
                .salary(2000)
                .taxPaid(-20)
                .build();

        heroApi.createHero(hero)
                .then()
                .statusCode(400)
                .body("errorMsg", equalTo("must be greater than or equal to 0"));
    }

    @Test
    @DisplayName("US1:AC2 : browniePoints and deathDate are nullable")
    public void createHeroTest_AC2_nullable_browniepoints_and_deathdate(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);
        Hero hero = Hero.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("MALE")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(null)
                .salary(2000)
                .taxPaid(200)
                .build();

        heroApi.createHero(hero)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("US1:AC3 : NatId already exist")
    public void createHeroTest_AC3_NatId_already_exist(){
        String randonNatId = "natid-" + 1;
        String randonName = StringUtil.generateRandomString(6, 12);
        Hero hero = Hero.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("MALE")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(null)
                .salary(2000)
                .taxPaid(200)
                .build();

        heroApi.createHero(hero)
                .then()
                .statusCode(400)
                .body("errorMsg", equalTo("Working Class Hero of natid: natid-1 already exists!"));
    }

    @Test
    @DisplayName("US1:AC4 : Verify hero record created in DB WORKING_CLASS_HEROES table")
    public void createHeroTest_AC4_Record_created_in_db(){
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);
        Hero hero = Hero.builder()
                .natid(randonNatId)
                .name(randonName)
                .gender("MALE")
                .birthDate("2020-01-01")
                .deathDate(null)
                .browniePoints(null)
                .salary(2000)
                .taxPaid(200)
                .build();

        heroApi.createHero(hero)
                .then()
                .statusCode(200);

        String sqlQuery = "SELECT * FROM `working_class_heroes` where natid='" + randonNatId + "';";
        ResultSet rs = DBUtil.getInstance().executeQuery(sqlQuery);

        try {
            if (rs != null && rs.next()) {
                // At least one record exists
                String name = rs.getString("name");
                int salary = rs.getInt("salary");
                Assertions.assertEquals(randonName, name);
                Assertions.assertEquals(2000, salary);
            } else {
                Assertions.fail("No record found in the DB");
            }
        } catch (SQLException e) {
            Assertions.fail("Error while connecting to the DB : " + e.getMessage());
        }
    }
}
