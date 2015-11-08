package com.jeanmile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jeanmile.domain.Purchase;
import com.jeanmile.domain.User;
import com.jeanmile.repository.PurchaseRepository;
import com.jeanmile.repository.UserRepository;
import com.jeanmile.security.SecurityUtils;
import com.jeanmile.service.PurchaseService;
import com.jeanmile.web.rest.util.HeaderUtil;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Purchase.
 */
@RestController
@RequestMapping("/api")
public class PurchaseResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseResource.class);

    @Inject
    private PurchaseRepository purchaseRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PurchaseService purchaseService;

    /**
     * POST  /purchases -> Create a new purchase.
     */
    @RequestMapping(value = "/purchases",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase) throws URISyntaxException {
        log.debug("REST request to save Purchase : {}", purchase);
        if (purchase.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new purchase cannot already have an ID").body(null);
        }
        // Set current user.
        if (SecurityUtils.isAuthenticated()) {
            Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
            purchase.setUser(currentUser.get());
        }

        Purchase result = purchaseRepository.save(purchase);
        return ResponseEntity.created(new URI("/api/purchases/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("purchase", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /purchases -> Updates an existing purchase.
     */
    @RequestMapping(value = "/purchases",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Purchase> updatePurchase(@RequestBody Purchase purchase) throws URISyntaxException {
        log.debug("REST request to update Purchase : {}", purchase);
        if (purchase.getId() == null) {
            return createPurchase(purchase);
        }
        Purchase result = purchaseRepository.save(purchase);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("purchase", purchase.getId().toString()))
                .body(result);
    }

    /**
     * GET  /purchases -> get all the purchases.
     */
    @RequestMapping(value = "/purchases",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Purchase> getAllPurchases() {
        log.debug("REST request to get all Purchases");
        return purchaseRepository.findAll();
    }

    /**
     * GET  /purchases/:id -> get the "id" purchase.
     */
    @RequestMapping(value = "/purchases/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Purchase> getPurchase(@PathVariable Long id) {
        log.debug("REST request to get Purchase : {}", id);
        return Optional.ofNullable(purchaseRepository.findOne(id))
            .map(purchase -> new ResponseEntity<>(
                purchase,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /purchases/:id -> delete the "id" purchase.
     */
    @RequestMapping(value = "/purchases/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        log.debug("REST request to delete Purchase : {}", id);
        purchaseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("purchase", id.toString())).build();
    }

    /**
     * GET purchases?fromDate=07/11/2015&toDate=07/11/2015 -> get purchases fromDate toDate
     */
    @RequestMapping(value = "/purchases",
        method = RequestMethod.GET,
        params = {"fromDate", "toDate"})
    public List<Purchase> getByDates(@RequestParam(value = "fromDate") LocalDate fromDate,
                                     @RequestParam(value = "toDate") LocalDate toDate) {
        log.debug("REST request to get Purchases by date : {0 {1}", fromDate, toDate);
        return this.purchaseService.findByDates(fromDate, toDate);
    }

    /**
     * GET purchases/1?atDate=18/10/2015 -> get purchase at Date for current user.
     */
    @RequestMapping(value = "/purchases/me",
        method = RequestMethod.GET,
        params = {"atDate"})
    public List<Purchase> getByUser(@RequestParam(value = "atDate") LocalDate atDate) {
        log.debug("REST request to get Purchases at date : {0}", atDate);
        return this.purchaseService.findByUserIsCurrentUserAndByDate(atDate);
    }
}
