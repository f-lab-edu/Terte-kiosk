package com.terte.repository.menu;

import com.terte.entity.menu.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<MenuOption, Long> {
    boolean existsById(Long id);
}
