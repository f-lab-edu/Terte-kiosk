package com.terte.service.menu;
import com.terte.common.exception.NotFoundException;
import com.terte.entity.menu.Choice;
import com.terte.repository.menu.ChoiceRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChoiceServiceTest {
    @Mock
    ChoiceRepository choiceRepository;

    @InjectMocks
    ChoiceServiceImpl choiceService;

    @Test
    @DisplayName("선택지 생성")
    void createChoice() {
        Choice choice = new Choice(null, "샷 추가", 500);
        Choice createdChoice = new Choice(1L, "샷 추가", 500);
        when(choiceRepository.save(choice)).thenReturn(createdChoice);

        Choice result = choiceService.createChoice(choice);

        assertNotNull(result.getId());
        assertEquals(choice.getName(), result.getName());
        assertEquals(choice.getPrice(), result.getPrice());

        verify(choiceRepository, times(1)).save(choice);
    }

    @Test
    @DisplayName("존재하는 선택지 수정")
    void updateChoice() {
        Choice choice = new Choice(1L, "샷 추가", 500);
        Choice existingChoice = new Choice(1L, "샷 추가", 500);
        when(choiceRepository.findById(choice.getId())).thenReturn(existingChoice);
        when(choiceRepository.save(choice)).thenReturn(choice);

        Choice result = choiceService.updateChoice(choice);

        assertEquals(choice.getId(), result.getId());
        assertEquals(choice.getName(), result.getName());
        assertEquals(choice.getPrice(), result.getPrice());

        verify(choiceRepository, times(1)).findById(choice.getId());
    }

    @Test
    @DisplayName("존재하지 않는 선택지 수정")
    void updateChoiceWithNonExistingChoice() {
        Choice choice = new Choice(1L, "샷 추가", 500);
        when(choiceRepository.findById(choice.getId())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> choiceService.updateChoice(choice));
        verify(choiceRepository, times(1)).findById(choice.getId());
    }

    @Test
    @DisplayName("선택지 삭제")
    void deleteChoice() {
        Long choiceId = 1L;
        Choice existingChoice = new Choice(choiceId, "샷 추가", 500);
        when(choiceRepository
                .findById(choiceId))
                .thenReturn(existingChoice);

        choiceService.deleteChoice(choiceId);
        verify(choiceRepository, times(1)).deleteById(choiceId);
    }

    @Test
    @DisplayName("존재하지 않는 선택지 삭제")
    void deleteChoiceWithNonExistingChoice() {
        Long choiceId = 1L;
        when(choiceRepository
                .findById(choiceId))
                .thenReturn(null);
        assertThrows(NotFoundException.class, () -> choiceService.deleteChoice(choiceId));
        verify(choiceRepository, never()).deleteById(choiceId);
    }
}
