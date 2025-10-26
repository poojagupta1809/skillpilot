package com.thoughtworks.skillpilot.filter;

import java.io.IOException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

public class CustomerFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        CachedBodyHttpServletRequest cachedBodyHttpServletRequest =
                new CachedBodyHttpServletRequest((HttpServletRequest) request);


        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");


        //to handle preflight request for the first time which is raised by web browser , when ui is based on javascript , to check the availability of server
        if(cachedBodyHttpServletRequest.getServletPath().equals("/api/users/login") || cachedBodyHttpServletRequest.getServletPath().equals("/api/users/register-user")) {

            chain.doFilter(cachedBodyHttpServletRequest, response);
        } else {
            String authHeader = cachedBodyHttpServletRequest.getHeader("Authorization");

            if ((authHeader == null) || (!authHeader.startsWith("Bearer"))) {
                throw new ServletException("JWT Token is missing");
            }

            String token1 = authHeader.substring(7);

            try {

                JwtParser jwtparser = Jwts.parser().setSigningKey("twteamkey");

                // parse the JWT token

                Jwt jwtobj = jwtparser.parse(token1);

                Claims claim = (Claims) jwtobj.getBody();
                HttpSession httpsession = cachedBodyHttpServletRequest.getSession();

                httpsession.setAttribute("userloggedin", claim.getAudience());

            } catch (SignatureException sign) {
                throw new ServletException("Signature mismatch");

            } catch (MalformedJwtException malforn) {
                throw new ServletException("Some one modified token");
            }
            chain.doFilter(cachedBodyHttpServletRequest, httpResponse);
        }

    }


}
