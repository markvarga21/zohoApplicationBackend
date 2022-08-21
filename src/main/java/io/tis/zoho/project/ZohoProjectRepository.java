package io.tis.zoho.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZohoProjectRepository extends JpaRepository<ZohoProject, String> {

}
