package com.aroma.shop.shop.service;
import com.aroma.shop.shop.dto.UserDTO;
import com.aroma.shop.shop.model.Role;
import com.aroma.shop.shop.model.User;
import com.aroma.shop.shop.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;

    @SneakyThrows
    @Transactional
    public void addUser(UserDTO userDTO) {
        if(userDTO == null) {
            throw new UsernameNotFoundException("User not found");
        }
        String roleName = "User";
        String password = passwordEncoder.encode(userDTO.getPassword());
        Role role = roleService.findByName(roleName).orElseThrow(() -> new Exception(
                "В базе не найдено роли с названием - " + roleName
        ));
        User user = new User(userDTO.getUsername(), password, userDTO.getEmail(), role);
        userRepository.save(user);
    }

    @Transactional
    public Optional<User> findUserByName(String name) {
        return userRepository.findUserByName(name);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByName(username).orElseThrow(() -> new UsernameNotFoundException(
                "Пользователь с именем " + username + " не найден"
        ));

        List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRole()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
    }
    @Transactional
    public String validateUser(UserDTO user) {
        String regex = "[@. /;'+?!-]";
        String username = user.getUsername();
        String password = user.getPassword();
        String passwordRepeat = user.getPasswordRepeat();
        String email = user.getEmail();
        if(findUserByName(username).isPresent())
            return "Пользователь с таким именем уже существует!";
        if(findUserByEmail(email).isPresent())
            return "Пользователь с таким email уже существует!";
        if(username.length() < 3 || username.length() >= 15)
            return "Вы ввели некоретное имя(от 3 до 15 символов)";
        if (Pattern.compile(regex).matcher(username).find())
            return "Имя содержит недопустимые символы";
        if(password.length() < 8)
            return "Слишком короткий пароль! (от 8 символов)";
        if(!passwordRepeat.equals(password))
            return "Пароли не совпадают";
        if(username.equals("Хуесос"))
            return "Ты дебил?";

        return "";
    }
}
