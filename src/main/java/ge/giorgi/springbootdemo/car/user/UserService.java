package ge.giorgi.springbootdemo.car.user;


import ge.giorgi.springbootdemo.car.dto.PaginationRequest;
import ge.giorgi.springbootdemo.car.error.AccessDeniedException;
import ge.giorgi.springbootdemo.car.error.NotFoundException;
import ge.giorgi.springbootdemo.car.user.model.UserDTO;
import ge.giorgi.springbootdemo.car.user.persistence.AppUser;
import ge.giorgi.springbootdemo.car.user.persistence.AppUserRepository;
import ge.giorgi.springbootdemo.car.user.persistence.Role;
import ge.giorgi.springbootdemo.car.user.model.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ge.giorgi.springbootdemo.car.security.AuthUtil.getAuthenticatedUser;
import static ge.giorgi.springbootdemo.car.security.AuthUtil.hasAdminRole;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public long createUser(UserRequest userRequest){
        AppUser user = new AppUser();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setBalanceInCents(userRequest.getBalanceInCents());
        user.setRoles(userRequest.getRoleIds().stream().
                map(roleService::getRole).collect(Collectors.toSet()));

        appUserRepository.save(user);
        return user.getId();
    }

    public AppUser findByUsername(String username){
        return appUserRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("User not found"));
    }

    public AppUser findById(Long id){
        return appUserRepository.findById(id).orElseThrow(() -> new NotFoundException("User " + id + " was not found"));
    }

    public void save(AppUser user){
        appUserRepository.save(user);
    }

    private UserDTO mapUser(AppUser appUser) {
        Set<String> roleNames = appUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserDTO(
                appUser.getId(),
                appUser.getUsername(),
                roleNames, // Pass the role names
                appUser.getBalanceInCents()
        );
    }

    public Page<UserDTO> getUsers(PaginationRequest paginationRequest){
        return appUserRepository.findAll(PageRequest.of(paginationRequest.getPage(), paginationRequest.getPageSize()))
                .map(this::mapUser);
    }

    public UserDTO getUser(Optional<Long> userId){
        AppUser user = findByUsername(getAuthenticatedUser());
        if(userId.isPresent()){
            if(!hasAdminRole() && !userId.get().equals(user.getId())){
                throw new AccessDeniedException("User " + user.getUsername() + " can not access other's account!");
            }
            return mapUser(findById(userId.get()));
        }
        return mapUser(user);
    }


}
