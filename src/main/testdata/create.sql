
ALTER TABLE catalog_attribute DROP CONSTRAINT catalog_attribute_fk1;
ALTER TABLE catalog_attribute DROP CONSTRAINT catalog_attribute_fk2;
ALTER TABLE catalog_attribute DROP CONSTRAINT catalog_attribute_fk3;

ALTER TABLE attribute_value DROP CONSTRAINT attribute_value_fk1;
ALTER TABLE product DROP CONSTRAINT product_fk1;
ALTER TABLE product DROP CONSTRAINT product_fk2;
ALTER TABLE product DROP CONSTRAINT product_fk3;
ALTER TABLE product DROP CONSTRAINT product_fk4;
ALTER TABLE product DROP CONSTRAINT product_fk5;
ALTER TABLE product DROP CONSTRAINT product_fk6;
ALTER TABLE product DROP CONSTRAINT product_fk7;
ALTER TABLE orders DROP CONSTRAINT order_fk5;
ALTER TABLE orders DROP CONSTRAINT order_fk1;
ALTER TABLE person DROP CONSTRAINT person_fk2;
ALTER TABLE person DROP CONSTRAINT person_fk1;
ALTER TABLE stock DROP CONSTRAINT stock_fk1;
ALTER TABLE stock DROP CONSTRAINT stock_fk2;
ALTER TABLE stock DROP CONSTRAINT stock_fk3;
ALTER TABLE detail DROP CONSTRAINT detail_fk1;
ALTER TABLE order_product DROP CONSTRAINT order_product_fk2;
ALTER TABLE order_product DROP CONSTRAINT order_product_fk1;
ALTER TABLE cart DROP CONSTRAINT cart_fk1;
ALTER TABLE cart_product DROP CONSTRAINT cart_product_fk1;
ALTER TABLE cart_product DROP CONSTRAINT cart_product_fk2;

DROP TABLE order_status;
DROP TABLE catalog_attribute;
DROP TABLE attribute_value;
DROP TABLE catalog;
DROP TABLE product;
DROP TABLE position_type;
DROP TABLE shop;
DROP TABLE orders;
DROP TABLE person;
DROP TABLE stock;
DROP TABLE person_status;
DROP TABLE detail;
DROP TABLE shipment_type;
DROP TABLE attribute;
DROP TABLE order_product;
DROP TABLE cart;
DROP TABLE cart_product;
DROP TABLE tbtest2;


DROP SEQUENCE product_id;
DROP SEQUENCE person_status_id;
DROP SEQUENCE attribute_value_id;
DROP SEQUENCE attribute_id;
DROP SEQUENCE position_type_id;
DROP SEQUENCE stock_id;
DROP SEQUENCE order_status_id;
DROP SEQUENCE catalog_attribute_id;
DROP SEQUENCE shop_id;
DROP SEQUENCE detail_id;
DROP SEQUENCE shipment_type_id;
DROP SEQUENCE person_id;
DROP SEQUENCE orders_id;
DROP SEQUENCE catalog_id;
DROP SEQUENCE order_product_id;
DROP SEQUENCE cart_id;
DROP SEQUENCE cart_product_id;
DROP SEQUENCE tbtest2_id;


CREATE SEQUENCE tbtest2_id;
CREATE TABLE tbtest2(
id INTEGER NOT NULL DEFAULT nextval('tbtest2_id'),
key VARCHAR(255),
total_amount INTEGER,
value integer[],
CONSTRAINT tbtest2_pk PRIMARY KEY(id)
);
CREATE INDEX tbtest2_idx2 ON tbtest2 USING hash (key);

CREATE SEQUENCE product_id;
CREATE TABLE product (
	id INTEGER NOT NULL DEFAULT nextval('product_id'),
	catalog_fk INTEGER NOT NULL,
	name VARCHAR ( 255 ) NOT NULL,
	description VARCHAR ( 255 ),
	image VARCHAR ( 255 ),
	price DECIMAL ( 10, 2 ) NOT NULL,
	brand VARCHAR (25) NOT NULL,
	brand_fk INTEGER NOT NULL,
	resolution VARCHAR (25) ,
	resolution_fk INTEGER ,
	screen VARCHAR (25),
	screen_fk INTEGER,
	"Resp time" VARCHAR (25),
	"Resp time_fk" INTEGER,
	color VARCHAR (25),
	color_fk INTEGER,
	"Battery Life" VARCHAR (25),
	"Battery Life_fk" INTEGER,
	type VARCHAR (25) NOT NULL CHECK (type IN ('monitors','smartphones')),
	CONSTRAINT product_pk PRIMARY KEY (id)
	);
