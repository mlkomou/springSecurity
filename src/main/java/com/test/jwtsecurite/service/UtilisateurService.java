/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.jwtsecurite.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import io.tuwindi.demo.config.Response;
import io.tuwindi.demo.model.*;
import io.tuwindi.demo.payload.ChangePassword;
import io.tuwindi.demo.payload.ChangePasswordRequest;
import io.tuwindi.demo.payload.RegistrationForm;
import io.tuwindi.demo.repository.AppRoleRepository;
import io.tuwindi.demo.repository.RegionRepository;
import io.tuwindi.demo.repository.UserMobileRepository;
import io.tuwindi.demo.repository.UtilisateurRepository;
import io.tuwindi.demo.sec.CurrentUser;
import io.tuwindi.demo.sec.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author alhousseini
 */
@Service
public class UtilisateurService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;


    @Autowired
    private UserMobileRepository userMobileRepository;
    @Autowired
    private AppRoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    public Response signUp(RegistrationForm data) {
        if (utilisateurRepository.existsByLogin(data.getLogin())) {
            return Response.error("Ce nom d'utilisateur est déjà pris!");
        }

        if (utilisateurRepository.existsByEmail(data.getEmail())) {
            return Response.error("Cette adresse e-mail déjà utilisée!");

        }
        Utilisateur u = new Utilisateur();
        u.setLogin(data.getLogin());
        u.setPassword(bCryptPasswordEncoder.encode(data.getPassword()));
        u.setEmail(data.getEmail());
        u.setNom(data.getNom());
        u.setPrenom(data.getPrenom());
        u.setTelephone(data.getTelephone());

        Optional<AppRole> role;
        role = roleRepository.findById(data.getRole());
        if (!role.isPresent()) return Response.error("Impossible de trouver ce rôle avec l'id :" + data.getRole());
        u.getRoles().add(role.get());
        Utilisateur utilisateur = utilisateurRepository.save(u);
        return Response.with(utilisateur, "Utilisateur enregistré avec succès");
    }


    public Response findCurrentUser(UserPrincipal currentUser) {
        return Response.with(utilisateurRepository.findUtilisateurById(currentUser.getId()), "Utilisateur trouvé!");
    }




    /**
     * find all utilisateur
     *
     * @return
     */


    public Response findAllRegion() {
        List<Region> regions;
        try {
            regions = regionRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.error("Une erreur est survenue lors de l'opération");
        }
        //findFiles()
        return Response.with(regions, regions.size() + " Liste des  regions ");
    }
    public Response findAllUserByRegion(Long idRegion) {
        List<ModelUserMobile> utilisateursMobiles;
        try {
            utilisateursMobiles = userMobileRepository.findUtilisateurByRegion(idRegion);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.error("Une erreur est survenue lors de l'opération");
        }
        //findFiles()
        return Response.with(utilisateursMobiles, utilisateursMobiles.size() + " Liste des utilisateurs mobile par regions ");
    }

    /**
     * @param utilisateur
     * @return
     * @desc saving new utilisateur
     */
    public Response save(Utilisateur utilisateur) {
        try {
            if (utilisateurRepository.checkNewUtilisateur(utilisateur.getLogin()) != null) {
                return Response.error("Ce login existe déjà");
            }
            utilisateur.setPassword(utilisateur.getPassword());
            utilisateurRepository.saveAndFlush(utilisateur);
//            List<File> files = utilisateur.getFiles();
//            if (files != null && !files.isEmpty()) {
//                files.forEach((file) -> {
//                    fileService.createFile(file, utilisateur);
//                });
//            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.error("Une erreur est survenue lors de l'opération");
        }
        return Response.with(utilisateur, "Utilisateur enregistré avec succès");
    }

    /**
     * @param utilisateur
     * @return
     * @desc updating a utilisateur
     */
    public Response update(Utilisateur utilisateur) {
        try {
//           
            utilisateur.setDateModification(new Date());
            utilisateurRepository.saveAndFlush(utilisateur);
//            joinFiles(utilisateur);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.error("Une erreur est survenue lors de l'opération");
        }
        return Response.with(utilisateur, "Utilisateur modifié avec succès");
    }

    /**
     * @param id
     * @return
     * @desc desactivation of a utilisateur
     */
    public Response desactivate(Long id) {
        try {
            Utilisateur pv = utilisateurRepository.findUtilisateurById(id);
            if (pv == null) {
                return Response.error("Impossible de désactiver ce utilisateur");
            }
            pv.setEnabled(false);
            utilisateurRepository.saveAndFlush(pv);
            return Response.with(pv, "Utilisateur désactivé avec succès");
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.error("Une erreur est survenue lors de l'opération");
        }
    }

    /**
     * @param id
     * @return
     * @desc activation of a utilisateur
     */
    public Response activate(Long id) {
        try {
            Utilisateur pv = utilisateurRepository.findUtilisateurById(id);
            if (pv == null) {
                return Response.error("Impossible d'activer ce utilisateur");
            }
            pv.setEnabled(true);
            utilisateurRepository.saveAndFlush(pv);
            return Response.with(pv, "Utilisateur activé avec succès");
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.error("Une erreur est survenue lors de l'opération");
        }
    }

    public Response findAllProfile() {
        List<AppRole> appRoles;
        try {

            appRoles = roleRepository.findAppRole();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.error("Une erreur est survenue lors de l'opération");
        }
        return Response.with(appRoles, appRoles.size() + " Roles(s) utilisateur trouvé(s)");
    }


    public Response findAllUser() {
        List<Utilisateur> utilisateurs;
        try {
            utilisateurs = utilisateurRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.error("Une erreur est survenue lors de l'opération");
        }
        //findFiles()
        return Response.with(utilisateurs, utilisateurs.size() + " utilisateur(s) agent(s) de santé trouvé(s)");
    }

    //save for import
    public Response saveFileExcel(List<UtilisateurMobile> utilisateurMobile) {
//        Dashboard dashboardSave = new Dashboard();
        System.out.println("test size ======>" + utilisateurMobile.size());
        try {
            for (int i = 0; i < utilisateurMobile.size(); i++) {
                userMobileRepository.saveAndFlush(utilisateurMobile.get(i));

            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.error("Une erreur est survenue lors de l'opération");
        }
        return Response.with(utilisateurMobile, " fichier excel utilisateur mobile enregistrer avec succès  ");
    }
}
