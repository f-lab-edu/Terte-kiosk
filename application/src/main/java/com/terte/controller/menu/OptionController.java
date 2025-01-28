package com.terte.controller.menu;

import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.menu.*;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.MenuOption;
import com.terte.service.menu.MenuService;
import com.terte.service.menu.OptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/options")
@RequiredArgsConstructor
public class OptionController {

    private final MenuService menuService;
    private final OptionService optionService;


    /**
     * POST /options
     * 옵션생성
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createOption(@RequestBody @Valid OptionCreateReqDTO optionCreateReqDTO){
        Long menuId = optionCreateReqDTO.getMenuId();
        Menu menu = menuService.getMenuById(menuId);
        MenuOption menuOption = new MenuOption(
                null,
                optionCreateReqDTO.getName(),
                optionCreateReqDTO.getMultipleSelection(),
                optionCreateReqDTO.getRequired(),
                null,
                menu);
        MenuOption createdMenuOption = optionService.createOption(menuOption);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(createdMenuOption.getId()).build()));
    }

    /**
     * PUT /options
     * 옵션수정
     */
    @PutMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> updateOption(@RequestBody @Valid OptionUpdateReqDTO optionUpdateReqDTO){
        MenuOption menuOption = new MenuOption(optionUpdateReqDTO.getId(),
                optionUpdateReqDTO.getName(),
                optionUpdateReqDTO.getMultipleSelection(),
                optionUpdateReqDTO.getRequired(),
                null,null);
        MenuOption updatedMenuOption = optionService.updateOption(menuOption);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(updatedMenuOption.getId()).build()));
    }

    /**
     * DELETE /options
     * 옵션수정
     */
    @DeleteMapping("/{optionId}")
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> updateOption(@PathVariable Long optionId){
        optionService.deleteOption(optionId);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(optionId).build()));
    }

    /**
     * GET /options/{optionId}/choices
     * 특정 옵션의 선택지 조회
     */
    @GetMapping("/{optionId}/choices")
    public ResponseEntity<ApiResDTO<List<ChoiceResDTO>>> getMenuById(@PathVariable Long optionId) {
        List<Choice> choices = optionService.getChoicesById(optionId);
        List<ChoiceResDTO> choiceResDTOList = choices.stream().map(ChoiceResDTO::from).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResDTO.success(choiceResDTOList));

    }
}