/*
CREATE INDEX product_idx1 ON product USING btree (catalog_fk,brand);
CREATE INDEX product_idx2 ON product USING btree (catalog_fk,resolution );
CREATE INDEX product_idx3 ON product USING btree (catalog_fk,screen );
CREATE INDEX product_idx4 ON product USING btree (catalog_fk,"Resp time" );
CREATE INDEX product_idx5 ON product USING btree (catalog_fk,color );
CREATE INDEX product_idx6 ON product USING btree (catalog_fk,"Battery Life" );
CREATE INDEX cart_product_idx1 ON cart_product (product_fk );
CREATE INDEX cart_product_idx2 ON cart_product (cart_fk );
CREATE INDEX attribute_value_idx1 ON attribute_value (attribute_fk );
CREATE INDEX stock_idx3 ON stock (product_fk );
CREATE INDEX stock_idx2 ON stock (shipment_type_fk );
CREATE INDEX stock_idx1 ON stock (shop_fk );
CREATE INDEX catalog_attribute_idx2 ON catalog_attribute (attribute_fk );
CREATE INDEX catalog_attribute_idx1 ON catalog_attribute (catalog_fk );
CREATE INDEX catalog_attribute_idx3 ON catalog_attribute (attribute_value_fk );
CREATE INDEX detail_idx1 ON detail (product_fk );
CREATE INDEX order_idx1 ON orders (person_fk );
CREATE INDEX order_product_idx1 ON order_product (order_fk );
CREATE INDEX order_product_idx2 ON order_product (product_fk );
CREATE INDEX cart_idx1 ON cart (user_fk );
*/

CREATE SEQUENCE cart_id;
CREATE TABLE cart (
	id INTEGER NOT NULL DEFAULT nextval('cart_id'),
	user_fk INTEGER,
	session_id VARCHAR ( 255 ) NOT NULL,
	CONSTRAINT cart_pk PRIMARY KEY (id)
	);

CREATE SEQUENCE cart_product_id;
CREATE TABLE cart_product (
	id INTEGER NOT NULL DEFAULT nextval('cart_product_id'),
	qty INTEGER NOT NULL,
	price DECIMAL(10,2) NOT NULL,
	product_fk INTEGER NOT NULL,
	cart_fk INTEGER NOT NULL,
	CONSTRAINT cart_product_pk PRIMARY KEY (id)
	);

CREATE SEQUENCE person_status_id;
CREATE TABLE person_status (
	id INTEGER NOT NULL DEFAULT nextval('person_status_id'),
	name VARCHAR ( 255 ) NOT NULL,
	CONSTRAINT person_status_pk PRIMARY KEY (id)
	);

CREATE SEQUENCE attribute_value_id;
CREATE TABLE attribute_value (
	id INTEGER NOT NULL DEFAULT nextval('attribute_value_id'),
	attribute_fk INTEGER NOT NULL,
	attributeValue VARCHAR ( 255 ) NOT NULL,
	orderby INTEGER NOT NULL,
	CONSTRAINT attribute_value_pk PRIMARY KEY (id)
	);


CREATE SEQUENCE attribute_id;
CREATE TABLE attribute (
	id INTEGER NOT NULL DEFAULT nextval('attribute_id'),
	name VARCHAR ( 255 ) NOT NULL,
	CONSTRAINT attribute_pk PRIMARY KEY (id)
	);

CREATE SEQUENCE position_type_id;
CREATE TABLE position_type (
	id INTEGER NOT NULL DEFAULT nextval('position_type_id'),
	name VARCHAR ( 255 ) NOT NULL,
	CONSTRAINT position_type_pk PRIMARY KEY (id)
	);

