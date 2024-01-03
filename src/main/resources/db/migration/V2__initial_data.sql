insert into user_role (role_name)
values ('GUEST'),
       ('STUDENT'),
       ('TEACHER'),
       ('GLOBAL_ADMIN'),
       ('UNIVERSITY_ADMIN');

insert into university (title, domain)
values ('National Technical University of Ukraine "Igor Sikorsky Kyiv Polytechnic Institute"', 'kpi.ua'),
       ('Taras Shevchenko National University of Kyiv', 'univ.kiev.ua'),
       ('Lviv Polytechnic National University', 'lpnu.ua'),
       ('Kharkiv Polytechnic Institute', 'khpi.ua'),
       ('Odessa National Polytechnic University', 'opu.ua');

insert into user_credentials (email, password, role_id)
values ('student1kpi@kpi.ua', '$2a$10$G.zFDgVn.PBnE146DeFqZea0Fxzfo.agIMEIM1h/BLKyP7ZLffPEG', 2),
       ('teacher1kpi@kpi.ua', '$2a$10$0IpMIMunjDeB0dxgQ.msX.Nbf24XfuL.mH9llPO5bjlx57H8CihKW', 3),
       ('admin1kpi@kpi.ua', '$2a$10$F8GPagO2KGjdK0vMWqSVTeGztv06CzZpqpS96m8wpqU9Acd3Kx7Aq', 5),
       ('globaladmin1@library.ua', '$2a$10$09g0rK18EKvryB40dOZhGOjGEZZaZNGsDApzibOoZt8QF6i9TFWSG', 4);

insert into university_user(user_name, space, university_id, user_credentials_id)
values ('student1_name', 1024, 1, 1),
       ('teacher1_name', null, 1, 2),
       ('university_admin1_name', null, 1, 3),
       ('global_admin1_name', null, null, 4);

insert into library (title, topic, library_access, university_id)
values ('Teacher1_library1', 'Science', true, 1),
       ('Teacher1_library2', 'Math', false, 1),
       ('Student1_library1', 'History', false, 1);

insert into documents (title, topic, description, file_path, owner_id)
values ('Document 1', 'Science', 'Description for Document 1', '/path/to/document1.pdf', 1),
       ('Document 2', 'Science', 'Description for Document 2', '/path/to/document2.pdf', 2),
       ('Document 3', 'Science', 'Description for Document 3', '/path/to/document3.pdf', 3),
       ('Document 4', 'Math Science', 'Description for Document 4', '/path/to/document4.pdf', 1),
       ('Document 5', 'Math', 'Description for Document 5', '/path/to/document5.pdf', 2),
       ('Document 6', 'History', 'Description for Document 6', '/path/to/document6.pdf', 3);

insert into library_documents(library_id, document_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 4),
       (2, 5),
       (3, 6);

insert into user_library_owners (user_id, library_id)
values (2, 1),
       (2, 2),
       (1, 3);

insert into user_library_subscribers (user_id, library_id)
values (1, 2),
       (2, 1),
       (3, 3);


