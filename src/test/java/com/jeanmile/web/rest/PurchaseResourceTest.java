package com.jeanmile.web.rest;

import com.jeanmile.Application;
import com.jeanmile.domain.Purchase;
import com.jeanmile.repository.PurchaseRepository;

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
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PurchaseResource REST controller.
 *
 * @see PurchaseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PurchaseResourceTest {


    private static final LocalDate DEFAULT_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE = new LocalDate();

    private static final Boolean DEFAULT_FRIES = false;
    private static final Boolean UPDATED_FRIES = true;

    private static final Boolean DEFAULT_DELIVERY = false;
    private static final Boolean UPDATED_DELIVERY = true;

    @Inject
    private PurchaseRepository purchaseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPurchaseMockMvc;

    private Purchase purchase;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PurchaseResource purchaseResource = new PurchaseResource();
        ReflectionTestUtils.setField(purchaseResource, "purchaseRepository", purchaseRepository);
        this.restPurchaseMockMvc = MockMvcBuilders.standaloneSetup(purchaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        purchase = new Purchase();
        purchase.setDate(DEFAULT_DATE);
        purchase.setFries(DEFAULT_FRIES);
        purchase.setDelivery(DEFAULT_DELIVERY);
    }

    @Test
    @Transactional
    public void createPurchase() throws Exception {
        int databaseSizeBeforeCreate = purchaseRepository.findAll().size();

        // Create the Purchase

        restPurchaseMockMvc.perform(post("/api/purchases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchase)))
                .andExpect(status().isCreated());

        // Validate the Purchase in the database
        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeCreate + 1);
        Purchase testPurchase = purchases.get(purchases.size() - 1);
        assertThat(testPurchase.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPurchase.getFries()).isEqualTo(DEFAULT_FRIES);
        assertThat(testPurchase.getDelivery()).isEqualTo(DEFAULT_DELIVERY);
    }

    @Test
    @Transactional
    public void getAllPurchases() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchases
        restPurchaseMockMvc.perform(get("/api/purchases"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(purchase.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].fries").value(hasItem(DEFAULT_FRIES.booleanValue())))
                .andExpect(jsonPath("$.[*].delivery").value(hasItem(DEFAULT_DELIVERY.booleanValue())));
    }

    @Test
    @Transactional
    public void getPurchase() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get the purchase
        restPurchaseMockMvc.perform(get("/api/purchases/{id}", purchase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(purchase.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.fries").value(DEFAULT_FRIES.booleanValue()))
            .andExpect(jsonPath("$.delivery").value(DEFAULT_DELIVERY.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPurchase() throws Exception {
        // Get the purchase
        restPurchaseMockMvc.perform(get("/api/purchases/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchase() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

		int databaseSizeBeforeUpdate = purchaseRepository.findAll().size();

        // Update the purchase
        purchase.setDate(UPDATED_DATE);
        purchase.setFries(UPDATED_FRIES);
        purchase.setDelivery(UPDATED_DELIVERY);

        restPurchaseMockMvc.perform(put("/api/purchases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchase)))
                .andExpect(status().isOk());

        // Validate the Purchase in the database
        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeUpdate);
        Purchase testPurchase = purchases.get(purchases.size() - 1);
        assertThat(testPurchase.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPurchase.getFries()).isEqualTo(UPDATED_FRIES);
        assertThat(testPurchase.getDelivery()).isEqualTo(UPDATED_DELIVERY);
    }

    @Test
    @Transactional
    public void deletePurchase() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

		int databaseSizeBeforeDelete = purchaseRepository.findAll().size();

        // Get the purchase
        restPurchaseMockMvc.perform(delete("/api/purchases/{id}", purchase.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeDelete - 1);
    }
}
