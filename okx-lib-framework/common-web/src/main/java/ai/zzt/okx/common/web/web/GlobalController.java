package ai.zzt.okx.common.web.web;

import ai.zzt.okx.common.base.IR;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.common.web.dto.EnumDTO;
import ai.zzt.okx.v5.enumeration.Bar;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouzhitong
 * @since 2024/11/27
 **/
@RestController
@RequestMapping("/api/v1/global")
public class GlobalController {

    private final List<String> packageList = List.of("ai.zzt.okx");

    @GetMapping("enums")
    public IR<Map<String, List<EnumDTO>>> getAllEnums() {
//        List<Class<IEnum>> subClasses = PackageUtil.getSubClasses(IEnum.class, packageList);
        Map<String, List<EnumDTO>> map = new HashMap<>();
        List<Bar> bars = Bar.BARS;
        List<EnumDTO> barEnums = new ArrayList<>(bars.size());
        for (Bar bar : bars) {
            EnumDTO enumDTO = new EnumDTO();
            enumDTO.setCode(bar.getValue());
            enumDTO.setName(bar.getName());
            enumDTO.setVal(bar);
            barEnums.add(enumDTO);
        }
        map.put("bars", barEnums);

        List<EnumDTO> indicatorEnums = new ArrayList<>();

        IndicatorType[] values = IndicatorType.values();
        for (IndicatorType type : values) {
            EnumDTO enumDTO = new EnumDTO();
            enumDTO.setCode(type.getName());
            enumDTO.setName(type.getName());
            enumDTO.setVal(type);
            indicatorEnums.add(enumDTO);
        }
        map.put("indicators", indicatorEnums);
        return R.ok(map);
    }

}
