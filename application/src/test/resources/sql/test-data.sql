INSERT INTO Category (id, name, store_id, description) VALUES
                                                           (1, 'COFFEE', 101, '커피 메뉴'),
                                                           (2, '빙수', 101, '맛있는 빙수');
INSERT INTO Menu (id, name, price, category_id, store_id, image, description) VALUES
                                                                                  (1, '아메리카노', 5000, 1, 101, 'image.jpg', '블렌딩 원두를 사용한 아메리카노'),
                                                                                  (2, '팥빙수', 10000, 2, 101, 'image2.jpg', '맛있는 팥빙수');

INSERT INTO MenuOption (id, name, multiple_selection, required, menu_id) VALUES
                                                                             (1, '얼음 옵션', true, false, 2),
                                                                             (2, '토핑 옵션', true, false, 2);
INSERT INTO Choice (id, name, price, menu_option_id) VALUES
                                                         (1, '물얼음', 0, 1),
                                                         (2, '우유 얼음', 500, 1),
                                                         (3, '과일 추가', 2000, 2),
                                                         (4, '아이스크림 추가', 2000, 2);
INSERT INTO Orders (id, store_id, status, order_type, phone_number, table_number) VALUES
                                                                                       (1, 101, 'ORDERED', 'EATIN', '123-456-7890', 5),
                                                                                       (2, 101, 'COMPLETED', 'EATIN', NULL, NULL);

INSERT INTO OrderItem (id, menu_id, quantity, order_id) VALUES
                                                            (1, 1, 2, 1),
                                                            (2, 2, 1, 2);

INSERT INTO SelectedOption (id, menu_option_id, order_item_id) VALUES
                                                                   (1, 1, 1), -- Milk Option for Latte
                                                                   (2, 2, 2); -- Add-ons for Cookie

INSERT INTO selected_option_selected_choice_ids (selected_option_id, selected_choice_ids) VALUES
                                                                                              (1, 101), -- 첫 번째 SelectedOption의 선택된 선택지
                                                                                              (1, 102), -- 첫 번째 SelectedOption의 두 번째 선택지
                                                                                              (2, 103); -- 두 번째 SelectedOption의 선택된 선택지

INSERT INTO Payment (id, store_id, payment_method, status, order_id) VALUES
                                                                         (1, 101, 'CREDIT_CARD', 'PAYMENT_COMPLETED', 2),
                                                                         (2, 101, 'CASH', 'PAYMENT_COMPLETED', 1);




