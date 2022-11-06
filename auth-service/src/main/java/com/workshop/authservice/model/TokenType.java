package com.workshop.authservice.model;

import lombok.Getter;

import javax.persistence.AttributeConverter;
import java.util.stream.Stream;

@Getter
public enum TokenType {

    REFRESH(0, "REFRESH"),
    ACCESS(1, "ACCESS");

    private final int id;
    private final String name;

    TokenType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    static class TokenConverter implements AttributeConverter<TokenType,Integer> {

        @Override
        public Integer convertToDatabaseColumn(TokenType attribute) {
            return attribute.getId();
        }

        @Override
        public TokenType convertToEntityAttribute(Integer dbData) {
            if (dbData == null || dbData < 0) return null;

            return Stream.of(TokenType.values())
                    .filter(s -> s.getId() == dbData)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
}
