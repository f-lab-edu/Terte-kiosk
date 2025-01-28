package com.terte.repository.menu;

import com.terte.entity.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStoreId(Long storeId);
    List<Menu> findByCategoryId(Long categoryId);

    List<Menu> findByIdIn(List<Long> ids);
}
