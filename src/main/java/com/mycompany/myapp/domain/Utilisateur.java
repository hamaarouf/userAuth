package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
//@SecondaryTable(name = "operateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_utilisateur")
    private String nomUtilisateur;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "nom")
    private String nom;

    @Column(name = "date_inscription")
    private LocalDate dateInscription;

    @Column(name = "password")
    private String password;

    @Transient
    // @JsonSerialize
    // @JsonDeserialize
    @JsonProperty
    private String centre_rc;

    /*  public Utilisateur() {
    }

    public Utilisateur(Long id, String nomUtilisateur, String prenom, String nom, LocalDate dateInscription, String password, String centre_rc, Operateur operateur) {
        this.id = id;
        this.nomUtilisateur = nomUtilisateur;
        this.prenom = prenom;
        this.nom = nom;
        this.dateInscription = dateInscription;
        this.password = password;
        this.centre_rc = centre_rc;
        this.operateur = operateur;
    }*/

    public String getCentre_rc() {
        return this.centre_rc;
    }

    public void setCentre_rc(String centre_rc) {
        this.centre_rc = centre_rc;
    }

    @ManyToOne
    @JsonIgnoreProperties(value = { "utilisateurs" }, allowSetters = true)
    private Operateur operateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomUtilisateur() {
        return this.nomUtilisateur;
    }

    public Utilisateur nomUtilisateur(String nomUtilisateur) {
        this.setNomUtilisateur(nomUtilisateur);
        return this;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Utilisateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public Utilisateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateInscription() {
        return this.dateInscription;
    }

    public Utilisateur dateInscription(LocalDate dateInscription) {
        this.setDateInscription(dateInscription);
        return this;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getPassword() {
        return this.password;
    }

    public Utilisateur password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Operateur getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Operateur operateur) {
        this.operateur = operateur;
    }

    public Utilisateur operateur(Operateur operateur) {
        this.setOperateur(operateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return id != null && id.equals(((Utilisateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", nomUtilisateur='" + getNomUtilisateur() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", nom='" + getNom() + "'" +
            ", dateInscription='" + getDateInscription() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
