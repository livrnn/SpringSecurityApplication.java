package com.example.springsecurityapplication.util;

import com.example.springsecurityapplication.models.Category;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.CategoryRepository;
import com.example.springsecurityapplication.repositories.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class ProductSearchConditions {

  public void checkSearchParameters(String search, String Ot, String Do, String category,
      String sort, Model model, CategoryRepository categoryRepository,
      ProductRepository productRepository) {
    if (!search.isEmpty()) {
      System.out.println("тут должны бить если есть search");
      if (!Ot.isEmpty() && !Do.isEmpty()) {
        if (category != null && !category.equals("")) {
          List<Category> categoryList = categoryRepository.findAll();

          for (Category category1 : categoryList) {
            if (category.equals(category1.getName())) {
              if (sort.equals("sorted_by_descending_price")) {
                model.addAttribute("search_product",
                    productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(),
                        Float.parseFloat(Ot), Float.parseFloat(Do), category1.getId()));
              } else {
                model.addAttribute("search_product",
                    productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(),
                        Float.parseFloat(Ot), Float.parseFloat(Do), category1.getId()));
              }
            }
          }
        } else {
          model.addAttribute("search_product",
              productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(search,
                  Float.parseFloat(Ot), Float.parseFloat(Do)));
        }
      } else if (Ot.isEmpty() && !(Do.isEmpty())) {
        if (category != null && !category.equals("")) {
          List<Category> categoryList = categoryRepository.findAll();
          for (Category category1 : categoryList) {
            if (category.equals(category1.getName())) {
              if (sort.equals("sorted_by_descending_price")) {
                model.addAttribute("search_product",
                    productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(),
                        1, Float.parseFloat(Do), category1.getId()));
              } else {
                model.addAttribute("search_product",
                    productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), 1,
                        Float.parseFloat(Do), category1.getId()));
              }
            }
          }
        } else {
          if (sort.equals("sorted_by_descending_price")) {
            model.addAttribute("search_product",
                productRepository.findByTitleOrderByPriceDest(search.toLowerCase(), 1,
                    Float.parseFloat(Do)));
          } else {
            model.addAttribute("search_product",
                productRepository.findByTitleOrderByPriceAsc(search.toLowerCase(), 1,
                    Float.parseFloat(Do)));
          }
        }
      } else if (!(Ot.isEmpty()) && Do.isEmpty()) {
        if (category != null && !category.equals("")) {
          List<Category> categoryList = categoryRepository.findAll();
          for (Category category1 : categoryList) {
            if (category.equals(category1.getName())) {
              if (sort.equals("sorted_by_descending_price")) {
                model.addAttribute("search_product",
                    productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(),
                        Float.parseFloat(Ot), Float.MAX_VALUE, category1.getId()));
              } else {
                model.addAttribute("search_product",
                    productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(),
                        Float.parseFloat(Ot), Float.MAX_VALUE, category1.getId()));
              }
            }
          }
        } else {
          if (sort.equals("sorted_by_descending_price")) {
            model.addAttribute("search_product",
                productRepository.findByTitleOrderByPriceDest(search.toLowerCase(),
                    Float.parseFloat(Ot), Float.MAX_VALUE));
          } else {
            model.addAttribute("search_product",
                productRepository.findByTitleOrderByPriceAsc(search.toLowerCase(),
                    Float.parseFloat(Ot), Float.MAX_VALUE));
          }
        }
      } else {
        System.out.println("тут должны быть если нет от и до");
        if (category != null && !category.equals("")) {
          List<Category> categoryList = categoryRepository.findAll();
          for (Category category1 : categoryList) {
            if (category.equals(category1.getName())) {
              if (sort.equals("desc")) {
                model.addAttribute("search_product",
                    productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(),
                        1, Float.MAX_VALUE, category1.getId()));
              } else {
                model.addAttribute("search_product",
                    productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), 1,
                        Float.MAX_VALUE, category1.getId()));
              }
            }
          }
        } else {
          if (sort.equals("sorted_by_descending_price")) {
            model.addAttribute("search_product",
                productRepository.findByTitleOrderByPriceDest(search.toLowerCase(), 1,
                    Float.MAX_VALUE));
          } else {
            model.addAttribute("search_product",
                productRepository.findByTitleOrderByPriceAsc(search.toLowerCase(), 1,
                    Float.MAX_VALUE));
          }
        }
      }
    } else {
      System.out.println("наименование пустое, движемся");
      if (!Ot.isEmpty() && !Do.isEmpty()) {
        if (category != null && !category.equals("")) {
          List<Category> categoryList = categoryRepository.findAll();
          for (Category category1 : categoryList) {
            if (category.equals(category1.getName())) {
              if (sort.equals("sorted_by_descending_price")) {
                model.addAttribute("search_product",
                    productRepository.findAllByCategoryOrderByPriceDesc(Float.parseFloat(Ot),
                        Float.parseFloat(Do), category1.getId()));
              } else {
                model.addAttribute("search_product",
                    productRepository.findAllByCategoryOrderByPriceAsc(Float.parseFloat(Ot),
                        Float.parseFloat(Do), category1.getId()));
              }
            }
          }
        } else {
          if (sort.equals("sorted_by_descending_price")) {
            model.addAttribute("search_product",
                productRepository.findAllByPriceOrderByPriceDesc(Float.parseFloat(Ot),
                    Float.parseFloat(Do)));
          } else {
            model.addAttribute("search_product",
                productRepository.findAllByPriceOrderByPriceAsc(Float.parseFloat(Ot),
                    Float.parseFloat(Do)));
          }
        }
      } else if (Ot.isEmpty() && !Do.isEmpty()) {
        if (category != null && !category.equals("")) {
          List<Category> categoryList = categoryRepository.findAll();
          for (Category category1 : categoryList) {
            if (category.equals(category1.getName())) {
              if (sort.equals("sorted_by_descending_price")) {
                model.addAttribute("search_product",
                    productRepository.findAllByCategoryOrderByPriceDesc(1, Float.parseFloat(Do),
                        category1.getId()));
              } else {
                model.addAttribute("search_product",
                    productRepository.findAllByCategoryOrderByPriceAsc(1, Float.parseFloat(Do),
                        category1.getId()));
              }
            }
          }
        } else {
          if (sort.equals("sorted_by_descending_price")) {
            model.addAttribute("search_product",
                productRepository.findAllByPriceOrderByPriceDesc(1, Float.parseFloat(Do)));
          } else {
            model.addAttribute("search_product",
                productRepository.findAllByPriceOrderByPriceAsc(1, Float.parseFloat(Do)));
          }
        }
      } else if (!Ot.isEmpty() && Do.isEmpty()) {
        if (category != null && !category.equals("")) {
          List<Category> categoryList = categoryRepository.findAll();
          for (Category category1 : categoryList) {
            if (category.equals(category1.getName())) {
              if (sort.equals("sorted_by_descending_price")) {
                model.addAttribute("search_product",
                    productRepository.findAllByCategoryOrderByPriceDesc(Float.parseFloat(Ot),
                        Float.MAX_VALUE, category1.getId()));
              } else {
                model.addAttribute("search_product",
                    productRepository.findAllByCategoryOrderByPriceAsc(Float.parseFloat(Ot),
                        Float.MAX_VALUE, category1.getId()));
              }
            }
          }
        } else {
          if (sort.equals("sorted_by_descending_price")) {
            model.addAttribute("search_product",
                productRepository.findAllByPriceOrderByPriceDesc(Float.parseFloat(Ot),
                    Float.MAX_VALUE));
          } else {
            model.addAttribute("search_product",
                productRepository.findAllByPriceOrderByPriceAsc(Float.parseFloat(Ot),
                    Float.MAX_VALUE));
          }
        }
      } else {
        System.out.println("от и до тоже пустые движемся");
        if (category != null && !category.equals("")) {
          System.out.println("категория почему-то не пустая");
          List<Category> categoryList = categoryRepository.findAll();
          for (Category category1 : categoryList) {
            if (category.equals(category1.getName())) {
              if (sort.equals("sorted_by_descending_price")) {
                System.out.println("нашли категорию");
                model.addAttribute("search_product",
                    productRepository.findAllByCategoryOrderByPriceDesc(1, Float.MAX_VALUE,
                        category1.getId()));
                List<Product> productList = productRepository.findAllByCategoryOrderByPriceDesc(
                    50000, 1, category1.getId());
                for (Product product : productList) {
                  System.out.println("Лежат товары: " + product.getTitle());
                }
              } else {
                model.addAttribute("search_product",
                    productRepository.findAllByCategoryOrderByPriceAsc(1, Float.MAX_VALUE,
                        category1.getId()));
              }
            }
          }
        } else {
          if (sort.equals("sorted_by_descending_price")) {
            model.addAttribute("search_product",
                productRepository.findAllByPriceOrderByPriceDesc(1, Float.MAX_VALUE));
            System.out.println("отработала по убыванию");
          } else {
            model.addAttribute("search_product",
                productRepository.findAllByPriceOrderByPriceAsc(1, Float.MAX_VALUE));
            System.out.println("отработала по возрастанию");
          }
        }
      }
    }
  }
}
