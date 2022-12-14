package be.pxl.clubs.repository;

import be.pxl.clubs.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Owner findByNameIgnoreCase(String name);
}