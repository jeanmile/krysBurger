package com.jeanmile.service;

import com.jeanmile.domain.Purchase;
import com.jeanmile.repository.PurchaseRepository;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional
public class PurchaseService {
    @Inject
    PurchaseRepository purchaseRepository;

    public List<Purchase> findByDates(LocalDate fromDate, LocalDate toDate) {
        return purchaseRepository.findAllByDateBetween(fromDate, toDate);
    }
}
