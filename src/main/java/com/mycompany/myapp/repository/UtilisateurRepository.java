package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Utilisateur;
import java.util.List;
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

    @Query(
        value = "select u.*, o.centre_rc as centre_rc from utilisateur u inner join operateur o on o.id=u.operateur_id",
        nativeQuery = true
    )
    Page<Utilisateur> findAllOperateurs(Pageable pageable);
    /* 
   Page<Object[]> page = utilisateurRepository.findAllOperateurs(pageable);
        for(Object[] obj : page){
            Utilisateur utilisateur = (Utilisateur) obj[0];
            String centre_rc = (String) obj[1];
        }
    */
}
