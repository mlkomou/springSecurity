/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.jwtsecurite.controller;

import io.tuwindi.demo.model.Dashboard;
import io.tuwindi.demo.model.Utilisateur;
import io.tuwindi.demo.model.UtilisateurMobile;
import io.tuwindi.demo.payload.ChangePassword;
import io.tuwindi.demo.payload.ChangePasswordRequest;
import io.tuwindi.demo.payload.RegistrationForm;
import io.tuwindi.demo.sec.CurrentUser;
import io.tuwindi.demo.sec.UserPrincipal;
import io.tuwindi.demo.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author alhousseini
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/utilisateur")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/saveFileExcel")
    public ResponseEntity<?> saveFileExcel(@RequestBody List<UtilisateurMobile> utilisateurMobile) {
        return new ResponseEntity<>(utilisateurService.saveFileExcel(utilisateurMobile), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Utilisateur utilisateur) {
        return new ResponseEntity<>(utilisateurService.save(utilisateur), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegistrationForm utilisateur) {
        return new ResponseEntity<>(utilisateurService.signUp(utilisateur), HttpStatus.OK);
    }
    @PostMapping("/signup/list")
    public ResponseEntity<?> signupList(@RequestBody List<RegistrationForm> utilisateur) {
        return new ResponseEntity<>(utilisateurService.signUpList(utilisateur), HttpStatus.OK);
    }

    @GetMapping("/me")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new ResponseEntity<>(utilisateurService.findCurrentUser(currentUser), HttpStatus.OK);
    }

    @GetMapping("/findAllAgent")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(utilisateurService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody ChangePasswordRequest passwordRequest,
                                            @CurrentUser UserPrincipal userPrincipal) {
        System.out.println(userPrincipal.getId());
        return new ResponseEntity<>(utilisateurService.updatePassword(passwordRequest, userPrincipal), HttpStatus.OK);
    }
    @PutMapping("/changePasswordUser")
    public ResponseEntity<?> updatePasswordUser(@RequestBody ChangePassword changePassword) {
        return new ResponseEntity<>(utilisateurService.updatePasswordUser(changePassword), HttpStatus.OK);
    }

    @GetMapping("/findAllUser")
    public ResponseEntity<?> findAllUser() {
        return new ResponseEntity<>(utilisateurService.findAllUser(), HttpStatus.OK);
    }

    @GetMapping("/findAllUserByRegion{idRegion}")
    public ResponseEntity<?> findAllUserByRegion(@PathVariable(name = "idRegion") Long idRegion) {
        return new ResponseEntity<>(utilisateurService.findAllUserByRegion(idRegion), HttpStatus.OK);
    }
    @GetMapping("/findAllRegion")
    public ResponseEntity<?> findAllRegion() {
        return new ResponseEntity<>(utilisateurService.findAllRegion(), HttpStatus.OK);
    }

    @GetMapping("/findAllProfile")
    public ResponseEntity<?> findAllProfile() {
        return new ResponseEntity<>(utilisateurService.findAllProfile(), HttpStatus.OK);
    }
}