CREATE SEQUENCE stock_id;
CREATE TABLE stock (
	id INTEGER NOT NULL DEFAULT nextval('stock_id'),
	product_fk INTEGER NOT NULL,
	shop_fk INTEGER NOT NULL,
	shipment_type_fk INTEGER NOT NULL,
	quantity INTEGER NOT NULL,
	CONSTRAINT stock_pk PRIMARY KEY (id)
	);


CREATE SEQUENCE order_status_id;
CREATE TABLE order_status (
	id INTEGER NOT NULL DEFAULT nextval('order_status_id'),
	name VARCHAR ( 255 ) NOT NULL,
	CONSTRAINT order_status_pk PRIMARY KEY (id)
	);


CREATE SEQUENCE catalog_attribute_id;
CREATE TABLE catalog_attribute (
	id INTEGER NOT NULL DEFAULT nextval('catalog_attribute_id'),
	catalog_fk INTEGER NOT NULL,
	attribute_fk INTEGER NOT NULL,
	attribute_value_fk INTEGER NOT NULL,
	refcount INTEGER DEFAULT 0,
	CONSTRAINT catalog_attribute_pk PRIMARY KEY (id)
	);



CREATE SEQUENCE shop_id;
CREATE TABLE shop (
	id INTEGER NOT NULL DEFAULT nextval('shop_id'),
	address VARCHAR ( 255 ) NOT NULL,
	name VARCHAR ( 255 ) NOT NULL,
	CONSTRAINT shop_pk PRIMARY KEY (id)
	);

CREATE SEQUENCE detail_id;
CREATE TABLE detail (
	id INTEGER NOT NULL DEFAULT nextval('detail_id'),
	product_fk INTEGER NOT NULL,
	name VARCHAR ( 255 ) NOT NULL,
	detail_value VARCHAR ( 255 ) NOT NULL,
	CONSTRAINT detail_pk PRIMARY KEY (id)
	);

CREATE SEQUENCE shipment_type_id;
CREATE TABLE shipment_type (
	id INTEGER NOT NULL DEFAULT nextval('shipment_type_id'),
	ship_date VARCHAR ( 255 ) NOT NULL,
	CONSTRAINT shipment_type_pk PRIMARY KEY (id)
	);


CREATE SEQUENCE person_id;
CREATE TABLE person (
	id INTEGER NOT NULL DEFAULT nextval('person_id'),
	person_status_fk INTEGER NOT NULL,
	position_type_fk INTEGER,
	nick VARCHAR ( 255 ) NOT NULL UNIQUE,
	passwd VARCHAR ( 255 ) NOT NULL,
	salt VARCHAR ( 255 ) NOT NULL,
	last_login TIMESTAMP,
	reg_date TIMESTAMP NOT NULL,
	style VARCHAR ( 255 ) NOT NULL,
	person_status_name VARCHAR ( 255 ) NOT NULL,
	address VARCHAR ( 255 ),
	first_name VARCHAR ( 255 ),
	last_name VARCHAR ( 255 ),
	phone VARCHAR ( 255 ),
	email VARCHAR ( 255 ),
	is_client BOOLEAN NOT NULL,
	is_employee BOOLEAN NOT NULL,
	id_nr VARCHAR ( 11 ),
	CONSTRAINT person_pk PRIMARY KEY (id)
	);

CREATE SEQUENCE orders_id;
CREATE TABLE orders (
	id INTEGER NOT NULL DEFAULT nextval('orders_id'),
	person_fk INTEGER,
	order_status_fk INTEGER NOT NULL,
	creation_date TIMESTAMP NOT NULL,
	address VARCHAR ( 255 ) NOT NULL,
	first_name VARCHAR ( 255 ) NOT NULL,
	last_name VARCHAR ( 255 ) NOT NULL,
	phone VARCHAR ( 255 ) NOT NULL,
	email VARCHAR ( 255 ) NOT NULL,
	CONSTRAINT order_pk PRIMARY KEY (id)
	);

