package com.terte.repository.order;

import com.terte.entity.order.SelectedOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SelectedOptionRepository extends JpaRepository<SelectedOption, Long> {
    Optional<List<SelectedOption>> findByOrderId(Long orderId);
}
