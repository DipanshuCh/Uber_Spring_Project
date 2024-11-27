package com.DipanshuChaudhary.project.uber.UberApplication.services.implementation;

import com.DipanshuChaudhary.project.uber.UberApplication.dto.DriverDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.SignUpDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.UserDto;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Driver;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.User;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.Role;
import com.DipanshuChaudhary.project.uber.UberApplication.exceptions.ResourceNotFoundException;
import com.DipanshuChaudhary.project.uber.UberApplication.exceptions.RuntimeConflictException;
import com.DipanshuChaudhary.project.uber.UberApplication.repositories.UserRepository;
import com.DipanshuChaudhary.project.uber.UberApplication.security_Service.JWTServices;
import com.DipanshuChaudhary.project.uber.UberApplication.services.AuthService;
import com.DipanshuChaudhary.project.uber.UberApplication.services.DriverService;
import com.DipanshuChaudhary.project.uber.UberApplication.services.RiderService;
import com.DipanshuChaudhary.project.uber.UberApplication.services.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.Role.DRIVER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTServices jwtServices;



    @Override
    public String[] login(String email, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email,password)
        );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtServices.generateAccessToken(user);
        String refreshToken = jwtServices.generateRefreshToken(user);

        String[] tokens  = {accessToken, refreshToken};

        return tokens;
    }

    @Override
    @Transactional
    public UserDto signup(SignUpDto signUpDto) {

        // first we have to verify the user, if he already exists or not
//        User user = userRepository.findByEmail(signUpDto.getEmail());
//        if (user != null){
//            throw new RuntimeException("Cannot signUp, User already exists with email "+signUpDto.getEmail());
//        }

        User user = userRepository.findByEmail(signUpDto.getEmail()).orElse(null);
        if(user != null){
            throw new RuntimeConflictException("Cannot signup, User already exists with this email "+signUpDto.getEmail());
        }

        User mappedUser = modelMapper.map(signUpDto, User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));

        // here set the password using password encoder
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
        User savedUser = userRepository.save(mappedUser);

        // create user related entities
        riderService.createNewRider(savedUser);
        walletService.createNewWallet(savedUser);


        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public DriverDto onBoardNewDriver(Long userId, String vehicleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id" +userId));

        if (user.getRoles().contains(DRIVER)){
            throw new RuntimeException("User with id "+userId+" is already a Driver");
        }

        Driver createDriver = Driver.builder()
                .user(user)
                .rating(0.0)
                .vehicleId(vehicleId)
                .available(true)
                .build();

        user.getRoles().add(DRIVER);
        userRepository.save(user);

        Driver savedDriver = driverService.createNewDriver(createDriver);
        return modelMapper.map(savedDriver,DriverDto.class);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtServices.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with " +
                "id: "+userId));

        return jwtServices.generateAccessToken(user);
    }

}
