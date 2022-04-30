\connect web_db;

truncate ordergoods cascade;
truncate goods cascade;
truncate users cascade;
truncate orders cascade;

insert into goods(good_id, assembly_place, description, manufacturer, model, price, quantity, "type") values
(1, 'Китай', E'ШхВхГ	59.50х173.70х65.50 с
Общий объем	261 л
Размораживание	No Frost
Класс энергопотребления	A+
Объем холодильной камеры	182 л
Объем морозильной камеры	79 л
Тип компрессора	инверторный
Мощность замораживания	9.3 кг/сут
Режимы	суперзаморозка
Особенности конструкции	перевешиваемые двери, дисплей
Дополнительные функции	индикация температуры, защита от детей', 'LG', 'GA-B379SQUL', 46687, 2, 'KITCHEN'),
(2, 'Китай', 'Диагональ	86"
Разрешение HD	4K UHD
Частота обновления экрана	100 Гц
Форматы HDR	Dolby Vision, HDR 10 Pro
Технология экрана	HDR, NanoCell, QNED
Разъемы и интерфейсы	вход аудио коаксиальный, выход на наушники, Ethernet - RJ-45, USB Type-A x 3, вход HDMI x 4, слот CI/CI+, выход аудио оптический
Беспроводная связь	Miracast, Airplay, Wi-Fi, Bluetooth
Год создания модели	2021
Платформа Smart TV	webOS
Экосистема умного дома	Apple HomeKit, LG Smart ThinQ
Разрешение	3840x2160
Тип матрицы экрана	IPS
Тип подсветки	mini-LED
Мощность звука	40 Вт', 'LG', '86QNED916PA NanoCell, HDR, QNED (2021), черный/серый', 399990, 22, 'TV'),
(3, 'Россия', 'Линейка процессора	Intel Core i5
Частота процессора	3000 МГц
Объем оперативной памяти	8 ГБ
Видеокарта	Intel UHD Graphics 630
Общий объем накопителей SSD	512
Операционная система	Windows 10 Pro
Беспроводные интерфейсы	нет
Игровой компьютер', 'ASUS', 'D540MC-I58500004R MT i5 8500 (3) 8Gb SSD512Gb UHDG 630 Win 10 Pro', 74990, 12, 'COMPUTER');


insert into users(user_id, address, email, full_name, phone_number) values
(4, 'г. Москва ул. Пушкина дом 22 кв 33', 'EgorIvanov@mail.ru', 'Egor Ivanov Ivanovich', '+79556834566'),
(5, 'г. Москва ул. Дружко дом 2 кв 3', 'RayXray@mail.ru', 'Andrey Ray Xray', '+79556834333'),
(6, 'г. Москва ул. Лермонтова дом 11 кв 15', 'AntyanEgo@gmail.ru', 'Antyan Ego Vil', '+79123124500');

insert into orders(order_id, delivery_place, delivery_time, status, user_id) values
(7, 'г. Москва ул. Пушкина дом 22 кв 33', '2020-08-08 04:05:00', 'PROCESSING', 4),
(8, 'Самовывоз', '2020-01-08 04:05:00', 'DELIVERED', 4),
(9, 'г. Москва ул. Лермонтова дом 11 кв 15', '2020-09-12 04:05:00', 'SHIPPED', 5);

insert into ordergoods(order_good_id, purchase_price, purchase_quantity, good_id, order_id) values
(10, 46000, 2, 1, 7),
(11, 400000, 1, 2, 7),
(12, 75000, 3, 3, 8),
(13, 46000, 1, 1, 9);
