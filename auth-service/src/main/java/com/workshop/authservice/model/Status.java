package com.workshop.authservice.model;

import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Getter
public enum Status {

    // Do not change early initialized enumerated values!!!

    INITIALIZED(0, "INITIALIZED"), // user created account but did not confirm it
    ACTIVE(1, "ACTIVE"), // user created account and confirmed it
    DISABLED(2, "DISABLED"); // user is no more valid cause of some reason

    private final int id;
    private final String name;

    Status(int id, String name) {
        this.id = id;
        this.name = name;
    }

    static class StatusConverter implements AttributeConverter <Status,Integer> {
        @Override
        public Integer convertToDatabaseColumn(Status attribute) {
            return attribute.getId();
        }

        @Override
        public Status convertToEntityAttribute(Integer dbData) {
            if (dbData == null || dbData < 0) return null;

            return Stream.of(Status.values())
                    .filter(s -> s.getId() == dbData)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    };

}
