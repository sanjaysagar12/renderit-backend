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

        try {
            Path filePath = Paths.get("sites").resolve(path).normalize();
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
