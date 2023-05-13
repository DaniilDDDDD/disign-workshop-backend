INSERT INTO main.user (id, email, username, password, status)
VALUES (nextval('main."hibernate_sequence"'),
        'panushind@gmail.com',
        'admin',
        '$2a$12$Z6AghKxOMLeGpj0kzk/5qeUfo1BHQ7AYOgy9OIxe9avt6FzTrf.4W',
        1);

INSERT INTO main.user_role (user_id, role_id)
VALUES ((SELECT id FROM main.user WHERE email = 'panushind@gmail.com'),
        (SELECT id FROM main.role WHERE name = 'ROLE_CUSTOMER')),
       ((SELECT id FROM main.user WHERE email = 'panushind@gmail.com'),
        (SELECT id FROM main.role WHERE name = 'ROLE_AUTHOR')),
       ((SELECT id FROM main.user WHERE email = 'panushind@gmail.com'),
        (SELECT id FROM main.role WHERE name = 'ROLE_EXECUTOR')),
       ((SELECT id FROM main.user WHERE email = 'panushind@gmail.com'),
        (SELECT id FROM main.role WHERE name = 'ROLE_ADMIN')),
       ((SELECT id FROM main.user WHERE email = 'panushind@gmail.com'),
        (SELECT id FROM main.role WHERE name = 'ROLE_DEVELOPER')),
       ((SELECT id FROM main.user WHERE email = 'panushind@gmail.com'),
        (SELECT id FROM main.role WHERE name = 'ROLE_MODERATOR')),
       ((SELECT id FROM main.user WHERE email = 'panushind@gmail.com'),
        (SELECT id FROM main.role WHERE name = 'ROLE_SUPPORT'));