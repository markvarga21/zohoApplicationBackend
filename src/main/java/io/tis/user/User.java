package io.tis.user;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ZOHO_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String refreshToken;
}
