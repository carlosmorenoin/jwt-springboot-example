package org.example.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Controller
public class Protected {

    @RequestMapping(value = "/protected", method = RequestMethod.GET)
    @ResponseBody
    public String getProtectedResource(HttpServletRequest request) {

        if (!validateToken(request)) {
            throw new ForbiddenException();
        }
        return "<h2>Welcome to the PROTECTED RESOURCE</h2><br/><hr/><p>Here is your token: " + retrieveJWT(request) + "</p>";
    }

    private boolean validateToken(final HttpServletRequest request) {
        final String jwt = retrieveJWT(request);
        if (jwt == null) {
            return false;
        }
        Claims claims = Jwts.parser().setSigningKey(Secret.getKey()).parseClaimsJws(jwt).getBody();

        return "EXTERNAL_ID".equals(claims.get("externalServiceId"))
                && "demo".equals(claims.getSubject());
    }

    private String retrieveJWT(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return StringUtils.substringAfter(authHeader, "Bearer ");
    }
}
