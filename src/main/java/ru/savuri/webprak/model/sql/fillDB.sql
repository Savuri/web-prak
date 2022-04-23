\connect web_db;

truncate goods;
truncate ordergoods;
truncate users;
truncate orders;

insert into goods(assembly_place, description, manufacturer, model, price, quantity, "type")
values('Китай', '', '')

insert into ordergoods(purchase_price, purchase_quantity, good_id, order_id)
values

insert into users(address, email, full_name, phone_number)

insert into orders(delivery_place, deliverty_time, status, user_id)
values
