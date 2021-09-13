ALTER TABLE IF EXISTS ONLY public.product
    DROP CONSTRAINT IF EXISTS fk_product_category_id CASCADE;
    
ALTER TABLE IF EXISTS ONLY public.product
    DROP CONSTRAINT IF EXISTS fk_supplier_id CASCADE;

ALTER TABLE IF EXISTS ONLY public.shopping_cart
    DROP CONSTRAINT IF EXISTS fk_product_id CASCADE;


DROP TABLE IF EXISTS public.user_account;
CREATE TABLE public.user_account (
    id serial NOT NULL PRIMARY KEY,
    name text NOT NULL,
    email text NOT NULL,
    user_password text NOT NULL
);


DROP TABLE IF EXISTS public.product_category;
CREATE TABLE public.product_category (
    id serial NOT NULL PRIMARY KEY,
    name text NOT NULL,
    description text NOT NULL,
    department text NOT NULL
);


DROP TABLE IF EXISTS public.supplier;
CREATE TABLE public.supplier (
    id serial NOT NULL PRIMARY KEY,
    name text NOT NULL,
    description text NOT NULL
);


DROP TABLE IF EXISTS public.product;
CREATE TABLE public.product (
    id serial NOT NULL PRIMARY KEY,
    name text NOT NULL,
    description text NOT NULL,
    default_price float NOT NULL,
    default_currency text NOT NULL,
    product_category_id integer NOT NULL,
    supplier_id integer NOT NULL
);


DROP TABLE IF EXISTS public.shopping_cart;
CREATE TABLE public.shopping_cart (
    id serial NOT NULL PRIMARY KEY,
    product_id integer NOT NULL,
    quantity integer NOT NULL,
    total_price float NOT NULL
);


DROP TABLE IF EXISTS public.checkout;
CREATE TABLE public.checkout (
    id serial NOT NULL PRIMARY KEY,
    used_id integer NOT NULL,
    checkout_property text NOT NULL,
    checkout_value text NOT NULL
);


ALTER TABLE ONLY public.product
    ADD CONSTRAINT fk_product_category_id FOREIGN KEY (product_category_id) REFERENCES public.product_category(id);
    
ALTER TABLE ONLY public.product
    ADD CONSTRAINT fk_supplier_id FOREIGN KEY (supplier_id) REFERENCES public.supplier(id);

ALTER TABLE ONLY public.shopping_cart
    ADD CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES public.product(id);
