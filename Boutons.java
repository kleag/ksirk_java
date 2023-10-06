#include ".\sources\risk.h"

void TGameApp::CmExitGame(void)
{
	exit(0);
}

void TGameApp::CmNewGame(void)
{
	SetupJoueurs();
	Etat = INIT;
}

void TGameApp::CmInfo(void)
{
	Frame-> HaltTimer();          	        //Arrêt du Timer
	Dialog = new TDialog(Frame, "IDD_INFO");//Afficher la boîte de dialogue Info
	Dialog->Execute();                      //Exécuter
	delete Dialog;                     		//Et libérer
	Frame-> InitTimer();                    //Et redémarrer le Timer
}

void TGameApp::CmRecycl(void)
{
	EnleveBoutonsRecycle();
	AfficheBoutonJoueurSuivant();
	Etat = INTERMEDE;
}

void TGameApp::CmRecyclFini(void)
{
	EnleveBoutonsRecycle();
	Etat = ATTEND;
	AfficheBoutonsJeuNormal();
}

void TGameApp::CmJoueurSuivant(void)
{
	int i;

	if ((Etat == INTERMEDE)||(Etat == NOUVARMEES))
	{
		if ((*Joueurs)[JoueurCourant]-> NbArmeesDisponibles)
			MessageBox(0, "Vous devez distribuer\ntoutes vos armées", "Risk",
							MB_ICONINFORMATION|MB_OK);
		else
		{
			if (JoueurCourant == NbJoueurs-1)
			{
				EnleveBoutonJoueurSuivant();
				AfficheBoutonsRecycle();
				JoueurCourant = 0;
				lstrcpy(Status2Text, "Echanger à nouveau des armées ou continuer ?");
				((TTextGadget *)(*StatusBar2)[0])-> SetText(Status2Text);
			}
			else
			{
				JoueurCourant++;
				wsprintf(Status2Text, "%s : %d armées à placer",
							(*Joueurs)[JoueurCourant]-> Nom,
							(*Joueurs)[JoueurCourant]-> NbArmeesDisponibles);
				if (lstrcmp(((TTextGadget *)(*StatusBar2)[0])-> GetText(), Status2Text))
						((TTextGadget *)(*StatusBar2)[0])-> SetText(Status2Text);
			}
		}
	}
	else
	{
		if (JoueurCourant == NbJoueurs-1)
		{
			EnleveBoutonsJeuNormal();
			DistribueArmees();
			AfficheBoutonJoueurSuivant();
			JoueurCourant = 0;
			wsprintf(Status2Text, "%s : %d armées à placer",
							(*Joueurs)[JoueurCourant]-> Nom,
							(*Joueurs)[JoueurCourant]-> NbArmeesDisponibles);
			((TTextGadget *)(*StatusBar2)[0])-> SetText(Status2Text);
			NbArmeesDeplacees = 0;
			PremierPays = DeuxiemePays = 45;
			NDT = NAT = 0;
			Client-> MovingCanons-> Flush(TShouldDelete::Delete);		// Canons en déplacement
			Client-> MovingChevaux-> Flush(TShouldDelete::Delete);		// Chevaux en déplacement
			Client-> MovingSoldats-> Flush(TShouldDelete::Delete);		// Soldats en déplacement
			Client-> MovingCombattants-> Flush(TShouldDelete::Delete);	// Combattants en déplacement
			Client-> AnimCombattants-> Flush(TShouldDelete::Delete);		// Combattants combattant
			Client-> AnimExplosants-> Flush(TShouldDelete::Delete);		// Combattants explosant
			Etat = ATTEND;
			for (i = 0; i <= 44; i++) LeMonde-> Pays[i].NbArmeesAjoutees = 0;
			Etat = NOUVARMEES;
		}
		else
		{
			JoueurCourant++;
			wsprintf(Status2Text, "%s : ", (*Joueurs)[JoueurCourant]-> Nom);
			((TTextGadget *)(*StatusBar2)[0])-> SetText(Status2Text);
			NbArmeesDeplacees = 0;
			PremierPays = DeuxiemePays = 45;
			NDT = NAT = 0;
			Client-> MovingCanons-> Flush(TShouldDelete::Delete);		// Canons en déplacement
			Client-> MovingChevaux-> Flush(TShouldDelete::Delete);		// Chevaux en déplacement
			Client-> MovingSoldats-> Flush(TShouldDelete::Delete);		// Soldats en déplacement
			Client-> MovingCombattants-> Flush(TShouldDelete::Delete);	// Combattants en déplacement
			Client-> AnimCombattants-> Flush(TShouldDelete::Delete);		// Combattants combattant
			Client-> AnimExplosants-> Flush(TShouldDelete::Delete);		// Combattants explosant
			Etat = ATTEND;
		}
	}
}

