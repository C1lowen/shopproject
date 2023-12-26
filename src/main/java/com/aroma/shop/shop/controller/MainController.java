package com.aroma.shop.shop.controller;

import com.aroma.shop.shop.dto.ConditionProducts;
import com.aroma.shop.shop.dto.PriceDTO;
import com.aroma.shop.shop.dto.UserDTO;
import com.aroma.shop.shop.model.Products;
import com.aroma.shop.shop.model.User;
import com.aroma.shop.shop.service.MainService;
import com.aroma.shop.shop.service.ProductsService;
import com.aroma.shop.shop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.DoubleBuffer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private UserService userService;
    @Autowired
    private MainService mainService;
    @Autowired
    private ProductsService productsService;

    @PostMapping("/checksaveuser")
    @ResponseBody
    public ResponseEntity<String> saveUser(@RequestBody UserDTO user, HttpServletRequest request){
        String answer = userService.validateUser(user);
        String answerForClient = "{\"success\": false, \"answer\": \"" + answer + "\"}";
        if(!answer.isEmpty()) return ResponseEntity.ok(answerForClient);
        String password = user.getPassword();
        userService.addUser(user);
        mainService.userAuth(request, user, password);
        return ResponseEntity.ok("{\"success\": true, \"answer\": \"\"}");
    }

    @PostMapping("/filter")
    @ResponseBody
    public List<Products> colorFilter(@RequestBody ConditionProducts filter){
        return productsService.findByCondition(filter);
    }



    @GetMapping("/")
    public String rootPage() {
        return "index";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }
    @GetMapping("/shop")
    public String shopPage(Model model,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           @RequestParam(value = "gender", required = false) String[] gender,
                           @RequestParam(value = "brands", required = false) String[] brands,
                           @RequestParam(value = "color", required = false) String[] color,
                           @RequestParam(value = "sorted", required = false) String sorted,
                           @RequestParam(value = "priceStart", required = false) Double startPrice,
                           @RequestParam(value = "priceEnd", required = false) Double endPrice){
        model.addAttribute("carts", productsService.findAllProducts());

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        Page<Products> bookPage = productsService.findPaginated(PageRequest.of(currentPage - 1, pageSize),
                        new ConditionProducts(gender, color, brands, startPrice, endPrice, sorted));

        int totalPages = bookPage.getTotalPages();
        int sizePage = 4;

        model.addAttribute("sizePageElements", pageSize);
        model.addAttribute("genderBox", gender);
        model.addAttribute("sorted", sorted);
        model.addAttribute("brandsBox", brands);
        model.addAttribute("colorBox", color);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("fullCarts", bookPage);
        model.addAttribute("lastPage", totalPages);

        if(startPrice == null || endPrice == null)
            model.addAttribute("price", new PriceDTO(0, 1000));
        else
            model.addAttribute("price", new PriceDTO(startPrice, endPrice));

        if (currentPage >= sizePage && currentPage < totalPages)
            productsService.listNumber(currentPage-2,currentPage+1, model);
         else if(totalPages <= sizePage)
            productsService.listNumber(1,totalPages, model);
         else if(currentPage == totalPages)
            productsService.listNumber(currentPage-3,currentPage, model);
         else if (currentPage <= totalPages)
            productsService.listNumber(1,sizePage, model);


        return "category";
    }
    @GetMapping("/cart")
    public String cartPage(){
        return "cart";
    }
    @GetMapping("/shop/product")
    public String productPage(){
        return "single-product";
    }
    @GetMapping("/tracking")
    public String trackingPage(){
        return "tracking-order";
    }
    @GetMapping("/contact")
    public String contactPage(){
        return "contact";
    }
}
