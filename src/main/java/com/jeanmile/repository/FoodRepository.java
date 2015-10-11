package com.jeanmile.repository;

import com.jeanmile.domain.Food;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Food entity.
 */
public interface FoodRepository extends JpaRepository<Food,Long> {

}
