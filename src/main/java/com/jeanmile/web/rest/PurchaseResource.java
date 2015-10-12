package com.jeanmile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jeanmile.domain.Purchase;
import com.jeanmile.repository.PurchaseRepository;
import com.jeanmile.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
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
}