package com.lms.oauth2.userInfo;

public interface OAuth2UserInfo {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    String getFullName();
}
