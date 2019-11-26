package br.com.wallace.softplanchallenge.service.data.base.repositories;


import br.com.wallace.softplanchallenge.service.data.base.entities.SexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SexRepository extends JpaRepository<SexEntity, Long> {


}
