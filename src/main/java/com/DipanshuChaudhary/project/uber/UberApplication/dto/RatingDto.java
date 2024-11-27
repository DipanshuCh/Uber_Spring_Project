package com.DipanshuChaudhary.project.uber.UberApplication.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {

    private Long RideId;
    private Integer rating;

}
