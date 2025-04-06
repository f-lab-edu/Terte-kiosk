package com.terte.repository.menu;

import com.terte.entity.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStoreId(Long storeId);
    List<Menu> findByCategoryId(Long categoryId);

    List<Menu> findByIdIn(List<Long> ids);

    @Query("""
    SELECT DISTINCT m FROM Menu m
    LEFT JOIN FETCH m.menuOptions mo
    LEFT JOIN FETCH mo.choices
    WHERE m.id IN :ids
""")
    List<Menu> findAllWithOptionsAndChoicesByIds(@Param("ids") List<Long> ids);

}
