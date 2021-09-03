delete from plans where plans.id IS NOT NULL;
delete from categories where categories.id IS NOT NULL;
delete from users where users.id IS NOT NULL;

insert into users(id,created_at,email,password,updated_at,username) values
(1,'2021-08-26 15:45:25.254000','popovilya57@gmail.com','$2a$10$dU0TpZcSKme8XeLmDO/zT.FuoJAqbPArOmQEpXJOV9YwgC3aOkrH2','2021-08-26 15:45:25.274000','Illia');

insert into plans(id,created_at,end_datez,limitz,name,start_datez,updated_at,user_id) values
(1,'2021-08-26 15:50:25.254000','2021-09-01',1000,'August','2021-08-01','2021-08-26 15:50:25.554000',1);

insert into categories(id,created_at,name,updated_at,user_id) values
(1,'2021-08-26 15:50:25.254000','Gifts','2021-08-26 15:50:25.554000',1);

insert into categories(id,created_at,name,updated_at,user_id) values
(2,'2021-08-27 15:50:25.254000','Water','2021-08-27 15:50:25.554000',1);


