package com.DipanshuChaudhary.project.uber.UberApplication.controllers;


//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("/signUp")
//    public UserDto signUp(@RequestBody SignUpDto signUpDto){
//
//        return authService.signup(signUpDto);
//    }
//}



//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("/signUp")
//    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto){
//
//        return ResponseEntity.ok(authService.signup(signUpDto));
//    }
//}


import com.DipanshuChaudhary.project.uber.UberApplication.advices.ApiResponse;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.*;
import com.DipanshuChaudhary.project.uber.UberApplication.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<ApiResponse<UserDto>> signUp(@RequestBody SignUpDto signUpDto) {
        // Call the service to handle sign-up logic
        UserDto userDto = authService.signup(signUpDto);

        // Wrap the result in an ApiResponse object
        ApiResponse<UserDto> response = new ApiResponse<>(userDto);

        // Return the ApiResponse object
        return ResponseEntity.ok(response);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/onBoardNewDriver/{userId}")
    public ResponseEntity<ApiResponse<DriverDto>> onBoardNewDriver(@PathVariable Long userId, @RequestBody OnBoardDriverDto onBoardDriverDto){

        DriverDto driverDto = authService.onBoardNewDriver(userId, onBoardDriverDto.getVehicleId());

        ApiResponse<DriverDto> response = new ApiResponse<>(driverDto);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto,
                                                        HttpServletRequest httpServletRequest,
                                                        HttpServletResponse httpServletResponse) {

        String[] tokens = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        Cookie cookie = new Cookie("token", tokens[1]);
        cookie.setHttpOnly(true);

        httpServletResponse.addCookie(cookie);

        ApiResponse<LoginResponseDto> apiResponse = new ApiResponse<>(new LoginResponseDto(tokens[0]));

        return ResponseEntity.ok(apiResponse);


//        String[] tokens = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
//
//        Cookie cookie = new Cookie("token", tokens[1]);
//        cookie.setHttpOnly(true);
//
//        httpServletResponse.addCookie(cookie);
//
//        return ResponseEntity.ok(new LoginResponseDto(tokens[0]));

    }


    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponseDto>> refresh(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies()).
                filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        String accessToken = authService.refreshToken(refreshToken);

        ApiResponse<LoginResponseDto> apiResponse = new ApiResponse<>(new LoginResponseDto(accessToken));
        return ResponseEntity.ok(apiResponse);
    }
}


































