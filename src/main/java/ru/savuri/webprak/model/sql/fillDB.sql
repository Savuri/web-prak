\connect web_db;

truncate ordergoods cascade;
truncate goods cascade;
truncate users cascade;
truncate orders cascade;

insert into goods(good_id, assembly_place, description, manufacturer, model, price, quantity, "type")
values (1, 'Китай', E'ШхВхГ	59.50х173.70х65.50 с
Общий объем	261 л
Размораживание	No Frost
Класс энергопотребления	A+
Объем холодильной камеры	182 л
Объем морозильной камеры	79 л
Тип компрессора	инверторный
Мощность замораживания	9.3 кг/сут
Режимы	суперзаморозка
Особенности конструкции	перевешиваемые двери, дисплей
Дополнительные функции	индикация температуры, защита от детей', 'LG', 'GA-B379SQUL', 46687, 2, 'KITCHEN');


insert into users(user_id, address, email, full_name, phone_number)
values (2, 'г. Москва ул. Пушкина дом 22 кв 33', 'EgorIvanov@mail.ru', 'Egor Ivanov Ivanovich', '+79556834566');

insert into orders(order_id, delivery_place, delivery_time, status, user_id)
values (3, 'г. Москва ул. Пушкина дом 22 кв 33', '2020-01-08 04:05:06', 'DELIVERED', 2);

insert into ordergoods(order_good_id, purchase_price, purchase_quantity, good_id, order_id)
values (4, 46000, 1, 1, 3);
