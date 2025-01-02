package com.terte.service.menu;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Option;
import com.terte.repository.menu.OptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {
    @Mock
    OptionRepository optionRepository;

    @InjectMocks
    OptionServiceImpl optionService;

    @Test
    @DisplayName("옵션 생성")
    void createOption() {
        Option option = new Option(null, "샷 추가", true, false, null);
        Option createdOption = new Option(1L, "샷 추가", true, false, null);
        when(optionRepository.save(option)).thenReturn(createdOption);

        Option result = optionService.createOption(option);

        assertNotNull(result.getId());
        assertEquals(option.getName(), result.getName());
        assertEquals(option.getRequired(), result.getRequired());
        assertEquals(option.getMultipleSelection(), result.getMultipleSelection());

        verify(optionRepository, times(1)).save(option);

    }

    @Test
    @DisplayName("존재하는 옵션 수정")
    void updateOption() {
        Option option = new Option(1L, "샷 추가", true, false, null);
        Option existingOption = new Option(1L, "샷 추가", true, false, null);
        when(optionRepository.findById(option.getId())).thenReturn(existingOption);
        when(optionRepository.save(option)).thenReturn(option);

        Option result = optionService.updateOption(option);

        assertEquals(option.getId(), result.getId());
        assertEquals(option.getName(), result.getName());
        assertEquals(option.getRequired(), result.getRequired());
        assertEquals(option.getMultipleSelection(), result.getMultipleSelection());

        verify(optionRepository, times(1)).findById(option.getId());
        verify(optionRepository, times(1)).save(option);
    }

    @Test
    @DisplayName("존재하지 않는 옵션 수정")
    void updateOptionNotFound() {
        Option option = new Option(1L, "샷 추가", true, false, null);
        when(optionRepository.findById(option.getId())).thenReturn(null);


        assertThrows(NotFoundException.class, () -> optionService.updateOption(option));
        verify(optionRepository, times(1)).findById(option.getId());
        verify(optionRepository, never()).save(option);
    }

    @Test
    @DisplayName("옵션 삭제")
    void deleteOption() {
        Long optionId = 1L;
        Option option = new Option(optionId, "샷 추가", true, false, null);
        when(optionRepository.findById(optionId)).thenReturn(option);

        optionService.deleteOption(optionId);

        verify(optionRepository, times(1)).deleteById(optionId);
    }

    @Test
    @DisplayName("존재하지 않는 옵션 삭제")
    void deleteOptionNotFound() {
        Long optionId = 1L;
        when(optionRepository.findById(optionId)).
                thenReturn(null);

        assertThrows(NotFoundException.class, () -> optionService.deleteOption(optionId));
        verify(optionRepository, never()).deleteById(optionId);
    }

    @Test
    @DisplayName("옵션의 선택지 조회")
    void getChoicesById() {
        Choice choice = new Choice(1L, "샷 추가", 500);
        Option option = new Option(1L, "샷 추가", true, false, List.of(choice));
        when(optionRepository.findById(option.getId())).thenReturn(option);

        List<Choice> result = optionService.getChoicesById(option.getId());
        assertEquals(option.getChoices(), result);
        verify(optionRepository, times(1)).findById(option.getId());
    }

    @Test
    @DisplayName("존재하지 않는 옵션의 선택지 조회")
    void getChoicesByIdNotFound() {
        Long optionId = 1L;
        when(optionRepository.findById(optionId)).
                thenReturn(null);

        assertThrows(NotFoundException.class, () -> optionService.getChoicesById(optionId));
        verify(optionRepository, times(1)).findById(optionId);

    }

}
