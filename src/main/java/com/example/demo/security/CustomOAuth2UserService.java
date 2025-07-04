package com.example.demo.security;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.ClientMapper;
import com.example.demo.model.Client;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final ClientMapper clientMapper;
	private final HttpSession session;
	
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(request);

		String platform = request.getClientRegistration().getRegistrationId();
		Map<String, Object> attributes = oAuth2User.getAttributes();
		
		String socialId = null;
		String name = null;
		String email = null;

		if ("google".equals(platform)) {
			socialId = attributes.get("sub").toString();
			name = (String) attributes.get("name");
			email = (String) attributes.get("email");
		} else if ("naver".equals(platform)) {
			attributes = (Map<String, Object>) attributes.get("response");
			socialId = attributes.get("id").toString();
			name = (String) attributes.get("name");
			email = (String) attributes.get("email");
		} else if ("kakao".equals(platform)) {
			socialId = attributes.get("id").toString();
			Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
		    name = (String) profile.get("nickname");
		    email = (String) kakaoAccount.get("email");
		} else {
			throw new IllegalArgumentException("Unknown platform: " + platform);
		}
		
		String emailId = "", emailDomain = "";
		if (email != null && email.contains("@")) {
			String[] emailParts = email.split("@");
			emailId = emailParts[0];
			emailDomain = emailParts[1];
		}

		Client client = findByEmail(emailId, emailDomain);
		if (client == null) {
			client = new Client();
			String shortUUID = UUID.randomUUID().toString().substring(0, 30);
			client.setClientId(shortUUID);
			client.setName(name);	
			client.setSocialPlatform(platform);
			client.setSocialId(socialId);
			client.setPassword("SOCIAL");
			client.setBirth(Date.valueOf("1900-01-01"));
			client.setPhone("000-0000-0000");
			client.setGender('N');
			client.setEmailVerified("N");
			client.setZipcode(" ");
			client.setAddrBase(" ");
			client.setAddrDetail(email);
			client.setZipcode(" ");
			clientMapper.socialInsert(client);
			
			client = clientMapper.findBySocialIdAndPlatform(socialId, platform);
		}
		
		session.setAttribute("loginClient", client);

		List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		
		if(platform.equals("kakao"))
			return new DefaultOAuth2User(authorities, attributes, "id");
		else
			return new DefaultOAuth2User(authorities, attributes, "name");
	}

	public Client findByEmail(String emailId, String emailDomain) {
		return clientMapper.findByEmail(emailId + "@" + emailDomain);
	}
}