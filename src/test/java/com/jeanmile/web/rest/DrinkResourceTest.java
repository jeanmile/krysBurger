package com.jeanmile.web.rest;

import com.jeanmile.Application;
import com.jeanmile.domain.Drink;
import com.jeanmile.repository.DrinkRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DrinkResource REST controller.
 *
 * @see DrinkResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DrinkResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final String DEFAULT_PHOTO = "AAAAA";
    private static final String UPDATED_PHOTO = "BBBBB";

    @Inject
    private DrinkRepository drinkRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDrinkMockMvc;

    private Drink drink;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DrinkResource drinkResource = new DrinkResource();
        ReflectionTestUtils.setField(drinkResource, "drinkRepository", drinkRepository);
        this.restDrinkMockMvc = MockMvcBuilders.standaloneSetup(drinkResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        drink = new Drink();
        drink.setName(DEFAULT_NAME);
        drink.setPrice(DEFAULT_PRICE);
        drink.setPhoto(DEFAULT_PHOTO);
    }

    @Test
    @Transactional
    public void createDrink() throws Exception {
        int databaseSizeBeforeCreate = drinkRepository.findAll().size();

        // Create the Drink

        restDrinkMockMvc.perform(post("/api/drinks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(drink)))
                .andExpect(status().isCreated());

        // Validate the Drink in the database
        List<Drink> drinks = drinkRepository.findAll();
        assertThat(drinks).hasSize(databaseSizeBeforeCreate + 1);
        Drink testDrink = drinks.get(drinks.size() - 1);
        assertThat(testDrink.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDrink.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testDrink.getPhoto()).isEqualTo(DEFAULT_PHOTO);
    }

    @Test
    @Transactional
    public void getAllDrinks() throws Exception {
        // Initialize the database
        drinkRepository.saveAndFlush(drink);

        // Get all the drinks
        restDrinkMockMvc.perform(get("/api/drinks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(drink.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO.toString())));
    }

    @Test
    @Transactional
    public void getDrink() throws Exception {
        // Initialize the database
        drinkRepository.saveAndFlush(drink);

        // Get the drink
        restDrinkMockMvc.perform(get("/api/drinks/{id}", drink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(drink.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDrink() throws Exception {
        // Get the drink
        restDrinkMockMvc.perform(get("/api/drinks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDrink() throws Exception {
        // Initialize the database
        drinkRepository.saveAndFlush(drink);

		int databaseSizeBeforeUpdate = drinkRepository.findAll().size();

        // Update the drink
        drink.setName(UPDATED_NAME);
        drink.setPrice(UPDATED_PRICE);
        drink.setPhoto(UPDATED_PHOTO);

        restDrinkMockMvc.perform(put("/api/drinks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(drink)))
                .andExpect(status().isOk());

        // Validate the Drink in the database
        List<Drink> drinks = drinkRepository.findAll();
        assertThat(drinks).hasSize(databaseSizeBeforeUpdate);
        Drink testDrink = drinks.get(drinks.size() - 1);
        assertThat(testDrink.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDrink.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testDrink.getPhoto()).isEqualTo(UPDATED_PHOTO);
    }

    @Test
    @Transactional
    public void deleteDrink() throws Exception {
        // Initialize the database
        drinkRepository.saveAndFlush(drink);

		int databaseSizeBeforeDelete = drinkRepository.findAll().size();

        // Get the drink
        restDrinkMockMvc.perform(delete("/api/drinks/{id}", drink.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Drink> drinks = drinkRepository.findAll();
        assertThat(drinks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
