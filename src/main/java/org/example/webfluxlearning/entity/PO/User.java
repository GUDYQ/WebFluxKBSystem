package org.example.webfluxlearning.entity.PO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table("users")
public class User {
    @Id
    private String uuid;
    private String username;
    @Column(value = "password_hash")
    private String passwordHashHex;
    private String email;
    private String avatarUrl;
    private LocalDateTime createdTime;
    private LocalDateTime lastLoginTime;
    private Boolean isActive;

}
