package io.tis.zoho.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZohoJobRepository extends JpaRepository<ZohoJob, String> {
    Optional<List<ZohoJob>> getZohoJobsByClientName(String clientName);
}
