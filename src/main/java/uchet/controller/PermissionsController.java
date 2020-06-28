package uchet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uchet.models.Permission;
import uchet.service.permissions.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/permissions_controller")
public class PermissionsController {

    private PermissionService permissionService;

    @GetMapping
    @RequestMapping("/all")
    public List<Permission> listOfAllUsers() {
        return permissionService.getAll();
    }



    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }
}
