package com.github.hybusa.taskMSocial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;



@Data
public class DailyDomainDto {

    @JsonProperty("domainname")
    private String domainName;

    private int hotness;

    private int price;

    @JsonProperty("x_value")
    private int xValue;

    @JsonProperty("yandex_tic")
    private int yandexTic;

    private int links;

    private int visitors;

    private String registrar;

    private int old;

    @JsonProperty("delete_date")
    private Date deleteDate;

    private boolean rkn;

    private boolean judicial;

    private boolean block;
}
