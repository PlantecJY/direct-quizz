package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.*;
import forms.SequenceForm;
import entities.*;
import forms.ParticipantForm;
import forms.QuestionForm;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 *
 * @author JYP
 */
//@WebServlet(name = "SaveHelpStatus", urlPatterns = {"/SaveHelpStatus"})
public class SaveHelpStatus extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public static final String ACCES_PUBLIC     = "/formulaire_connexion.jsp";
    public static final String ACCES_RESTREINT  = "/restricted";
    public static final String HELP_STATUS = "helpStatus";


    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
                // récupération de l'état
                String etat = request.getParameter("etat");
                
                // mise en session
                HttpSession session = request.getSession();
                session.setAttribute(HELP_STATUS, etat);


   }

    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
        
}