package com.DipanshuChaudhary.project.uber.UberApplication.services;

import com.DipanshuChaudhary.project.uber.UberApplication.dto.DriverDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.SignUpDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.UserDto;

public interface AuthService {

    String[] login(String email, String password);

    UserDto signup(SignUpDto signUpDto);

    DriverDto onBoardNewDriver(Long userId, String vehicleId);

    String refreshToken(String refreshToken);

}
