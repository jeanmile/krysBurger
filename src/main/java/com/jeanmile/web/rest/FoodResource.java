package com.jeanmile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jeanmile.domain.Food;
import com.jeanmile.repository.FoodRepository;
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
 * REST controller for managing Food.
 */
@RestController
@RequestMapping("/api")
public class FoodResource {

    private final Logger log = LoggerFactory.getLogger(FoodResource.class);

    @Inject
    private FoodRepository foodRepository;

    /**
     * POST  /foods -> Create a new food.
     */
    @RequestMapping(value = "/foods",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Food> createFood(@RequestBody Food food) throws URISyntaxException {
        log.debug("REST request to save Food : {}", food);
        if (food.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new food cannot already have an ID").body(null);
        }
        Food result = foodRepository.save(food);
        return ResponseEntity.created(new URI("/api/foods/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("food", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /foods -> Updates an existing food.
     */
    @RequestMapping(value = "/foods",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Food> updateFood(@RequestBody Food food) throws URISyntaxException {
        log.debug("REST request to update Food : {}", food);
        if (food.getId() == null) {
            return createFood(food);
        }
        Food result = foodRepository.save(food);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("food", food.getId().toString()))
                .body(result);
    }

    /**
     * GET  /foods -> get all the foods.
     */
    @RequestMapping(value = "/foods",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Food> getAllFoods() {
        log.debug("REST request to get all Foods");
        return foodRepository.findAll();
    }

    /**
     * GET  /foods/:id -> get the "id" food.
     */
    @RequestMapping(value = "/foods/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Food> getFood(@PathVariable Long id) {
        log.debug("REST request to get Food : {}", id);
        return Optional.ofNullable(foodRepository.findOne(id))
            .map(food -> new ResponseEntity<>(
                food,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /foods/:id -> delete the "id" food.
     */
    @RequestMapping(value = "/foods/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        log.debug("REST request to delete Food : {}", id);
        foodRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("food", id.toString())).build();
    }
}
