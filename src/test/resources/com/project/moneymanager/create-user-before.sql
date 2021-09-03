delete from users where users.id IS NOT NULL;

insert into users(id,created_at,email,password,updated_at,username) values
(1,'2021-08-26 15:45:25.254000','popovilya57@gmail.com','$2a$10$qO/TManj9ZWUmoN6kYC.Q.nPcHRj0DbEM1io5NKyBQZGsMSkn.bEq','2021-08-26 15:45:25.274000','Illia')
