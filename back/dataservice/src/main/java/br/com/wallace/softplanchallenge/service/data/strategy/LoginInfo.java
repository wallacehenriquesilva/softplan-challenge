package br.com.wallace.softplanchallenge.service.data.strategy;


import br.com.wallace.softplanchallenge.service.data.base.enums.LoginTypeEnum;

import java.util.Map;

public interface LoginInfo {

    LoginInfo constructor(Map<String, Object> values);

    Map<String, Object> getAttributes();

    String getId();

    String getName();

    String getEmail();

    String getImagePath();

    LoginTypeEnum getType();

    String getRegistrationId();

}
