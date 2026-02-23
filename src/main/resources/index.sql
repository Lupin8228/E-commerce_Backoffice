/*
 CUSTOMER INDEXES
 */
DROP INDEX idx_customer_status_deleted ON customers;
CREATE INDEX idx_customer_status_deleted
    ON customers (status, deleted);

/*
 ORDER INDEXES
 */
DROP INDEX idx_orders_status_created ON orders;
DROP INDEX idx_orders_status_created_price ON orders;
DROP INDEX idx_orders_deleted_created_desc ON orders;
CREATE INDEX idx_orders_status_created
    ON orders (status, created_at);

CREATE INDEX idx_orders_status_created_price
    ON orders (status, created_at, total_price);

CREATE INDEX idx_orders_deleted_created_desc
    ON orders (deleted, created_at DESC);

/*
 PRODUCT INDEXES
 */
DROP INDEX idx_product_deleted_stock ON products;
DROP INDEX idx_product_deleted_category ON products;
CREATE INDEX idx_product_deleted_stock
    ON products (stock, deleted);

CREATE INDEX idx_product_deleted_category
    ON products (category, deleted);

/*
 REVIEW INDEXES
 */
DROP INDEX idx_review_deleted_rating ON reviews;
CREATE INDEX idx_review_deleted_rating
    ON reviews (deleted, rating);