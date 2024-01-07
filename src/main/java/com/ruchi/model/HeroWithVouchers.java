package com.ruchi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeroWithVouchers{
    private String natid;
    private String name;
    private String gender;
    private String birthDate;
    private String deathDate;
    private Integer browniePoints;
    private double salary;
    private double taxPaid;
    private List<Voucher> vouchers;
}
