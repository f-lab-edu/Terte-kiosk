package com.terte.service.menu;

import com.terte.entity.menu.Choice;
import org.springframework.stereotype.Service;

@Service
public interface ChoiceService {
    Choice getChoiceById(Long id);
    Choice createChoice(Choice choice);
    Choice updateChoice(Choice choice);
    void deleteChoice(Long id);
}
