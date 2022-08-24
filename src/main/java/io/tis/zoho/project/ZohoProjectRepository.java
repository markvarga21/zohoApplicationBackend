package io.tis.zoho.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZohoProjectRepository extends JpaRepository<ZohoProject, String> {
    Optional<List<ZohoProject>> getZohoProjectsByClientName(String clientName);
}