void TGameApp::CmAttaque1(void)
{
	if (Etat == ATTEND)
	{
		Etat = ATTAQUE;
		EnleveBoutonsJeuNormal();
		AfficheBoutonAnnuler();
		(*Joueurs)[JoueurCourant]-> NbAttaque = 1;
		lstrcpy(Status2Text, "Attaque avec 1 armée : Désignez les belligérants");
		((TTextGadget *)(*StatusBar2)[0])-> SetText(Status2Text);
	}
}

void TGameApp::CmAttaque2(void)
{
	if (Etat == ATTEND)
	{
		Etat = ATTAQUE;
		EnleveBoutonsJeuNormal();
		AfficheBoutonAnnuler();
		(*Joueurs)[JoueurCourant]-> NbAttaque = 2;
		lstrcpy(Status2Text, "Attaque avec 2 armées : Désignez les belligérants");
		((TTextGadget *)(*StatusBar2)[0])-> SetText(Status2Text);
	}
}

void TGameApp::CmAttaque3(void)
{
	if (Etat == ATTEND)
	{
		Etat = ATTAQUE;
		EnleveBoutonsJeuNormal();
		AfficheBoutonAnnuler();
		(*Joueurs)[JoueurCourant]-> NbAttaque = 3;
		lstrcpy(Status2Text, "Attaque avec 3 armées : Désignez les belligérants");
		((TTextGadget *)(*StatusBar2)[0])-> SetText(Status2Text);
	}
}

void TGameApp::CmDefense1(void)
{
	EnleveBoutonsDefense();
	(*Joueurs)[LeMonde-> Pays[DeuxiemePays].AppartientA]-> NbDefense = 1;
	Etat = COMBAT_AMENE;
	Client-> InitBougeCombat(PremierPays, DeuxiemePays);
}

void TGameApp::CmDefense2(void)
{
	EnleveBoutonsDefense();
	(*Joueurs)[LeMonde-> Pays[DeuxiemePays].AppartientA]-> NbDefense = 2;
	Etat = COMBAT_AMENE;
	Client-> InitBougeCombat(PremierPays, DeuxiemePays);
}

void TGameApp::CmEnvahit1(void)
{
	if (LeMonde-> Pays[PremierPays].NbArmees > 1)
	{
		Client-> InitBougeArmees(1, PremierPays, DeuxiemePays);
		if (Etat == DEPLACE2) NbArmeesDeplacees++;
	}
}

void TGameApp::CmEnvahit5(void)
{
	if (LeMonde-> Pays[PremierPays].NbArmees > 5)
	{
		Client-> InitBougeArmees(5, PremierPays, DeuxiemePays);
		if (Etat == DEPLACE2) NbArmeesDeplacees+=5;
	}
}

void TGameApp::CmEnvahit10(void)
{
	if (LeMonde-> Pays[PremierPays].NbArmees > 10)
	{
		Client-> InitBougeArmees(10, PremierPays, DeuxiemePays);
		if (Etat == DEPLACE2) NbArmeesDeplacees+=10;
	}
}

