package br.com.wallace.softplanchallenge.service.data.base.repositories;

import br.com.wallace.softplanchallenge.service.data.base.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
