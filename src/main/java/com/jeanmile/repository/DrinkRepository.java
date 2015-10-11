package com.jeanmile.repository;

import com.jeanmile.domain.Drink;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Drink entity.
 */
public interface DrinkRepository extends JpaRepository<Drink,Long> {

}
