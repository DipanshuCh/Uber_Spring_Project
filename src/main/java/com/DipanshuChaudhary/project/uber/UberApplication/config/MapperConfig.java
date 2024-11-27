package com.DipanshuChaudhary.project.uber.UberApplication.config;

import com.DipanshuChaudhary.project.uber.UberApplication.dto.PointDto;
import com.DipanshuChaudhary.project.uber.UberApplication.utils.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();

        // PointDTO to point class
        mapper.typeMap(PointDto.class, Point.class).setConverter(context -> {
            PointDto pointDto = context.getSource();

            return GeometryUtil.createPoint(pointDto);
        });

        // Point to PointDTO
        mapper.typeMap(Point.class, PointDto.class).setConverter(context -> {

            Point point = context.getSource();
            double[] coordinates = {
                    point.getX(),
                    point.getY()
            };

            return new PointDto(coordinates);
        });



//        mapper.typeMap(Rider.class, RiderDto.class).addMappings(mapp -> {
//            mapp.map(Rider::getUser, RiderDto::setUser); // Maps Rider's User to UserDto
//        });

//        mapper.typeMap(Rider.class, RiderDto.class).addMappings(mapp -> {
//            mapp.map(Rider::getUser, RiderDto::setUserr); // Maps Rider's User to UserDto
//        });


        return mapper;
    }

}








