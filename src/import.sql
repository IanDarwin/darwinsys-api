-- import.sql for trivial JPADemo

insert into Person(id,username, firstname, lastname) values(1, 'ian_d', 'Ian', 'Darwin');
insert into Person(id,username, firstname, lastname) values(2, 'Mikki', 'Mikki', 'Maus');

insert into Recipe(id, owner, title) values(1, 1, 'Getting Started');
insert into Recipe(id, owner, title) values(2, 1, 'Advanced Feechures');