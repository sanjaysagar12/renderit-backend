package com.turplespace.renderit.hoster;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.MalformedURLException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HosterController {
    
    @GetMapping("/**")
    public ResponseEntity<Resource> serveSite(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        
        if (path.isEmpty()) {
            path = "index.html";
        }

        System.out.println("Processing Request: " + request.getRequestURL());
        
        String directory = "sites";
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            try {
                String base64Credentials = authHeader.substring(6);
                byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Credentials);
                String credentials = new String(decodedBytes, java.nio.charset.StandardCharsets.UTF_8);
                // credentials = username:password
                final String[] values = credentials.split(":", 2);
                if (values.length > 0 && !values[0].isEmpty()) {
                    directory = values[0];
                    System.out.println("User detected in Auth header: " + directory);
                }
            } catch (Exception e) {
                System.out.println("Error parsing auth header: " + e.getMessage());
            }
        } else {
            System.out.println("No Authorization header. Sending 401 challenge.");
            return ResponseEntity.status(401)
                    .header("WWW-Authenticate", "Basic realm=\"RenderIt\"")
                    .build();
        }

        System.out.println("Serving from directory: " + directory);

        try {
            Path filePath = Paths.get(directory).resolve(path).normalize();
            
            if (!filePath.startsWith(directory)) {
                 return ResponseEntity.badRequest().build();
            }

            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
