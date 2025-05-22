package top.lrshuai.user.api.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LoginDto implements Serializable {

    @NotEmpty(message = "username不能为空")
    private String username;

    @NotEmpty(message = "password不能为空")
    private String password;

}
