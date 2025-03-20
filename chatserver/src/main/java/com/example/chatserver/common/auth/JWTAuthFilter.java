package com.example.chatserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JWTAuthFilter extends GenericFilter {

    private final ServletConfig servletConfig;
    @Value("${jwt.secretKey}")
    private String secretKey;

    public JWTAuthFilter(@Qualifier("servletConfig") ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override
    public void doFilter(ServletRequest request, //request 안에 토큰이 들어가 있음
                         ServletResponse response, //토큰에 문제가 있으면 response에 반환해줌, 아니면 정상적으로 dofilter로 이동
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletRequest.getHeader("Authorization");

        try {

            if (token != null) { //토큰이 있는 경우에만 검증하겠다.
                if (token.substring(0, 7).equals("Bearer ")) { //Bearer이 앞에 붙어있는지 확인
                    throw new AuthenticationServiceException("Bearer 형식이 아닙니다.");
                }
                String jwtToken = token.substring(7);//Bearer 떄내고 토큰의 원본만 꺼냄

                //우리 서비스 토큰인지 확인하는 방법은 시그니처 부분을 확인하는것

                //헤더 + 페이로드 + 시크릿키 암호화 -> 시그니처 부분
                //우리가 헤어 + 페이로드는 알고 있으니 암호화해서 동일한 내용인지 확인
                //claims은 페이로드 부분 이므로  아래는 현재 페이로드 부분을 꺼내는 것

                //토큰 검증 및 claims 추출
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJwt(jwtToken)
                        .getBody();

                //authentication 객체 생성
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role").toString()));
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication); //계층 구조
            }
            chain.doFilter(request, response);
        }catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("invalid token");
        }
    }
}
