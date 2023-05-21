package com.example.springsecurityapplication.repositories;

import com.example.springsecurityapplication.models.Category;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

  Category findByName(String name);

}
