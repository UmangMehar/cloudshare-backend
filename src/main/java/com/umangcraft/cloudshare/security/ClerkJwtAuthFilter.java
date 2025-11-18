package com.umangcraft.cloudshare.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class ClerkJwtAuthFilter extends OncePerRequestFilter {

    @Value("${clerk.issuer}")
    private String clerkIssuer;

    private final ClerkJwksProvider jwksProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n================= Clerk JWT Filter =================");
        System.out.println("➡️ Incoming: " + request.getMethod() + " " + request.getRequestURI());

        // Skip JWT for public endpoints
        if (request.getRequestURI().contains("/webhooks") ||
                request.getRequestURI().contains("/public") ||
                request.getRequestURI().contains("/download") ||
                request.getRequestURI().contains("/health")) {

            System.out.println("⚠️ Skipping token validation for public endpoint");
            System.out.println("====================================================\n");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("🔍 Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Missing or invalid Authorization header");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authorization header missing/invalid");
            System.out.println("====================================================\n");
            return;
        }

        try {
            String token = authHeader.substring(7);
            System.out.println("🔑 Extracted Token: " + token);

            String[] chunks = token.split("\\.");
            if (chunks.length < 3) {
                System.out.println("❌ Token does NOT have 3 parts");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid JWT token format");
                return;
            }

            // Decode header
            String headerJson = new String(Base64.getUrlDecoder().decode(chunks[0]));
            System.out.println("📝 Header Decoded: " + headerJson);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode headerNode = mapper.readTree(headerJson);

            if (!headerNode.has("kid")) {
                System.out.println("❌ JWT missing 'kid' in header");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token header is missing kid");
                return;
            }

            String kid = headerNode.get("kid").asText();
            System.out.println("🔑 Extracted KID: " + kid);

            // Fetch public key
            PublicKey publicKey = jwksProvider.getPublicKey(kid);
            System.out.println("🔐 Public key successfully resolved");

            // Verify token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .setAllowedClockSkewSeconds(60)
                    .requireIssuer(clerkIssuer)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("✅ Token Verified. Claims: " + claims);

            String clerkId = claims.getSubject();
            System.out.println("👤 Clerk User ID: " + clerkId);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            clerkId,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println("🔓 SecurityContext updated");
            System.out.println("====================================================\n");

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("❌ JWT Verification FAILED: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid JWT token: " + e.getMessage());
            System.out.println("====================================================\n");
        }
    }
}