void TGameApp::CmEnvFini(void)
{
	EnleveBoutonsEnvahit();
	if (Etat == ENVAHIT)
	{
		AfficheBoutonsJeuNormal();
		wsprintf(Status2Text, "%s :", (*Joueurs)[JoueurCourant]-> Nom);
		((TTextGadget *)(*StatusBar2)[0])-> SetText(Status2Text);
		Etat = ATTEND;
	}
	else if (Etat == DEPLACE2)
	{
		EnleveBoutonAnnuler();
		AfficheBoutonsJeuNormal();
		CmJoueurSuivant();
	}
}

void TGameApp::CmRetraite1(void)
{
	if (LeMonde-> Pays[DeuxiemePays].NbArmees > 1)
	{
		Client-> InitBougeArmees(1, DeuxiemePays, PremierPays);
		if (Etat == DEPLACE2) NbArmeesDeplacees--;
	}
}

void TGameApp::CmRetraite5(void)
{
	if (LeMonde-> Pays[DeuxiemePays].NbArmees > 5)
	{
		Client-> InitBougeArmees(5, DeuxiemePays, PremierPays);
		if (Etat == DEPLACE2) NbArmeesDeplacees-= 5;
	}
}

void TGameApp::CmRetraite10(void)
{
	if (LeMonde-> Pays[DeuxiemePays].NbArmees > 10)
	{
		Client-> InitBougeArmees(10, DeuxiemePays, PremierPays);
		if (Etat == DEPLACE2) NbArmeesDeplacees-= 10;
	}
}

void TGameApp::CmDeplace(void)
{
	Etat = DEPLACE;
	EnleveBoutonsJeuNormal();
	AfficheBoutonAnnuler();
}

void TGameApp::CmAnnuler(void)
{
	if ((Etat == DEPLACE) || (Etat == ATTAQUE))
	{
		Etat = ATTEND;
		EnleveBoutonAnnuler();
		AfficheBoutonsJeuNormal();
		wsprintf(Status2Text, "%s :", (*Joueurs)[JoueurCourant]-> Nom);
		((TTextGadget *)(*StatusBar2)[0])-> SetText(Status2Text);
	}
	else if (Etat == DEPLACE2)
	{
		EnleveBoutonsEnvahit();
		Etat = DEPLACE;
		if (NbArmeesDeplacees < 0)
		{
			LeMonde-> Pays[PremierPays].NbArmees-= NbArmeesDeplacees;
			LeMonde-> Pays[DeuxiemePays].NbArmees+= NbArmeesDeplacees;
			LeMonde-> Pays[PremierPays].AjouteArmees(Client-> GfxPool, Client-> BackGnd);
			LeMonde-> Pays[DeuxiemePays].AjouteArmees(Client-> GfxPool, Client-> BackGnd);
			NbArmeesDeplacees = 0;
		}
		if (NbArmeesDeplacees > 0)
		{
			LeMonde-> Pays[PremierPays].NbArmees+= NbArmeesDeplacees;
			LeMonde-> Pays[DeuxiemePays].NbArmees-= NbArmeesDeplacees;
			LeMonde-> Pays[PremierPays].AjouteArmees(Client-> GfxPool, Client-> BackGnd);
			LeMonde-> Pays[DeuxiemePays].AjouteArmees(Client-> GfxPool, Client-> BackGnd);
			NbArmeesDeplacees = 0;
		}
	}
}

void TGameApp::AfficheBoutonJoueurSuivant(void)
{
	Actions-> Insert(*new TButtonGadget(IDB_JOUEURSUIVANT, CM_JOUEURSUIVANT));
	Actions-> LayoutSession();
}

void TGameApp::EnleveBoutonJoueurSuivant(void)
{
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_JOUEURSUIVANT)));
	Actions-> LayoutSession();
}

