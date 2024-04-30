package com.aytronn.kibvet.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Food {
    @Id
    @UuidGenerator
    private UUID id;

    //%
    private double humidity;
    private double protein;
    private double grossFatContent;
    private double starch;
    private double ena;
    private double totalFiber;
    private double crudeCellulose;
    private double crudeAsh;
    private double arginine;
    private double taurine;
    private double epaDha;
    private double calcium;
    private double phosphorus;
    private double magnesium;
    private double sodium;
    private double potassium;
    private double chlorine;

    //mg*
    private double iron;
    private double copper;
    private double zinc;

    //mg
    private double selenium;

    //UI
    private double vitaminA;
    private double vitaminD3;

    //mg
    private double vitaminE;
    private double vitaminC;
    private double vitaminB1;
    private double vitaminB2;
    private double vitaminB6;
    private double niacin;
    private double pantothenicAcid;
    private double vitaminB12;
    private double folicAcid;
    private double biotin;
    private double choline;
    private double lCarnitine;
    private double chondroitinGlucosamine;
    private double polyphenols;

    //kcal/kg
    private double metabolizableEnergy;

}