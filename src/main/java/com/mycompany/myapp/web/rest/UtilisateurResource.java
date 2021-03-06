package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Operateur;
import com.mycompany.myapp.domain.Utilisateur;
import com.mycompany.myapp.repository.UtilisateurRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Utilisateur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UtilisateurResource {

    private final Logger log = LoggerFactory.getLogger(UtilisateurResource.class);

    private static final String ENTITY_NAME = "utilisateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurResource(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * {@code POST  /utilisateurs} : Create a new utilisateur.
     *
     * @param utilisateur the utilisateur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new utilisateur, or with status {@code 400 (Bad Request)} if the utilisateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/utilisateurs")
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) throws URISyntaxException {
        log.debug("REST request to save Utilisateur : {}", utilisateur);
        if (utilisateur.getId() != null) {
            throw new BadRequestAlertException("A new utilisateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Utilisateur result = utilisateurRepository.save(utilisateur);
        return ResponseEntity
            .created(new URI("/api/utilisateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /utilisateurs/:id} : Updates an existing utilisateur.
     *
     * @param id the id of the utilisateur to save.
     * @param utilisateur the utilisateur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilisateur,
     * or with status {@code 400 (Bad Request)} if the utilisateur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the utilisateur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Utilisateur utilisateur
    ) throws URISyntaxException {
        log.debug("REST request to update Utilisateur : {}, {}", id, utilisateur);
        if (utilisateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Utilisateur result = utilisateurRepository.save(utilisateur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilisateur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /utilisateurs/:id} : Partial updates given fields of an existing utilisateur, field will ignore if it is null
     *
     * @param id the id of the utilisateur to save.
     * @param utilisateur the utilisateur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilisateur,
     * or with status {@code 400 (Bad Request)} if the utilisateur is not valid,
     * or with status {@code 404 (Not Found)} if the utilisateur is not found,
     * or with status {@code 500 (Internal Server Error)} if the utilisateur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/utilisateurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Utilisateur> partialUpdateUtilisateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Utilisateur utilisateur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Utilisateur partially : {}, {}", id, utilisateur);
        if (utilisateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Utilisateur> result = utilisateurRepository
            .findById(utilisateur.getId())
            .map(existingUtilisateur -> {
                if (utilisateur.getNomUtilisateur() != null) {
                    existingUtilisateur.setNomUtilisateur(utilisateur.getNomUtilisateur());
                }
                if (utilisateur.getPrenom() != null) {
                    existingUtilisateur.setPrenom(utilisateur.getPrenom());
                }
                if (utilisateur.getNom() != null) {
                    existingUtilisateur.setNom(utilisateur.getNom());
                }
                if (utilisateur.getDateInscription() != null) {
                    existingUtilisateur.setDateInscription(utilisateur.getDateInscription());
                }
                if (utilisateur.getPassword() != null) {
                    existingUtilisateur.setPassword(utilisateur.getPassword());
                }

                return existingUtilisateur;
            })
            .map(utilisateurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilisateur.getId().toString())
        );
    }

    /**
     * {@code GET  /utilisateurs} : get all the utilisateurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of utilisateurs in body.
     */
    @GetMapping("/utilisateurs")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Utilisateurs");
        Page<Utilisateur> page = utilisateurRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /utilisateurs/:id} : get the "id" utilisateur.
     *
     * @param id the id of the utilisateur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the utilisateur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable Long id) {
        log.debug("REST request to get Utilisateur : {}", id);
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(utilisateur);
    }

    /**
     * {@code DELETE  /utilisateurs/:id} : delete the "id" utilisateur.
     *
     * @param id the id of the utilisateur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/utilisateurs/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        log.debug("REST request to delete Utilisateur : {}", id);
        utilisateurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /utilisateurCalledJames} : get all the utilisateurs Called James.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of utilisateurs in body.
     */
    @GetMapping("/utilisateurCalledJames")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateursCalledJames(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Utilisateurs");
        Page<Utilisateur> page = utilisateurRepository.findAllJames(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/utilisateursOperateurs")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateursCalledOperateurs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Utilisateurs");
        Page<Utilisateur> page = utilisateurRepository.findAllOperateurs(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    /*  @GetMapping("/utilisateursOperateurs")
    public ResponseEntity<List<Object[]>> getAllUtilisateursCalledOperateurs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Utilisateurs Operateurs");
        Page<Object[]> page = utilisateurRepository.findAllOperateurs(pageable);
        for(Object[] obj : page){
            Utilisateur utilisateur = (Utilisateur) obj[0];
            String centre_rc = (String) obj[1];
        }
        
        //Page<Object[]> page = utilisateurRepository.findAllOperateurs(pageable);
        //Page<Utilisateur> utilisateurs = (Page<Utilisateur>) new ArrayList<Utilisateur>();
       // for(Object[] obj : page){

            
            /*Utilisateur utilisateur = new Utilisateur();
            Operateur operateur = new Operateur();
            //utilisateur.setId((BigInteger)obj[0]);
            utilisateur.setNomUtilisateur((String) obj[1]);
            utilisateur.setPrenom((String) obj[2]);
            utilisateur.setNom((String) obj[3]);
           // utilisateur.setDateInscription((LocalDate) obj[4]);

           // operateur.setId((Long) obj[6]);
            operateur.setCentreRc((String) obj[7]);
            operateur.setNumeroRc((String) obj[7]);
            utilisateur.setOperateur(operateur);
            utilisateurs.add(utilisateur);*/
    //String centre_rc = (String) obj[];
    // }
    //Page<Utilisateur> pagee = (Page<Utilisateur>) utilisateurs;

    // HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    //   return ResponseEntity.ok().headers(headers).body(page.getContent());
    // }

}
