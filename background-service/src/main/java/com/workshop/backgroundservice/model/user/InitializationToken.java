package com.workshop.backgroundservice.model.user;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "initialization_token")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitializationToken {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(name = "value", unique = true)
    private String value;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private User user;

}
