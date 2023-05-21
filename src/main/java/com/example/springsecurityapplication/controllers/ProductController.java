package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.models.Category;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.CategoryRepository;
import com.example.springsecurityapplication.repositories.ProductRepository;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.ProductService;
import com.example.springsecurityapplication.util.ProductSearchConditions;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductService productService;
  private final ProductSearchConditions productSearchConditions;

  @Autowired
  public ProductController(ProductRepository productRepository,
      CategoryRepository categoryRepository, ProductService productService,
      ProductSearchConditions productSearchConditions) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.productService = productService;
    this.productSearchConditions = productSearchConditions;
  }

  @GetMapping("")
  public String getAllProduct(Model model) {
    model.addAttribute("products", productService.getAllProduct());
    return "/product/product";
  }

  @GetMapping("/info/{id}")
  public String infoProduct(@PathVariable("id") int id, Model model) {
    model.addAttribute("product", productService.getProductId(id));
    return "/product/infoProduct";
  }

  //  @PostMapping("/search")
//  public String productSearch(
//      @RequestParam("search") String search,
//      @RequestParam("ot") String ot,
//      @RequestParam("do") String Do,
//      @RequestParam(value = "price", required = false, defaultValue = "") String price,
//      @RequestParam(value = "contract", required = false, defaultValue = "") String contract,
//      Model model) {
//    model.addAttribute("products", productService.getAllProduct());
//
//    if (!ot.isEmpty() & !Do.isEmpty()) {
//      if (!price.isEmpty()) {
//        if (price.equals("sorted_by_ascending_price")) {
//          if (!contract.isEmpty()) {
//            switch (contract) {
//              case "clothes" -> model.addAttribute("search_product",
//                  productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(),
//                      Float.parseFloat(ot), Float.parseFloat(Do), 1));
//              case "shoes" -> model.addAttribute("search_product",
//                  productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(),
//                      Float.parseFloat(ot), Float.parseFloat(Do), 2));
//              case "accessories" -> model.addAttribute("search_product",
//                  productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(),
//                      Float.parseFloat(ot), Float.parseFloat(Do), 3));
//            }
//          } else {
//            model.addAttribute("search_product",
//                productRepository.findByTitleOrderByPriceAsc(search.toLowerCase(),
//                    Float.parseFloat(ot), Float.parseFloat(Do)));
//          }
//        } else if (price.equals("sorted_by_descending_price")) {
//          if (!contract.isEmpty()) {
//            System.out.println(contract);
//            switch (contract) {
//              case "clothes" -> model.addAttribute("search_product",
//                  productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(),
//                      Float.parseFloat(ot), Float.parseFloat(Do), 1));
//              case "shoes" -> model.addAttribute("search_product",
//                  productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(),
//                      Float.parseFloat(ot), Float.parseFloat(Do), 2));
//              case "accessories" -> model.addAttribute("search_product",
//                  productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(),
//                      Float.parseFloat(ot), Float.parseFloat(Do), 3));
//            }
//          } else {
//            model.addAttribute("search_product",
//                productRepository.findByTitleOrderByPriceDesc(search.toLowerCase(),
//                    Float.parseFloat(ot), Float.parseFloat(Do)));
//          }
//        }
//      } else {
//        model.addAttribute("search_product",
//            productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(
//                search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));
//      }
//    } else {
//      model.addAttribute("search_product",
//          productRepository.findByTitleContainingIgnoreCase(search));
//    }
//
//    model.addAttribute("value_search", search);
//    model.addAttribute("value_price_ot", ot);
//    model.addAttribute("value_price_do", Do);
//    return "/product/product";
//
//  }
  @PostMapping("/search")
  public String getAll(Model model, @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "category", required = false, defaultValue = "") String category,
      @RequestParam(value = "ot", required = false) String Ot,
      @RequestParam(value = "do", required = false) String Do,
      @RequestParam(value = "price", required = false, defaultValue = "asc") String sort) {

    System.out.println("пришли данные: " + category + " " + sort + " " + search);
    System.out.println("пришли данные: " + Ot + " " + Do);

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
}
