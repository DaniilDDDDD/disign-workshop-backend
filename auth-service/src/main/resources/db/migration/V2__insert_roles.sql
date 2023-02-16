INSERT INTO main.role (id, "name")
VALUES (nextval('main."hibernate_sequence"'), 'ROLE_CUSTOMER'),
       (nextval('main."hibernate_sequence"'), 'ROLE_AUTHOR'),
       (nextval('main."hibernate_sequence"'), 'ROLE_EXECUTOR'),
       (nextval('main."hibernate_sequence"'), 'ROLE_ADMIN'),
       (nextval('main."hibernate_sequence"'), 'ROLE_DEVELOPER'),
       (nextval('main."hibernate_sequence"'), 'ROLE_MODERATOR'),
       (nextval('main."hibernate_sequence"'), 'ROLE_SUPPORT');