void TGameApp::AfficheBoutonsRecycle(void)
{
	Actions-> Insert(*new TButtonGadget(IDB_RECYCL, CM_RECYCL));
	Actions-> Insert(*new TButtonGadget(IDB_RECYCLFINI, CM_RECYCLFINI));
	Actions-> LayoutSession();
}

void TGameApp::EnleveBoutonsRecycle(void)
{
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_RECYCL)));
	Actions-> LayoutSession();
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_RECYCLFINI)));
	Actions-> LayoutSession();
}

void TGameApp::AfficheBoutonsJeuNormal(void)
{
	Actions-> Insert(*new TButtonGadget(IDB_JOUEURSUIVANT, CM_JOUEURSUIVANT));
	Actions-> Insert(*new TButtonGadget(IDB_ATTAQUE1, CM_ATTAQUE1));
	Actions-> Insert(*new TButtonGadget(IDB_ATTAQUE2, CM_ATTAQUE2));
	Actions-> Insert(*new TButtonGadget(IDB_ATTAQUE3, CM_ATTAQUE3));
	Actions-> Insert(*new TButtonGadget(IDB_DEPLACE, CM_DEPLACE));
	Actions-> LayoutSession();
}

void TGameApp::EnleveBoutonsJeuNormal(void)
{
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_JOUEURSUIVANT)));
	Actions-> LayoutSession();
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_ATTAQUE1)));
	Actions-> LayoutSession();
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_ATTAQUE2)));
	Actions-> LayoutSession();
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_ATTAQUE3)));
	Actions-> LayoutSession();
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_DEPLACE)));
	Actions-> LayoutSession();
}

void TGameApp::AfficheBoutonsDefense(void)
{
		Actions-> Insert(*new TButtonGadget(IDB_DEFENSE1, CM_DEFENSE1));
		Actions-> Insert(*new TButtonGadget(IDB_DEFENSE2, CM_DEFENSE2));
		Actions-> LayoutSession();
}

void TGameApp::EnleveBoutonsDefense(void)
{
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_DEFENSE1)));
	Actions-> LayoutSession();
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_DEFENSE2)));
	Actions-> LayoutSession();
}

void TGameApp::AfficheBoutonsEnvahit(void)
{
	Actions-> Insert(*new TButtonGadget(IDB_ENVAHIT1, CM_ENVAHIT1));
	Actions-> Insert(*new TButtonGadget(IDB_ENVAHIT5, CM_ENVAHIT5));
	Actions-> Insert(*new TButtonGadget(IDB_ENVAHIT10, CM_ENVAHIT10));
	Actions-> Insert(*new TButtonGadget(IDB_ENVFINI, CM_ENVFINI));
	Actions-> Insert(*new TButtonGadget(IDB_RETRAITE1, CM_RETRAITE1));
	Actions-> Insert(*new TButtonGadget(IDB_RETRAITE5, CM_RETRAITE5));
	Actions-> Insert(*new TButtonGadget(IDB_RETRAITE10, CM_RETRAITE10));
	Actions-> LayoutSession();
}

void TGameApp::EnleveBoutonsEnvahit(void)
{
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_ENVAHIT1)));
	Actions-> LayoutSession();
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_ENVAHIT5)));
	Actions-> LayoutSession();
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_ENVAHIT10)));
	Actions-> LayoutSession();
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_ENVFINI)));
	Actions-> LayoutSession();
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_RETRAITE1)));
	Actions-> LayoutSession();
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_RETRAITE5)));
	Actions-> LayoutSession();
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_RETRAITE10)));
	Actions-> LayoutSession();
}

void TGameApp::AfficheBoutonAnnuler(void)
{
	Actions-> Insert(*new TButtonGadget(IDB_ANNULER, CM_ANNULER));
	Actions-> LayoutSession();
}

void TGameApp::EnleveBoutonAnnuler(void)
{
	delete Actions-> Remove(*(Actions-> GadgetWithId(CM_ANNULER)));
	Actions-> LayoutSession();
}

