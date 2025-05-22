package top.lrshuai.common.satoken.config;

import cn.dev33.satoken.stp.StpInterface;

import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
public class StpInterfaceImpl  implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of();
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return List.of();
    }
}
