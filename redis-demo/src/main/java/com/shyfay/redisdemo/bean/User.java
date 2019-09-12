package com.shyfay.redisdemo.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mx
 * @since 2019/9/12
 */
@Data
public class User implements Serializable {
    private Long userId;
    private String userName;
    private String loginName;
    private String loginPassword;
}
