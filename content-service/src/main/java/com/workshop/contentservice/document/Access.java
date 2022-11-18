package com.workshop.contentservice.document;

import lombok.Getter;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
public enum Access {

    PUBLIC(0, "public"),
    PRIVATE(1, "private"),
    BY_LINK(2, "by_link");


    private final int id;
    private final String name;

    Access(int id, String name) {
        this.id = id;
        this.name = name;
    }

    static class AccessConverter implements AttributeConverter<Access,Integer> {
        @Override
        public Integer convertToDatabaseColumn(Access attribute) {
            return attribute.getId();
        }

        @Override
        public Access convertToEntityAttribute(Integer dbData) {
            if (dbData == null || dbData < 0) return null;

            return Stream.of(Access.values())
                    .filter(s -> s.getId() == dbData)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }

    public static Access getByName(String name) throws IllegalArgumentException {
        for (Access access : Access.values())
            if (Objects.equals(access.getName(), name)) return access;
        throw new IllegalArgumentException("Access with provided name does not exist!");
    }

}
