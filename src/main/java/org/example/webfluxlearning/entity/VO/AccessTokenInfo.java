package org.example.webfluxlearning.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

// !todo 增加权限部分
@Data
@AllArgsConstructor
public class AccessTokenInfo {
    private String userId;
    private String username;
    private String email;
//    private List<String> roles;
}
