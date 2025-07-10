package me.BaHeTo0.demoExchange.models.entities;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "exchange_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String username;

    @OneToMany(mappedBy = "id.userId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @MapKey(name = "id.currency")
    private Map<String, Balance> balances = new HashMap<>();


    public User() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Balance> getBalances() {
        return balances;
    }

    public void setBalances(Map<String, Balance> balances) {
        this.balances = balances;
    }

}
