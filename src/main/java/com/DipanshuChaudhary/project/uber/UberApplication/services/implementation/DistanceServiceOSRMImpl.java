/*
package com.DipanshuChaudhary.project.uber.UberApplication.services.implementation;

import com.DipanshuChaudhary.project.uber.UberApplication.services.DistanceService;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {

    private static final String OSRM_API_BASE_URL = "https://router.project-osrm.org/route/v1/driving/13.388860,52.517037;13.397634,52.529407";

    @Override
    public double calculateDistance(Point src, Point dest) {
        try {
            String uri = src.getX()+","+src.getY()+";"+dest.getX()+","+dest.getY();
            OSRMResponseDto responseDto = RestClient.builder()
                    .baseUrl(OSRM_API_BASE_URL)
                    .build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(OSRMResponseDto.class);

            return responseDto.getRoutes().get(0).getDistance() / 1000.0;
        } catch (Exception e) {
            throw new RuntimeException("Error getting data from OSRM "+e.getMessage());
        }
    }
}


@Data
class OSRMResponseDto {
    List<OSRMRoute> routes;
}

@Data
class OSRMRoute {
    private double distance;
}


*/


















package com.DipanshuChaudhary.project.uber.UberApplication.services.implementation;


import com.DipanshuChaudhary.project.uber.UberApplication.services.DistanceService;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {

    private static final String OSRM_API_BASE_URL = "https://router.project-osrm.org/route/v1/driving";

    @Override
    public double calculateDistance(Point src, Point dest) {
        try {
            // Construct the URI by inserting the coordinates correctly
            String uri = OSRM_API_BASE_URL + "/" + src.getX() + "," + src.getY() + ";" + dest.getX() + "," + dest.getY() ;

            // Log the URL for debugging purposes
            System.out.println("OSRM API Request URL: " + uri);

            // Make the 3rd party API call using RestClient
            OSRMResponseDto responseDto = RestClient.builder()
                    .baseUrl(OSRM_API_BASE_URL)  // Base URL only
                    .build()
                    .get()
                    .uri(uri)  // Pass the constructed URI here
                    .retrieve()
                    .body(OSRMResponseDto.class);

            // Return the distance in kilometers
            return responseDto.getRoutes().get(0).getDistance() / 1000.0;
        } catch (Exception e) {
            // Handle the exception and return a meaningful error message
            throw new RuntimeException("Error getting data from OSRM: " + e.getMessage());
        }
    }
}

@Data
class OSRMResponseDto {
    List<OSRMRoute> routes;
}

@Data
class OSRMRoute {
    private double distance;
}

