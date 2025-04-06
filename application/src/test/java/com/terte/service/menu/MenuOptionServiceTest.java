package com.terte.service.menu;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.MenuOption;
import com.terte.repository.menu.OptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuOptionServiceTest {
    @Mock
    OptionRepository optionMapRepository;

    @InjectMocks
    OptionServiceImpl optionService;

    @Test
    @DisplayName("옵션 생성")
    void createOption() {
        MenuOption menuOption = new MenuOption(null, "샷 추가", true, false, null, null);
        MenuOption createdMenuOption = new MenuOption(1L, "샷 추가", true, false, null, null);
        when(optionMapRepository.save(menuOption)).thenReturn(createdMenuOption);

        MenuOption result = optionService.createOption(menuOption);

        assertNotNull(result.getId());
        assertEquals(menuOption.getName(), result.getName());
        assertEquals(menuOption.getRequired(), result.getRequired());
        assertEquals(menuOption.getMultipleSelection(), result.getMultipleSelection());

        verify(optionMapRepository, times(1)).save(menuOption);

    }

    @Test
    @DisplayName("존재하는 옵션 수정")
    void updateOption() {
        MenuOption menuOption = new MenuOption(1L, "샷 추가", true, false, null, null);
        MenuOption existingMenuOption = new MenuOption(1L, "샷 추가", true, false, null, null);
        when(optionMapRepository.findById(menuOption.getId())).thenReturn(Optional.of(existingMenuOption));
        when(optionMapRepository.save(menuOption)).thenReturn(menuOption);

        MenuOption result = optionService.updateOption(menuOption);

        assertEquals(menuOption.getId(), result.getId());
        assertEquals(menuOption.getName(), result.getName());
        assertEquals(menuOption.getRequired(), result.getRequired());
        assertEquals(menuOption.getMultipleSelection(), result.getMultipleSelection());

        verify(optionMapRepository, times(1)).findById(menuOption.getId());
        verify(optionMapRepository, times(1)).save(menuOption);
    }

    @Test
    @DisplayName("존재하지 않는 옵션 수정")
    void updateOptionNotFound() {
        MenuOption menuOption = new MenuOption(1L, "샷 추가", true, false, null, null);
        when(optionMapRepository.findById(menuOption.getId())).thenReturn(Optional.empty());


        assertThrows(NotFoundException.class, () -> optionService.updateOption(menuOption));
        verify(optionMapRepository, times(1)).findById(menuOption.getId());
        verify(optionMapRepository, never()).save(menuOption);
    }

    @Test
    @DisplayName("옵션 삭제")
    void deleteOption() {
        Long optionId = 1L;
        MenuOption menuOption = new MenuOption(optionId, "샷 추가", true, false, null, null);
        when(optionMapRepository.findById(optionId)).thenReturn(Optional.of(menuOption));

        optionService.deleteOption(optionId);

        verify(optionMapRepository, times(1)).deleteById(optionId);
    }

    @Test
    @DisplayName("존재하지 않는 옵션 삭제")
    void deleteOptionNotFound() {
        Long optionId = 1L;
        when(optionMapRepository.findById(optionId)).
                thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> optionService.deleteOption(optionId));
        verify(optionMapRepository, never()).deleteById(optionId);
    }

    @Test
    @DisplayName("옵션의 선택지 조회")
    void getChoicesById() {
        Choice choice = new Choice(1L, "샷 추가", 500, null);
        MenuOption menuOption = new MenuOption(1L, "샷 추가", true, false, Set.of(choice), null);
        when(optionMapRepository.findById(menuOption.getId())).thenReturn(Optional.of(menuOption));

        Set<Choice> result = optionService.getChoicesById(menuOption.getId());
        assertEquals(menuOption.getChoices(), result);
        verify(optionMapRepository, times(1)).findById(menuOption.getId());
    }

    @Test
    @DisplayName("존재하지 않는 옵션의 선택지 조회")
    void getChoicesByIdNotFound() {
        Long optionId = 1L;
        when(optionMapRepository.findById(optionId)).
                thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> optionService.getChoicesById(optionId));
        verify(optionMapRepository, times(1)).findById(optionId);

    }

}
