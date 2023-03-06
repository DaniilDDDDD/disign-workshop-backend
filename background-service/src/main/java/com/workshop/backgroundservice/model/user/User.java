package com.workshop.backgroundservice.model.user;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(name = "status")
    @Convert(converter = Status.StatusConverter.class)
    private Status status;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

}
