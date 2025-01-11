package com.terte.controller.menu;

import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.menu.*;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.Option;
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
        Option option = new Option(
                null,
                optionCreateReqDTO.getName(),
                optionCreateReqDTO.getMultipleSelection(),
                optionCreateReqDTO.getRequired(),
                null);
        Option createdOption = optionService.createOption(option);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(createdOption.getId()).build()));
    }

    /**
     * PUT /options
     * 옵션수정
     */
    @PutMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> updateOption(@RequestBody @Valid OptionUpdateReqDTO optionUpdateReqDTO){
        Option option = new Option(optionUpdateReqDTO.getId(),
                optionUpdateReqDTO.getName(),
                optionUpdateReqDTO.getMultipleSelection(),
                optionUpdateReqDTO.getRequired(),
                null);
        Option updatedOption = optionService.updateOption(option);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(updatedOption.getId()).build()));
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
