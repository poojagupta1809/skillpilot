package com.thoughtworks.skillpilot.filter;

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
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomerFilter extends GenericFilter {

  private static final Logger logger = LoggerFactory.getLogger(CustomerFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;

    CachedBodyHttpServletRequest cachedBodyHttpServletRequest =
        new CachedBodyHttpServletRequest(httpRequest);

    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String origin = httpRequest.getHeader("Origin");
    if (origin != null && !origin.isEmpty()) {
      // when credentials are allowed, you cannot use '*', must echo the origin
      httpResponse.setHeader("Access-Control-Allow-Origin", origin);
      httpResponse.setHeader("Vary", "Origin");
    } else {
      httpResponse.setHeader("Access-Control-Allow-Origin", "*");
    }
    httpResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTIONS");
    httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
    httpResponse.setHeader(
        "Access-Control-Allow-Headers",
        "Origin, Content-Type, Accept, Authorization, X-Requested-With");
    httpResponse.setHeader("Access-Control-Expose-Headers", "Authorization");

    // Allow preflight requests to pass through without authentication check
    if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
      // respond directly for preflight
      httpResponse.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    // to handle login/register endpoints without auth
    if (cachedBodyHttpServletRequest.getServletPath().equals("/api/users/login")
        || cachedBodyHttpServletRequest.getServletPath().equals("/api/users/register-user")) {
      chain.doFilter(cachedBodyHttpServletRequest, response);
    } else {
      String authHeader = cachedBodyHttpServletRequest.getHeader("Authorization");

      // Expect header in format: "Bearer <token>" (note the space)
      if ((authHeader == null) || (!authHeader.startsWith("Bearer "))) {
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token is missing");
        return;
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
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Signature mismatch");
        return;

      } catch (MalformedJwtException malforn) {
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Some one modified token");
        return;
      }
      chain.doFilter(cachedBodyHttpServletRequest, httpResponse);
    }
  }
}
