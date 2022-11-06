package com.workshop.authservice.model;

import lombok.Getter;

import javax.persistence.AttributeConverter;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum LoginSource {

    LOCAL(0, "local"),
    GOOGLE(1, "google");

    private final int id;
    private final String name;

    LoginSource(int id, String name) {
        this.id = id;
        this.name = name;
    }

    static class LoginSourceConverter implements AttributeConverter<LoginSource,Integer> {
        @Override
        public Integer convertToDatabaseColumn(LoginSource attribute) {
            return attribute.getId();
        }

        @Override
        public LoginSource convertToEntityAttribute(Integer dbData) {
            if (dbData == null || dbData < 0) return null;

            return Stream.of(LoginSource.values())
                    .filter(s -> s.getId() == dbData)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    };

    public static LoginSource getByName(String name) throws IllegalArgumentException{
        if (name != null)
            for (LoginSource source : LoginSource.values())
                if (source.getName().equals(name))
                    return source;
        throw new IllegalArgumentException("No LoginSource with provided name: " + name);
    }
}
