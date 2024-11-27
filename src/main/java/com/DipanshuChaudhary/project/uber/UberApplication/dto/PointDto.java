package com.DipanshuChaudhary.project.uber.UberApplication.dto;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
//@RequiredArgsConstructor
public class PointDto {

    private double[] coordinates;
    private String type = "Point";

//    public double[] getCoordinates() {
//        return coordinates;
//    }

    public PointDto(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
