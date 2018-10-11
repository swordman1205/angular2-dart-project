package com.qurasense.userApi.controllers;

import java.util.List;

import com.qurasense.userApi.model.User;
import com.qurasense.userApi.model.UserWithPassword;
import com.qurasense.userApi.service.CustomerService;
import com.qurasense.userApi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="qurasense users", description="qurasense users registry")
public class UsersController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @ApiOperation(value = "List users", response = User.class, responseContainer = "List",
            authorizations = @Authorization(
                    value = "oauth2",
                    scopes = {
                            @AuthorizationScope(
                                    scope = "Only Admin can get all users", description = ""
                            )
                    }
            ))
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @ApiOperation(value = "List nurses", response = User.class, responseContainer = "List",
            authorizations = @Authorization(
                    value = "oauth2",
                    scopes = {
                            @AuthorizationScope(
                                    scope = "Only Medic can get all nurses", description = ""
                            )
                    }
            ))
    @RequestMapping(value = "/users/nurse", method = RequestMethod.GET)
    public List<User> getNurses() {
        return userService.getNurses();
    }

    @ApiOperation(value = "Get user", response = User.class)
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable("userId") String aUserId) {
        return userService.getUserById(aUserId);
    }

    @ApiOperation(value = "update user", response = User.class)
    @RequestMapping(value = "/admin/user/{userId}", method = RequestMethod.POST)
    public User updateUser(@PathVariable("userId") String aUserId, @RequestBody UserWithPassword toUpdate) {
        return userService.update(toUpdate);
    }

    @ApiOperation(value = "update user")
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.POST)
    public void updateUser(@PathVariable("userId") String aUserId, @RequestBody User toUpdate) {
        userService.update(toUpdate);
    }

    @ApiOperation(value = "delete user", authorizations = @Authorization(
            value = "oauth2",
            scopes = {
                    @AuthorizationScope(
                            scope = "Admin can delete any user", description = ""
                    ),
                    @AuthorizationScope(
                            scope = "Customer can delete himself", description = ""
                    )
            }
    ))
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(User deleter, @PathVariable("userId") String aUserId) {
        userService.delete(aUserId);
    }

    @ApiOperation(value = "Create user", response = Long.class)
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String createUser(User creator, @RequestBody UserWithPassword aToCreate) {
        return userService.create(aToCreate);
    }

//    @ApiOperation(value = "Get user by id", response = User.class)
////    @ApiResponses(value = {
////            @ApiResponse(code = 200, message = "Successfully retrieved user"),
////            @ApiResponse(code = 401, message = "You are not authorized to view the user"),
////            @ApiResponse(code = 403, message = "Accessing the user you were trying to reach is forbidden"),
////            @ApiResponse(code = 404, message = "The user you were trying to reach is not found")
////        })
//    @RequestMapping(value = "/{id}", method= RequestMethod.GET)
//    public ResponseEntity<User> showUser(User user, @PathVariable Long id) {
//        if (user.getRole() == UserRole.CUSTOMER && !Objects.equals(user.getId(), id)) {
//            logger.warn("User with id:{} trying to lookup user with id: {}", user.getId(), id);
//        }
//        User userById = userService.getUserById(id);
//        ResponseEntity<User> response = user != null ?
//                ResponseEntity.ok(userById) :
//                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        return response;
//    }
}
