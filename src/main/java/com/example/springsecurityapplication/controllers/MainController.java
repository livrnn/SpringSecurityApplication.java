package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.enumm.Status;
import com.example.springsecurityapplication.models.Cart;
import com.example.springsecurityapplication.models.Category;
import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.CartRepository;
import com.example.springsecurityapplication.repositories.CategoryRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.repositories.ProductRepository;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.services.ProductService;
import com.example.springsecurityapplication.util.PersonValidator;
import com.example.springsecurityapplication.util.ProductSearchConditions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

  private final ProductRepository productRepository;

  private final PersonValidator personValidator;
  private final ProductSearchConditions productSearchConditions;
  private final PersonService personService;

  private final ProductService productService;

  private final CartRepository cartRepository;

  private final CategoryRepository categoryRepository;

  private final OrderRepository orderRepository;

  @Autowired
  public MainController(ProductRepository productRepository, PersonValidator personValidator,
      ProductSearchConditions productSearchConditions, PersonService personService, ProductService productService, CartRepository cartRepository,
      CategoryRepository categoryRepository, OrderRepository orderRepository) {
    this.productRepository = productRepository;
    this.personValidator = personValidator;
    this.productSearchConditions = productSearchConditions;
    this.personService = personService;
    this.productService = productService;
    this.cartRepository = cartRepository;
    this.categoryRepository = categoryRepository;
    this.orderRepository = orderRepository;
  }

  @GetMapping("/person account")
  public String index(Model model) {
    // Получаем объект аутентификации -> с помощью SpringContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. Из сессии текущего пользователя получаем объект, который был положен в данную сессию после аутентификации пользователя
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
    String role = personDetails.getPerson().getRole();
    if (role.equals("ROLE_ADMIN")) {
      return "redirect:/admin";
    }
//        System.out.println(personDetails.getPerson());
//        System.out.println("ID пользователя: " + personDetails.getPerson().getId());
//        System.out.println("Логин пользователя: " + personDetails.getPerson().getLogin());
//        System.out.println("Пароль пользователя: " + personDetails.getPerson().getPassword());
//        System.out.println(personDetails);
    model.addAttribute("products", productService.getAllProduct());
    return "product/productUser";
  }

  //    @GetMapping("/registration")
//    public String registration(Model model){
//        model.addAttribute("person", new Person());
//        return "registration";
//    }

  @GetMapping("/registration")
  public String registration(@ModelAttribute("person") Person person) {
    return "registration";
  }

  @PostMapping("/registration")
  public String resultRegistration(@ModelAttribute("person") @Valid Person person,
      BindingResult bindingResult) {
    personValidator.validate(person, bindingResult);
    if (bindingResult.hasErrors()) {
      return "registration";
    }
    personService.register(person);
    return "redirect:/person account";
  }

  @GetMapping("/person account/product/info/{id}")
  public String infoProduct(@PathVariable("id") int id, Model model) {
    model.addAttribute("product", productService.getProductId(id));
    return "/user/infoProduct";
  }

  @PostMapping("/person account/product/search")
  public String productSearch
      (@RequestParam("search") String search, @RequestParam("ot") String Ot,
          @RequestParam("do") String Do,
          @RequestParam(value = "price", required = false, defaultValue = "") String price,
          @RequestParam(value = "category", required = false, defaultValue = "") String category,
          @RequestParam(value = "price", defaultValue = "asc") String sort,
          Model model) {

    productSearchConditions.checkSearchParameters(search, Ot, Do, category, sort, model, categoryRepository, productRepository);

    List<Category> categoryList = categoryRepository.findAll();

    List<String> categoryName = new ArrayList<>();
    for (Category category1 : categoryList) {
      categoryName.add(category1.getName());
    }

    model.addAttribute("value_search", search);
    model.addAttribute("value_price_ot", Ot);
    model.addAttribute("value_price_do", Do);
    model.addAttribute("products", productService.getAllProduct());
    model.addAttribute("category", categoryName);

    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
      String role = personDetails.getPerson().getRole();
      if (role.equals("ROLE_USER")) {
        return "/product/productUser";
      }
    } catch (Exception e) {
      return "/product/product";
    }

    return "/product/product";
  }

  @GetMapping("/cart/add/{id}")
  public String addProductInCart(@PathVariable("id") int id, Model model) {
    // Получаем продукт по id
    Product product = productService.getProductId(id);
    // Извлекаем объект аутентифицированного пользователя
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
    // Извлекаем id пользователя из объекта
    int id_person = personDetails.getPerson().getId();
    Cart cart = new Cart(id_person, product.getId());
    cartRepository.save(cart);
    return "redirect:/cart";
  }

  @GetMapping("/cart")
  public String cart(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
    // Извлекаем id пользователя из объекта
    int id_person = personDetails.getPerson().getId();

    List<Cart> cartList = cartRepository.findByPersonId(id_person);
    List<Product> productList = new ArrayList<>();

    // Получаем продукты из корзины по id товара
    for (Cart cart : cartList) {
      productList.add(productService.getProductId(cart.getProductId()));
    }

    // Вычисление итоговой цена
    float price = 0;
    for (Product product : productList) {
      price += product.getPrice();
    }

    model.addAttribute("price", price);
    model.addAttribute("cart_product", productList);
    return "/user/cart";
  }

  @GetMapping("/cart/delete/{id}")
  public String deleteProductFromCart(Model model, @PathVariable("id") int id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
    // Извлекаем id пользователя из объекта
    int id_person = personDetails.getPerson().getId();
    List<Cart> cartList = cartRepository.findByPersonId(id_person);
    List<Product> productList = new ArrayList<>();

    // Получаем продукты из корзины по id товара
    for (Cart cart : cartList) {
      productList.add(productService.getProductId(cart.getProductId()));
    }
    cartRepository.deleteCartByProductId(id);
    return "redirect:/cart";
  }

  @GetMapping("/order/create")
  public String order() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
    // Извлекаем id пользователя из объекта
    int id_person = personDetails.getPerson().getId();

    List<Cart> cartList = cartRepository.findByPersonId(id_person);
    List<Product> productList = new ArrayList<>();

    // Получаем продукты из корзины по id товара
    for (Cart cart : cartList) {
      productList.add(productService.getProductId(cart.getProductId()));
    }

    // Вычисление итоговой цена
    float price = 0;
    for (Product product : productList) {
      price += product.getPrice();
    }

    String uuid = UUID.randomUUID().toString();
    for (Product product : productList) {
      Order newOrder = new Order(uuid, product, personDetails.getPerson(), 1, product.getPrice(),
          Status.Оформлен);
      orderRepository.save(newOrder);
      cartRepository.deleteCartByProductId(product.getId());
    }
    return "redirect:/orders";
  }

  @GetMapping("/orders")
  public String orderUser(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
    List<Order> orderList = orderRepository.findByPerson(personDetails.getPerson());
    model.addAttribute("orders", orderList);
    return "/user/orders";
  }


}
