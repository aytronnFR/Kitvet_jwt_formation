package com.aytronn.kibvet.dto;

public record FoodCreateDto(
        //in %
        double humidity,
        double protein,
        double grossFatContent,
        double starch,
        double ena,
        double totalFiber,
        double crudeCellulose,
        double crudeAsh,
        double arginine,
        double taurine,
        double epaDha,
        double calcium,
        double phosphorus,
        double magnesium,
        double sodium,
        double potassium,
        double chlorine,

        //in mg*
        double iron,
        double copper,
        double zinc,

        //in mg
        double selenium,

        //in UI
        double vitaminA,
        double vitaminD3,

        //in mg
        double vitaminE,
        double vitaminC,
        double vitaminB1,
        double vitaminB2,
        double vitaminB6,
        double niacin,
        double pantothenicAcid,
        double vitaminB12,
        double folicAcid,
        double biotin,
        double choline,
        double lCarnitine,
        double chondroitinGlucosamine,
        double polyphenols,

        //in kcal/kg
        double metabolizableEnergy
) {
}
