package com.terte.controller.menu;

import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.menu.ChoiceCreateReqDTO;
import com.terte.dto.menu.ChoiceResDTO;
import com.terte.dto.menu.ChoiceUpdateReqDTO;
import com.terte.dto.menu.OptionCreateReqDTO;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Option;
import com.terte.service.menu.ChoiceService;
import com.terte.service.menu.MenuService;
import com.terte.service.menu.OptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/choices")
@RequiredArgsConstructor
public class ChoiceController {
    private final OptionService optionService;
    private final ChoiceService choiceService;

    /**
     * POST /choices
     * 선택지생성
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createOption(@RequestBody @Valid ChoiceCreateReqDTO choiceCreateReqDTO){
        Long optionId = choiceCreateReqDTO.getOptionId();
        Option option = optionService.getOptionById(optionId);
        Choice choice = new Choice(
                null,
                choiceCreateReqDTO.getName(),
                choiceCreateReqDTO.getPrice(),
                option
        );
        Choice createdChoice = choiceService.createChoice(choice);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(createdChoice.getId()).build()));
    }

    /**
     * PUT /choices
     * 선택지 수정
     */
    @PutMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> updateOption(@RequestBody @Valid ChoiceUpdateReqDTO choiceUpdateReqDTO){
        Choice choice = new Choice(
                choiceUpdateReqDTO.getId(),
                choiceUpdateReqDTO.getName(),
                choiceUpdateReqDTO.getPrice(),
                null
        );
        Choice updatedChoice = choiceService.updateChoice(choice);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(updatedChoice.getId()).build()));
    }

    /**
     * DELETE /choices/{choiceId}
     * 선택지 삭제
     */
    @DeleteMapping("/{choiceId}")
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> updateOption(@PathVariable Long choiceId){
        choiceService.deleteChoice(choiceId);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(choiceId).build()));
    }
}
