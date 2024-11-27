package com.DipanshuChaudhary.project.uber.UberApplication.utils;

import com.DipanshuChaudhary.project.uber.UberApplication.dto.PointDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {


    // this method used to convert the coordinate to Point
    public static Point createPoint(PointDto pointDto){
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(),4326);
        Coordinate coordinate =  new Coordinate(pointDto.getCoordinates()[0],
                pointDto.getCoordinates()[1]);

        return geometryFactory.createPoint(coordinate);
    }

}
