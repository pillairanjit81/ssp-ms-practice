delete from stores;
delete from products;
delete from stocks;
delete from products_stores;

insert into stores (store_id, name) values (1, 'test_Store1');
insert into stores (store_id, name) values (2, 'test_Store2');
insert into stores (store_id, name) values (3, 'test_Store3');

insert into products (product_id, name, description, sku, price) values(1, 'Desk', 'Desk', 'SKU001', 1.19);
insert into products (product_id, name, description, sku, price) values(2, 'Chair', 'Chair', 'SKU002', 2.99);
insert into products (product_id, name, description, sku, price) values(3, 'Cabinet', 'Cabinet', 'SKU003', 2.30);

insert into products_stores(product_id, store_id) values (1, 1);

insert into stocks (stock_id, store_id, product_id, count) values(1, 1, 1, 1);
insert into stocks (stock_id, store_id, product_id, count) values(2, 1, 2, 1);
insert into stocks (stock_id, store_id, product_id, count) values(3, 1, 3, 1);
insert into stocks (stock_id, store_id, product_id, count) values(4, 2, 2, 1);
insert into stocks (stock_id, store_id, product_id, count) values(5, 2, 3, 1);
insert into stocks (stock_id, store_id, product_id, count) values(6, 3, 2, 1);
insert into stocks (stock_id, store_id, product_id, count) values(7, 3, 3, 1);
