package lt.kurti.defectregistry.web.rest;

import lt.kurti.defectregistry.domain.userservice.User;
import lt.kurti.defectregistry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/defects/{id}/users", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUserToDefect(@RequestBody User user, @PathVariable Long id) throws URISyntaxException {
        final User result = userService.createUserForDefect(id, user);

        return ResponseEntity.created(new URI("/defects/" + id + "/users/" + result.getEmail()))
                .body(result);
    }

    @GetMapping(value = "/defects/{id}/users", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsersForDefect(@PathVariable Long id) {
        final List<User> result = userService.getUsersForDefect(id);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/defects/{defectId}/users/{userId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserForDefect(@PathVariable Long defectId, @PathVariable String userId) {
        final User result = userService.getUserByIdForDefect(defectId, userId);

        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/defects/{defectId}/users/{userId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUserForDefect(@RequestBody User user, @PathVariable Long defectId, @PathVariable String userId) {
        final User result = userService.updateUserForDefect(user, defectId, userId);

        return ResponseEntity.ok()
                .body(result);
    }

    @PatchMapping(value = "/defects/{defectId}/users/{userId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> patchUserForDefect(@RequestBody User user, @PathVariable Long defectId, @PathVariable String userId) {
        final User result = userService.patchUserForDefect(user, defectId, userId);

        return ResponseEntity.ok()
                .body(result);
    }

    @DeleteMapping(value = "/defects/{defectId}/users/{userId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUserForDefect(@PathVariable Long defectId, @PathVariable String userId) {
        userService.deleteUserForDefect(defectId, userId);

        return ResponseEntity.noContent().build();
    }
}
