package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Operateur.
 */
@Entity
@Table(name = "operateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Operateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "centre_rc")
    private String centreRc;

    @Column(name = "numero_rc")
    private String numeroRc;

    @OneToMany(mappedBy = "operateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "operateur" }, allowSetters = true)
    private Set<Utilisateur> utilisateurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Operateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCentreRc() {
        return this.centreRc;
    }

    public Operateur centreRc(String centreRc) {
        this.setCentreRc(centreRc);
        return this;
    }

    public void setCentreRc(String centreRc) {
        this.centreRc = centreRc;
    }

    public String getNumeroRc() {
        return this.numeroRc;
    }

    public Operateur numeroRc(String numeroRc) {
        this.setNumeroRc(numeroRc);
        return this;
    }

    public void setNumeroRc(String numeroRc) {
        this.numeroRc = numeroRc;
    }

    public Set<Utilisateur> getUtilisateurs() {
        return this.utilisateurs;
    }

    public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
        if (this.utilisateurs != null) {
            this.utilisateurs.forEach(i -> i.setOperateur(null));
        }
        if (utilisateurs != null) {
            utilisateurs.forEach(i -> i.setOperateur(this));
        }
        this.utilisateurs = utilisateurs;
    }

    public Operateur utilisateurs(Set<Utilisateur> utilisateurs) {
        this.setUtilisateurs(utilisateurs);
        return this;
    }

    public Operateur addUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.add(utilisateur);
        utilisateur.setOperateur(this);
        return this;
    }

    public Operateur removeUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.remove(utilisateur);
        utilisateur.setOperateur(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operateur)) {
            return false;
        }
        return id != null && id.equals(((Operateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Operateur{" +
            "id=" + getId() +
            ", centreRc='" + getCentreRc() + "'" +
            ", numeroRc='" + getNumeroRc() + "'" +
            "}";
    }
}