CREATE SEQUENCE order_product_id;	
CREATE TABLE order_product (
	id INTEGER NOT NULL DEFAULT nextval('order_product_id'),
	product_fk INTEGER NOT NULL,
	order_fk INTEGER NOT NULL,
	price DECIMAL(10,2) NOT NULL,
	quantity INTEGER NOT NULL,
	CONSTRAINT order_product_pk PRIMARY KEY (id)
	);


CREATE SEQUENCE catalog_id;
CREATE TABLE catalog (
	id INTEGER NOT NULL DEFAULT nextval('catalog_id'),
	title VARCHAR ( 255 ) NOT NULL,
	image VARCHAR ( 255 ) NOT NULL,
	info VARCHAR ( 255 ) NOT NULL,
	longinfo VARCHAR ( 255 ) NOT NULL,
	urlname VARCHAR ( 255 ) NOT NULL,
	catalog_level SMALLINT NOT NULL,
	upper_catalog_fk SMALLINT NOT NULL,
	prod_amount INTEGER DEFAULT 0,
	CONSTRAINT catalog_pk PRIMARY KEY (id)
	);

ALTER TABLE cart ADD CONSTRAINT cart_fk1 FOREIGN KEY (user_fk) REFERENCES person(id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE cart_product ADD CONSTRAINT cart_product_fk1 FOREIGN KEY (cart_fk) REFERENCES cart(id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE cart_product ADD CONSTRAINT cart_product_fk2 FOREIGN KEY (product_fk) REFERENCES product(id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE order_product ADD CONSTRAINT order_product_fk2 FOREIGN KEY (product_fk) REFERENCES product (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE order_product ADD CONSTRAINT order_product_fk1 FOREIGN KEY (order_fk) REFERENCES orders (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE catalog_attribute ADD CONSTRAINT catalog_attribute_fk1 FOREIGN KEY (catalog_fk) REFERENCES catalog (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE catalog_attribute ADD CONSTRAINT catalog_attribute_fk2 FOREIGN KEY (attribute_fk) REFERENCES attribute (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE catalog_attribute ADD CONSTRAINT catalog_attribute_fk3 FOREIGN KEY (attribute_value_fk) REFERENCES attribute_value (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE attribute_value ADD CONSTRAINT attribute_value_fk1 FOREIGN KEY (attribute_fk) REFERENCES attribute (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE product ADD CONSTRAINT product_fk1 FOREIGN KEY (catalog_fk) REFERENCES catalog (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE product ADD CONSTRAINT product_fk2 FOREIGN KEY (brand_fk) REFERENCES attribute_value (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE product ADD CONSTRAINT product_fk3 FOREIGN KEY (resolution_fk) REFERENCES attribute_value (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE product ADD CONSTRAINT product_fk4 FOREIGN KEY (screen_fk ) REFERENCES attribute_value (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE product ADD CONSTRAINT product_fk5 FOREIGN KEY ("Resp time_fk") REFERENCES attribute_value (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE product ADD CONSTRAINT product_fk6 FOREIGN KEY (color_fk) REFERENCES attribute_value (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE product ADD CONSTRAINT product_fk7 FOREIGN KEY ("Battery Life_fk") REFERENCES attribute_value (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE orders ADD CONSTRAINT order_fk5 FOREIGN KEY (order_status_fk) REFERENCES order_status (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE orders ADD CONSTRAINT order_fk1 FOREIGN KEY (person_fk) REFERENCES person (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE person ADD CONSTRAINT person_fk2 FOREIGN KEY (position_type_fk) REFERENCES position_type (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE person ADD CONSTRAINT person_fk1 FOREIGN KEY (person_status_fk) REFERENCES person_status (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE stock ADD CONSTRAINT stock_fk1 FOREIGN KEY (shop_fk) REFERENCES shop (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE stock ADD CONSTRAINT stock_fk2 FOREIGN KEY (product_fk) REFERENCES product (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE stock ADD CONSTRAINT stock_fk3 FOREIGN KEY (shipment_type_fk) REFERENCES shipment_type (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE detail ADD CONSTRAINT detail_fk1 FOREIGN KEY (product_fk) REFERENCES product (id)  ON DELETE NO ACTION ON UPDATE NO ACTION;


