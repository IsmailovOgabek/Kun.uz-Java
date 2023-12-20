package com.example.contoller;

import com.example.dto.profile.ProfileDTO;
import com.example.dto.profile.ProfileFilterDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Tag(name = "ProfileController API", description = "Api list for ProfileEntity")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    /**
     * ADMIN
     */
    @PostMapping("")
    public ResponseEntity<ProfileDTO> save(HttpServletRequest request,
                                           @RequestBody ProfileDTO profileDTO) {
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);

        ProfileDTO profile = profileService.create(profileDTO, pId);
        return ResponseEntity.ok(profile);
    }


    @GetMapping("/list")
    public ResponseEntity<Page<ProfileDTO>> getList(@RequestParam Integer page,
                                                    @RequestParam Integer size,
                                                    HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        Page<ProfileDTO> response = profileService.getList(page, size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id, HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);

        Boolean result = profileService.delete(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer id,
                                         @RequestBody ProfileDTO profileDTO,
                                         HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);

        ProfileDTO result = profileService.updateAdmin(id, profileDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * ANY
     */
    @PutMapping("")
    public ResponseEntity<?> update(HttpServletRequest request,
                                    @RequestBody ProfileDTO profileDTO) {
        Integer pId = JwtUtil.getIdFromHeader(request);
        int result = profileService.updateUser(pId, profileDTO);
        if (result == 1) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/filter")
    public ResponseEntity<?> update(@RequestBody ProfileFilterDTO filterDTO,
                                    @RequestParam("page") int page, @RequestParam("size") int size) {
        profileService.filter(filterDTO, page, size);
        return ResponseEntity.ok().build();


        // 1  Qalaysan  2

    }
}
