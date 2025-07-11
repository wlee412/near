package com.example.demo.security;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.ClientMapper;
import com.example.demo.model.Client;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private static final Logger log = LoggerFactory.getLogger(CustomOidcUserService.class);

    private final ClientMapper clientMapper;
    private final HttpSession session;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("✅ CustomOidcUserService 진입함");
        // 1) OIDC 유저 정보 가져오기
        OidcUser oidcUser = super.loadUser(userRequest);
        log.info("### OIDC Attributes: {}", oidcUser.getAttributes());
        
        // 2) 플랫폼, 소셜ID, 이름, 이메일 추출 (구글 응답 구조 기준)
        String platform = userRequest.getClientRegistration().getRegistrationId(); // "google"
        Map<String, Object> attrs = oidcUser.getAttributes();
        String socialId = attrs.get("sub").toString();
        String name     = (String) attrs.get("name");
        String email    = (String) attrs.get("email");

        // 3) DB에 없으면 socialInsert
        String[] split = email.split("@");
        String emailId     = split[0];
        String emailDomain = split[1];
        Client client = clientMapper.findByEmail(email);
        log.info("→ findByEmail('{}') returned: {}", email, client);

        if (client == null) {
            client = new Client();
            client.setClientId(UUID.randomUUID().toString());
            client.setName(name);
            client.setSocialPlatform(platform);
            client.setSocialId(socialId);
            client.setPassword("SOCIAL");
            client.setBirth(Date.valueOf("1900-01-01"));
            client.setPhone("000-0000-0000");
            client.setGender("N");
            client.setVerified("N");
            client.setZipcode("");
            client.setAddrBase("");
            client.setAddrDetail("");
            client.setAlarm(false);
            client.setPersonalInfo(true);
            client.setEmailId(emailId);
            client.setEmailDomain(emailDomain);
            clientMapper.socialInsert(client);
            
            log.info("New social user inserted: {}", client);
        }

        // 4) 세션에 저장
        session.setAttribute("loginClient", client);

        // 5) 권한 부여 후 반환
        List authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
