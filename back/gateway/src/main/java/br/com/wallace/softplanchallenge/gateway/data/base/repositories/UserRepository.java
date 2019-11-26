package br.com.wallace.softplanchallenge.gateway.data.base.repositories;


import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findFirstByName(String name);

    Optional<UserEntity> findFirstByEmail(String email);

    boolean existsByEmail(String email);
}