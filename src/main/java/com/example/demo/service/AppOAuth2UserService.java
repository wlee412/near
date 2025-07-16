package com.example.demo.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class AppOAuth2UserService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// ① 현재 로그인 중인 프로바이더 식별 (google/naver/kakao)
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		// ② application.yml 에 설정한 user-name-attribute 키 가져오기
		String userNameAttrKey = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
				.getUserNameAttributeName();

		Map<String, Object> attrs = oAuth2User.getAttributes();

		// ③ DefaultOAuth2User 생성 시, 반드시 유효한 userNameAttrKey 전달
		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), attrs,
				userNameAttrKey);
	}
}
