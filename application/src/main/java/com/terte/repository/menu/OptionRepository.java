package com.terte.repository.menu;

import com.terte.entity.menu.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<MenuOption, Long> {
    Optional<List<MenuOption>> findByMenuId(Long menuId);
}
