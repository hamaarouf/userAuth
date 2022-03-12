package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Utilisateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    @Query(value = "select * from utilisateur where nom='james'", nativeQuery = true)
    Page<Utilisateur> findAllJames(Pageable pageable);
}
