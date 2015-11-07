package com.jeanmile.repository;

import com.jeanmile.domain.Purchase;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Purchase entity.
 */
public interface PurchaseRepository extends JpaRepository<Purchase,Long> {

    @Query("select purchase from Purchase purchase where purchase.user.login = ?#{principal.username}")
    List<Purchase> findByUserIsCurrentUser();

    @Query("select purchase from Purchase purchase " +
            "where purchase.user.login = ?#{principal.username} and purchase.date = :atDate")
    List<Purchase> findByUserIsCurrentUserAndByDate(@Param("atDate") LocalDate atDate);

    List<Purchase> findAllByDateBetween(LocalDate fromDate, LocalDate toDate);
}
