package top.lrshuai.mall.api.dto;

import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class UpdateCommodityDto {

    @NotBlank(message = "code不能为空")
    private String code;

    @NegativeOrZero(message = "amount不能小于0")
    private Integer decrCount;
}
