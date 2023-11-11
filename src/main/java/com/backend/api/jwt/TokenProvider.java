package com.backend.api.jwt;

import com.backend.api.request.util.AccessToken;
import com.backend.api.request.util.RefreshToken;
import com.backend.api.response.user.TokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private final String accessHeader;

    private final String refreshHeader;

    private final String accessSecret;

    private final long accessTokenValidityInMs;

    private final long refreshTokenValidityInMs;

    private Key key;

    public TokenProvider(@Value("${jwt.access-secret}") String accessSecret,
                         @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInMs,
                         @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInMs,
                         @Value("${jwt.header}") String accessHeader,
                         @Value("${jwt.refresh-header}") String refreshHeader) {
        this.accessSecret = accessSecret;
        this.accessTokenValidityInMs = accessTokenValidityInMs;
        this.refreshTokenValidityInMs = refreshTokenValidityInMs;
        this.accessHeader = accessHeader;
        this.refreshHeader = refreshHeader;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
    }

    public TokenResponse createToken(String email) {
        AccessToken accessToken = AccessToken.builder()
                .header(accessHeader)
                .data(createAccessToken(email))
                .build();
        RefreshToken refreshToken = RefreshToken.builder()
                .header(refreshHeader)
                .data(createRefreshToken())
                .build();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(accessHeader).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 토큰 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("토큰이 잘못되었습니다.");
        }
        return false;
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String createAccessToken(String email) {

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accessTokenValidityInMs);

        return Jwts.builder()
                .setSubject(email)
                .signWith(key)
                .setExpiration(validity)
                .compact();
    }

    public String createRefreshToken() {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshTokenValidityInMs);

        return Jwts.builder()
                .setExpiration(validity)
                .signWith(key)
                .compact();

    }
}
