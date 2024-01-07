package com.ruchi.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hero {
    private String natid;
    private String name;
    private String gender;
    private String birthDate;
    private String deathDate;
    private Integer browniePoints;
    private double salary;
    private double taxPaid;


    public Hero(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Hero hero = objectMapper.readValue(jsonString, Hero.class);
        System.out.println(hero);
    }
}

