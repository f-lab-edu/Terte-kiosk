CREATE TABLE category (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          description VARCHAR(255),
                          name VARCHAR(255) NOT NULL,
                          store_id BIGINT NOT NULL,
                          PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE choice (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        price INT NOT NULL,
                        menu_option_id BIGINT,
                        PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE menu (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      description VARCHAR(255),
                      image VARCHAR(255),
                      name VARCHAR(255),
                      price INT,
                      store_id BIGINT,
                      category_id BIGINT,
                      PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE menu_option (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             multiple_selection BIT NOT NULL,
                             name VARCHAR(255) NOT NULL,
                             required BIT NOT NULL,
                             menu_id BIGINT,
                             PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE orders (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        create_time DATETIME(6),
                        order_type VARCHAR(255) NOT NULL,
                        phone_number VARCHAR(255),
                        status VARCHAR(255) NOT NULL,
                        store_id BIGINT NOT NULL,
                        table_number INT,
                        total_price BIGINT,
                        PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE order_item (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            menu_id BIGINT,
                            quantity INT NOT NULL,
                            order_id BIGINT,
                            PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE payment (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         order_id BIGINT,
                         payment_method VARCHAR(255),
                         status VARCHAR(255),
                         store_id BIGINT,
                         total_price BIGINT,
                         PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE selected_option (
                                 id BIGINT NOT NULL AUTO_INCREMENT,
                                 menu_option_id BIGINT,
                                 order_item_id BIGINT,
                                 PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE selected_option_choice_ids (
                                            selected_option_id BIGINT NOT NULL,
                                            selected_choice_ids BIGINT,
                                            PRIMARY KEY (selected_option_id, selected_choice_ids)
) ENGINE=InnoDB;

ALTER TABLE choice
    ADD CONSTRAINT FK_choice_menu_option
        FOREIGN KEY (menu_option_id) REFERENCES menu_option (id);

ALTER TABLE menu
    ADD CONSTRAINT FK_menu_category
        FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE menu_option
    ADD CONSTRAINT FK_menu_option_menu
        FOREIGN KEY (menu_id) REFERENCES menu (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_order_item_orders
        FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE selected_option_choice_ids
    ADD CONSTRAINT FK_selected_option_choice
        FOREIGN KEY (selected_option_id) REFERENCES selected_option (id);

ALTER TABLE selected_option
    ADD CONSTRAINT FK_selected_option_order_item
        FOREIGN KEY (order_item_id) REFERENCES order_item (id);


INSERT INTO category (id, name, store_id, description) VALUES
                                                           (1, 'COFFEE', 101, '커피 메뉴'),
                                                           (2, '빙수', 101, '맛있는 빙수'),
                                                           (3, '디저트', 101, '달콤한 케이크'),
                                                              (4, '티', 101, '차');
INSERT INTO menu (id, name, price, category_id, store_id, image, description) VALUES
                                                                                  (1, '아메리카노', 5000, 1, 101, 'image.jpg', '블렌딩 원두를 사용한 아메리카노'),
                                                                                  (2, '팥빙수', 10000, 2, 101, 'image2.jpg', '맛있는 팥빙수'),
                                                                                  (3, '카페라떼', 6000, 1, 101, 'image3.jpg', '라떼');
INSERT INTO menu_option (id, name, multiple_selection, required, menu_id) VALUES
                                                                             (1, '얼음 옵션', true, false, 2),
                                                                             (2, '토핑 옵션', true, false, 2),
                                                                             (3, '시럽 옵션', true, false, 1),
                                                                             (4, '크기 옵션', false, true, 3),
                                                                             (5, '삭제 될 옵션', true, false, 3);
INSERT INTO choice (id, name, price, menu_option_id) VALUES
                                                         (1, '물얼음', 0, 1),
                                                         (2, '우유 얼음', 500, 1),
                                                         (3, '과일 추가', 2000, 2),
                                                         (4, '아이스크림 추가', 2000, 2),
                                                         (5, '톨 사이즈', 0, 4),
                                                         (6, '그란데 사이즈', 500, 4),
                                                         (7, '벤티 사이즈', 1000, 4);
-- orders
INSERT INTO orders (id, store_id, status, total_price, order_type, phone_number, table_number, create_time) VALUES
                                                                                                                (1, 101, 'ORDERED', 20000, 'EATIN', '123-456-7890', 5, NOW()),
                                                                                                                (2, 101, 'COMPLETED', 10000, 'EATIN', NULL, NULL, NOW()),
                                                                                                                (3, 101, 'ORDERED', 6000, 'EATIN', '010-3333-3333', 8, NOW());

-- order_item
INSERT INTO order_item (id, menu_id, quantity, order_id) VALUES
                                                             (1, 1, 2, 1),   -- 아메리카노 2잔 (5,000 x 2 = 10,000)
                                                             (2, 2, 1, 1),   -- 팥빙수 1개 (10,000)
                                                             (3, 2, 1, 2),   -- 팥빙수 1개 (10,000)
                                                             (4, 3, 1, 3);   -- 카페라떼 1잔 (6,000)

-- selected_option
INSERT INTO selected_option (id, menu_option_id, order_item_id) VALUES
                                                                    (1, 1, 1),
                                                                    (2, 2, 2),
                                                                    (3, 2, 3),
                                                                    (4, 4, 4);

-- selected_option_choice_ids
INSERT INTO selected_option_choice_ids (selected_option_id, selected_choice_ids) VALUES
                                                                                     (1, 1),
                                                                                     (2, 2),
                                                                                     (3, 3),
                                                                                     (4, 5);

-- payment
INSERT INTO payment (id, store_id, payment_method, status, order_id, total_price) VALUES
                                                                                      (1, 101, 'CREDIT_CARD', 'PAYMENT_COMPLETED', 1, 20000),  -- 아메리카노+팥빙수 주문
                                                                                      (2, 101, 'CASH', 'PAYMENT_COMPLETED', 2, 10000);       -- 팥빙수 주문




