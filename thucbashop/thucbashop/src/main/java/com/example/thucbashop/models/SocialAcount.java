package com.example.thucbashop.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_account")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialAcount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider", nullable = false ,length = 20)//ko dc phép null
    private String provider;
    @Column(name = "provider_id", nullable = false,length = 20)//ko dc phép null
    private String provider_id;

    @Column(name = "name", nullable = false ,length = 20)//ko dc phép null
    private String name;
    @Column(name = "email", nullable = false ,length = 20)//ko dc phép null
    private String email;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
