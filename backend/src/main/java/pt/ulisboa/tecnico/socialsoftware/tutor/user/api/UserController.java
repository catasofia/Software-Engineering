package pt.ulisboa.tecnico.socialsoftware.tutor.user.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserService;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.Notification;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.NotificationResponse;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.dto.ExternalUserDto;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.AbstractMap;


@RestController
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping ("/users/create/{executionId}")
    @PreAuthorize("hasRole('ROLE_DEMO_ADMIN') or hasRole('ROLE_ADMIN')")
    public ExternalUserDto createExternalUser(@PathVariable int executionId, @Valid @RequestBody ExternalUserDto externalUserDto){
        return userService.createExternalUserApplicational(executionId,externalUserDto);
    }

    @PostMapping("/courses/executions/{executionId}/csv")
    @PreAuthorize("hasRole('ROLE_DEMO_ADMIN') or (hasRole('ROLE_ADMIN') and hasPermission(#executionId, 'EXECUTION.ACCESS'))")
    public NotificationResponse<CourseDto> uploadCSVFile(@PathVariable Integer executionId, @RequestParam("file") MultipartFile file) throws IOException {
        return userService.importListOfUsers(file.getInputStream(), executionId);
    }
}
