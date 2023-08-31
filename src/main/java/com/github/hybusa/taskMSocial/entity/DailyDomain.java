package com.github.hybusa.taskMSocial.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name ="daily_domains")
public class DailyDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String domainName;

    private int hotness;

    private int price;

    private int xValue;

    private int yandexTic;

    private int links;

    private int visitors;

    private String registrar;

    private int old;

    private Date deleteDate;

    private boolean rkn;

    private boolean judicial;

    private boolean block;
}