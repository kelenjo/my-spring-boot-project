package ge.giorgi.springbootdemo.car.user;

import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.user.auth.LoginService;
import ge.giorgi.springbootdemo.car.user.model.UserDTO;
import ge.giorgi.springbootdemo.car.user.model.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static ge.giorgi.springbootdemo.car.security.AuthUtil.ADMIN;
import static ge.giorgi.springbootdemo.car.security.AuthUtil.USER_OR_ADMIN;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {
    private final UserCarFacadeService userCarFacadeService;
    private final UserService userService;
    private final LoginService loginService;

//    @PostMapping("/logout")
//    @PreAuthorize(USER_OR_ADMIN)
//    public ResponseEntity<String> logout(@RequestHeader(name = "Authorization", required = false) String token) {
//        if (token == null || token.isBlank()) {
//            return ResponseEntity.badRequest().body("Token is required for logout.");
//        }
//        loginService.logout(token);
//        return ResponseEntity.ok("Logged out successfully.");
//    }

    @PostMapping
    @PreAuthorize(ADMIN)
    public ResponseEntity<String> createUser(@RequestBody @Valid UserRequest userRequest){
        long userId=userService.createUser(userRequest);
        return ResponseEntity.ok("User created successfully. UserId: "+userId);
    }

    @GetMapping
    @PreAuthorize(ADMIN)
    public Page<UserDTO> getUsers(@RequestBody @Valid PaginationRequest paginationRequest){
        return userService.getUsers(paginationRequest);
    }

    @GetMapping("/account")
    @PreAuthorize(USER_OR_ADMIN)
    public ResponseEntity<UserDTO> getUser(@RequestParam Optional<Long> userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/myCars")
    @PreAuthorize(USER_OR_ADMIN)
    public Page<OwnedCarsDTO> getCars(
            @RequestBody @Valid PaginationRequest paginationRequest,
            @RequestParam Optional<Long> userId) // admin should see others cars as well
    {
        return userCarFacadeService.getUserCars(paginationRequest, userId);
    }

    @GetMapping("/UsersCars")
    @PreAuthorize(ADMIN)
    public Page<OwnedCarsDTO> getUser(@RequestBody @Valid PaginationRequest paginationRequest){
        return userCarFacadeService.getAllUserCars(paginationRequest);
    }





}
