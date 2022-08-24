package io.tis.zoho.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZohoProjectRepository extends JpaRepository<ZohoProject, String> {
    Optional<List<ZohoProject>> getZohoProjectsByClientName(String clientName);
    @Query("SELECT p.projectId FROM ZohoProject p WHERE p.projectName LIKE :projectName AND p.clientName LIKE :clientName")
    Optional<String> getIdForProjectAndClientName(@Param("projectName") String projectName, @Param("clientName") String clientName);
}
