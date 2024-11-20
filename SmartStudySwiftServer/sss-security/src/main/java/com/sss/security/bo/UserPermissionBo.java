package com.sss.security.bo;

import com.sss.security.dao.UmsUserDao;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class UserPermissionBo implements Serializable {
    private UmsUserDao umsUserDao;
    private List<String> roleNames;
    private List<String> permissionNames;
}
