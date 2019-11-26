package br.com.wallace.softplanchallenge.gateway.data.base.repositories;

import br.com.wallace.softplanchallenge.gateway.data.base.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findFirstByDescription(String description);
}
