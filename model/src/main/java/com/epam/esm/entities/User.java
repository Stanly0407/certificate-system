package com.epam.esm.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@javax.persistence.Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class User extends Entity {

    @Column
    private String login;

    @Column
    @JsonIgnore
    private String password;

    @Column
    private String name;

    @Column
    private String lastname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Order> orders;

    @Builder
    public User(Long id, String login, String password, String name, String lastname, List<Order> orders) {
        super(id);
        this.login = login;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.orders = orders;
    }
}
