package com.terte.repository.menu;

import com.terte.entity.menu.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<List<Option>> findByMenuId(Long menuId);
}
