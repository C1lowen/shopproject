//package com.aroma.shop.shop.controller;
//
//import com.aroma.shop.shop.dto.UserDTO;
//import com.aroma.shop.shop.model.User;
//import com.aroma.shop.shop.service.MainService;
//import com.aroma.shop.shop.service.UserService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@Controller
//public class UserController {
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/loginuser")
//    public String loginUser(@RequestBody User user){
//        userService.findUserByName(user.getUsername());
//        return "d";
//    }
//
//
//}
