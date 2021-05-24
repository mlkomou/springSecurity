/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.jwtsecurite.model;


import io.tuwindi.demo.config.Response;
import io.tuwindi.demo.model.Utilisateur;
import io.tuwindi.demo.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author alhousseini
 */
@Service
public class AuthService {
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
      public Response login(String login, String password) {
          Utilisateur user;
        try {
            user = utilisateurRepository.findByLoginAndPassword(login, password);
            if (user == null) {
                return Response.error("Login ou mot de passe incorrect");
            }
            //retreive user rigths with his profile
//            if (user.getProfile() !=null) {
//                List<Droit> droits = droitRepository.findDroitByProfile(user.getProfile().getId());
//                user.getProfile().setDroits(droits);
//            }
             

        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.error("Une erreur est survenue lors de l'opération");
        }
        return Response.with(user, "Vous êtes authentifié avec succès");
    }

    /**
     * @desc change user password
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public Response updatePassword(Long id, String oldPassword, String newPassword) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findUtilisateurById(id);
            if (utilisateur == null) {
                return Response.error("Impossible de changer le mot de passe");
            }
//            if (!MD5Encoder.testPassword(oldPassword, utilisateur.getPassword())) {
//                return Response.error("Votre ancien mot de passe est incorrect");
//            }
            utilisateur.setPassword(newPassword);
            utilisateurRepository.saveAndFlush(utilisateur);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.error("Une erreur est survenue lors de l'opération");
        }
        return Response.success("Votre mot de passe a été changé avec succès");
    }

    public Response resetPassword(Long id, String newPassword) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findUtilisateurById(id);
            if (utilisateur == null) {
                return Response.error("Impossible de changer le mot de passe");
            }
            utilisateur.setPassword(newPassword);
            utilisateurRepository.saveAndFlush(utilisateur);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.error("Une erreur est survenue lors de l'opération");
        }
        return Response.success("Mot de passe a été réinitialié avec succès");
    }
}
