package com.jeanmile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jeanmile.domain.Drink;
import com.jeanmile.repository.DrinkRepository;
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
 * REST controller for managing Drink.
 */
@RestController
@RequestMapping("/api")
public class DrinkResource {

    private final Logger log = LoggerFactory.getLogger(DrinkResource.class);

    @Inject
    private DrinkRepository drinkRepository;

    /**
     * POST  /drinks -> Create a new drink.
     */
    @RequestMapping(value = "/drinks",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Drink> createDrink(@RequestBody Drink drink) throws URISyntaxException {
        log.debug("REST request to save Drink : {}", drink);
        if (drink.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new drink cannot already have an ID").body(null);
        }
        Drink result = drinkRepository.save(drink);
        return ResponseEntity.created(new URI("/api/drinks/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("drink", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /drinks -> Updates an existing drink.
     */
    @RequestMapping(value = "/drinks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Drink> updateDrink(@RequestBody Drink drink) throws URISyntaxException {
        log.debug("REST request to update Drink : {}", drink);
        if (drink.getId() == null) {
            return createDrink(drink);
        }
        Drink result = drinkRepository.save(drink);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("drink", drink.getId().toString()))
                .body(result);
    }

    /**
     * GET  /drinks -> get all the drinks.
     */
    @RequestMapping(value = "/drinks",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Drink> getAllDrinks() {
        log.debug("REST request to get all Drinks");
        return drinkRepository.findAll();
    }

    /**
     * GET  /drinks/:id -> get the "id" drink.
     */
    @RequestMapping(value = "/drinks/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Drink> getDrink(@PathVariable Long id) {
        log.debug("REST request to get Drink : {}", id);
        return Optional.ofNullable(drinkRepository.findOne(id))
            .map(drink -> new ResponseEntity<>(
                drink,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /drinks/:id -> delete the "id" drink.
     */
    @RequestMapping(value = "/drinks/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDrink(@PathVariable Long id) {
        log.debug("REST request to delete Drink : {}", id);
        drinkRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("drink", id.toString())).build();
    }
}
