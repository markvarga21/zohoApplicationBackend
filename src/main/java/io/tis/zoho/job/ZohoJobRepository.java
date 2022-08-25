package io.tis.zoho.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZohoJobRepository extends JpaRepository<ZohoJob, String> {
    Optional<List<ZohoJob>> getZohoJobsByClientName(String clientName);
    @Query("SELECT p.jobId FROM ZohoJob p WHERE p.jobName LIKE :jobName AND p.clientName LIKE :clientName")
    Optional<String> getIdForJobName(@Param("jobName") String jobName, @Param("clientName") String clientName);
}
