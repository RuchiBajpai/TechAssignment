package com.ruchi.utils;

import io.qameta.allure.Allure;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class CsvUtil {
    public static String getSingleRecordCSV() throws IOException {
        String randonNatId = "natid-" + NumberUtil.getRandomNatId();
        String randonName = StringUtil.generateRandomString(6, 12);

        String data = randonNatId + "," + randonName + ",FEMALE,2010-01-06T15:30:45,2022-01-06T15:30:45.123456789,2000,200,9";
        Allure.addAttachment("CSV Content", data);
        String filePath = System.getProperty("user.dir") + "/docs/single-hero.csv";

        Files.write(Paths.get(filePath), data.getBytes(), StandardOpenOption.CREATE);

        return filePath;
    }

    public static Map<String, String> getMultiRecordCSVWithOneErroneousRecord() throws IOException {
        Map<String, String> returnMap = new HashMap<>();
        String randonNatId1 = "natid-" + NumberUtil.getRandomNatId();
        String randonName1 = StringUtil.generateRandomString(6, 12);

        String randonNatId2 = "natid-" + NumberUtil.getRandomNatId();
        String randonName2 = StringUtil.generateRandomString(6, 12);

        String data = randonNatId1 + "," + randonName1 + ",FEMALE,2010-01-06T15:30:45,2022-01-06T15:30:45.123456789,2000,200,9"
                + System.getProperty("line.separator")
                + randonNatId2 + "," + randonName2 + ",ABCD,2010-01-06T15:30:45,2022-01-06T15:30:45.123456789,2000,200,9";

        returnMap.put("ValidRecordId", randonNatId1);
        returnMap.put("ErroneousRecordId", randonNatId2);

        Allure.addAttachment("CSV Content", data);
        String filePath = System.getProperty("user.dir") + "/docs/multiple-hero.csv";

        Files.write(Paths.get(filePath), data.getBytes(), StandardOpenOption.CREATE);
        returnMap.put("FilePath", filePath);
        return returnMap;
    }
}
