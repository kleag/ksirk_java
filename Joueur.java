import java.awt.*;
import java.awt.image.*;
import java.net.URL;

public class Joueur
{
	String nom;
	int nbPays;
	int nbArmeesDisponibles;
	int nbAttaque;
	int nbDefense;

	public Joueur(String nomJoueur, int nbArmees)
	{
		nom = nomJoueur;
		nbArmeesDisponibles = nbArmees;
		nbPays = 0;
		nbAttaque = 0;
	}
}