package com.workshop.authservice.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "token")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Token implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(name = "value", unique = true)
    private String value;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column
    @Convert(converter = TokenType.TokenConverter.class)
    private TokenType type;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    public Token() {
    }
}
