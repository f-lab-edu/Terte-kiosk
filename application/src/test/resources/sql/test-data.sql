
INSERT INTO category (id, name, store_id, description) VALUES
                                                           (1, 'COFFEE', 101, '커피 메뉴'),
                                                           (2, '빙수', 101, '맛있는 빙수');
INSERT INTO menu (id, name, price, category_id, store_id, image, description) VALUES
                                                                                  (1, '아메리카노', 5000, 1, 101, 'image.jpg', '블렌딩 원두를 사용한 아메리카노'),
                                                                                  (2, '팥빙수', 10000, 2, 101, 'image2.jpg', '맛있는 팥빙수'),
                                                                                  (3, '카페라떼', 6000, 1, 101, 'image3.jpg', '라떼');
INSERT INTO menu_option (id, name, multiple_selection, required, menu_id) VALUES
                                                                             (1, '얼음 옵션', true, false, 2),
                                                                             (2, '토핑 옵션', true, false, 2),
                                                                             (3, '시럽 옵션', true, false, 1),
                                                                             (4, '크기 옵션', false, true, 3);
INSERT INTO choice (id, name, price, menu_option_id) VALUES
                                                         (1, '물얼음', 0, 1),
                                                         (2, '우유 얼음', 500, 1),
                                                         (3, '과일 추가', 2000, 2),
                                                         (4, '아이스크림 추가', 2000, 2),
                                                         (5, '톨 사이즈', 0, 4),
                                                         (6, '그란데 사이즈', 500, 4);
INSERT INTO orders (id, store_id, status, total_price, order_type, phone_number, table_number, create_time) VALUES
                                                                                       (1, 101, 'ORDERED',12000, 'EATIN' ,'123-456-7890', 5, NOW()),
                                                                                       (2, 101, 'COMPLETED',10000, 'EATIN' ,NULL, NULL, NOW());
INSERT INTO order_item  (id, menu_id, quantity, order_id) VALUES
                                                            (1, 1, 2, 1),
                                                            (2, 2, 1, 1),
                                                            (3, 2, 1, 2);
INSERT INTO selected_option (id, menu_option_id, order_item_id) VALUES
                                                                   (1, 1, 1),
                                                                   (2, 2, 2),
                                                                   (3, 2, 3);
INSERT INTO selected_option_choice_ids (selected_option_id, selected_choice_ids) VALUES
                                                                                              (1, 1),
                                                                                              (2, 2),
                                                                                              (3, 3);
INSERT INTO payment (id, store_id, payment_method, status, order_id, total_price) VALUES
                                                                         (1, 101, 'CREDIT_CARD', 'PAYMENT_COMPLETED', 2, 12000),
                                                                         (2, 101, 'CASH', 'PAYMENT_COMPLETED', 1, 10000);




