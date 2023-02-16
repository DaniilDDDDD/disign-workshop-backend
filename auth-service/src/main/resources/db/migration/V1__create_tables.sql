SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;


ALTER SCHEMA main OWNER TO workshop;


COMMENT ON SCHEMA main IS 'Main schema for workshop app';


CREATE SEQUENCE main.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE main.hibernate_sequence OWNER TO workshop;

SET default_tablespace = '';

SET default_table_access_method = heap;


CREATE TABLE main.role (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE main.role OWNER TO workshop;


CREATE TABLE main.token (
    id bigint NOT NULL,
    expiration_date timestamp without time zone,
    type integer,
    value character varying(255),
    owner bigint NOT NULL
);


ALTER TABLE main.token OWNER TO workshop;


CREATE TABLE main."user" (
    id bigint NOT NULL,
    avatar character varying(255),
    biography character varying(255),
    email character varying(255) NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    login_source integer,
    password character varying(255),
    status integer,
    username character varying(255) NOT NULL
);


ALTER TABLE main."user" OWNER TO workshop;


CREATE TABLE main.user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE main.user_role OWNER TO workshop;


ALTER TABLE ONLY main.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


ALTER TABLE ONLY main.token
    ADD CONSTRAINT token_pkey PRIMARY KEY (id);


ALTER TABLE ONLY main.token
    ADD CONSTRAINT uk_1ijv0rgb0nkfb1suvfxcewa0y UNIQUE (value);


ALTER TABLE ONLY main.role
    ADD CONSTRAINT uk_8sewwnpamngi6b1dwaa88askk UNIQUE (name);


ALTER TABLE ONLY main."user"
    ADD CONSTRAINT uk_ob8kqyqqgmefl0aco34akdtpe UNIQUE (email);


ALTER TABLE ONLY main."user"
    ADD CONSTRAINT uk_sb8bbouer5wak8vyiiy4pf2bx UNIQUE (username);


ALTER TABLE ONLY main."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


ALTER TABLE ONLY main.user_role
    ADD CONSTRAINT fka68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES main.role(id);


ALTER TABLE ONLY main.user_role
    ADD CONSTRAINT fkfgsgxvihks805qcq8sq26ab7c FOREIGN KEY (user_id) REFERENCES main."user"(id);


ALTER TABLE ONLY main.token
    ADD CONSTRAINT fkiqyl6yyijy2pd0y9h4beyfpju FOREIGN KEY (owner) REFERENCES main."user"(id);
