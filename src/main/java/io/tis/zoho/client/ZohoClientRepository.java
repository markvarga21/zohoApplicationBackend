package io.tis.zoho.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZohoClientRepository extends JpaRepository<ZohoClient, String> {
}
