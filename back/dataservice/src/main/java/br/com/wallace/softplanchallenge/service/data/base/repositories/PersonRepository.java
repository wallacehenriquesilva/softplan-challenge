package br.com.wallace.softplanchallenge.service.data.base.repositories;


import br.com.wallace.softplanchallenge.service.data.base.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    @Query(value = "SELECT * FROM persons WHERE user_id = :user_id", nativeQuery = true)
    List<PersonEntity> findAllByUser(@Param("user_id") Long idUser);

}
