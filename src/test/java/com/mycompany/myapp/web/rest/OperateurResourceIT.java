package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Operateur;
import com.mycompany.myapp.repository.OperateurRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OperateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperateurResourceIT {

    private static final String DEFAULT_CENTRE_RC = "AAAAAAAAAA";
    private static final String UPDATED_CENTRE_RC = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_RC = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_RC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperateurRepository operateurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperateurMockMvc;

    private Operateur operateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operateur createEntity(EntityManager em) {
        Operateur operateur = new Operateur().centreRc(DEFAULT_CENTRE_RC).numeroRc(DEFAULT_NUMERO_RC);
        return operateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operateur createUpdatedEntity(EntityManager em) {
        Operateur operateur = new Operateur().centreRc(UPDATED_CENTRE_RC).numeroRc(UPDATED_NUMERO_RC);
        return operateur;
    }

    @BeforeEach
    public void initTest() {
        operateur = createEntity(em);
    }

    @Test
    @Transactional
    void createOperateur() throws Exception {
        int databaseSizeBeforeCreate = operateurRepository.findAll().size();
        // Create the Operateur
        restOperateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operateur)))
            .andExpect(status().isCreated());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeCreate + 1);
        Operateur testOperateur = operateurList.get(operateurList.size() - 1);
        assertThat(testOperateur.getCentreRc()).isEqualTo(DEFAULT_CENTRE_RC);
        assertThat(testOperateur.getNumeroRc()).isEqualTo(DEFAULT_NUMERO_RC);
    }

    @Test
    @Transactional
    void createOperateurWithExistingId() throws Exception {
        // Create the Operateur with an existing ID
        operateur.setId(1L);

        int databaseSizeBeforeCreate = operateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operateur)))
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOperateurs() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList
        restOperateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].centreRc").value(hasItem(DEFAULT_CENTRE_RC)))
            .andExpect(jsonPath("$.[*].numeroRc").value(hasItem(DEFAULT_NUMERO_RC)));
    }

    @Test
    @Transactional
    void getOperateur() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        // Get the operateur
        restOperateurMockMvc
            .perform(get(ENTITY_API_URL_ID, operateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operateur.getId().intValue()))
            .andExpect(jsonPath("$.centreRc").value(DEFAULT_CENTRE_RC))
            .andExpect(jsonPath("$.numeroRc").value(DEFAULT_NUMERO_RC));
    }

    @Test
    @Transactional
    void getNonExistingOperateur() throws Exception {
        // Get the operateur
        restOperateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOperateur() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();

        // Update the operateur
        Operateur updatedOperateur = operateurRepository.findById(operateur.getId()).get();
        // Disconnect from session so that the updates on updatedOperateur are not directly saved in db
        em.detach(updatedOperateur);
        updatedOperateur.centreRc(UPDATED_CENTRE_RC).numeroRc(UPDATED_NUMERO_RC);

        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOperateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOperateur))
            )
            .andExpect(status().isOk());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
        Operateur testOperateur = operateurList.get(operateurList.size() - 1);
        assertThat(testOperateur.getCentreRc()).isEqualTo(UPDATED_CENTRE_RC);
        assertThat(testOperateur.getNumeroRc()).isEqualTo(UPDATED_NUMERO_RC);
    }

    @Test
    @Transactional
    void putNonExistingOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operateur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperateurWithPatch() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();

        // Update the operateur using partial update
        Operateur partialUpdatedOperateur = new Operateur();
        partialUpdatedOperateur.setId(operateur.getId());

        partialUpdatedOperateur.numeroRc(UPDATED_NUMERO_RC);

        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperateur))
            )
            .andExpect(status().isOk());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
        Operateur testOperateur = operateurList.get(operateurList.size() - 1);
        assertThat(testOperateur.getCentreRc()).isEqualTo(DEFAULT_CENTRE_RC);
        assertThat(testOperateur.getNumeroRc()).isEqualTo(UPDATED_NUMERO_RC);
    }

    @Test
    @Transactional
    void fullUpdateOperateurWithPatch() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();

        // Update the operateur using partial update
        Operateur partialUpdatedOperateur = new Operateur();
        partialUpdatedOperateur.setId(operateur.getId());

        partialUpdatedOperateur.centreRc(UPDATED_CENTRE_RC).numeroRc(UPDATED_NUMERO_RC);

        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperateur))
            )
            .andExpect(status().isOk());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
        Operateur testOperateur = operateurList.get(operateurList.size() - 1);
        assertThat(testOperateur.getCentreRc()).isEqualTo(UPDATED_CENTRE_RC);
        assertThat(testOperateur.getNumeroRc()).isEqualTo(UPDATED_NUMERO_RC);
    }

    @Test
    @Transactional
    void patchNonExistingOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operateur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperateur() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        int databaseSizeBeforeDelete = operateurRepository.findAll().size();

        // Delete the operateur
        restOperateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, operateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
