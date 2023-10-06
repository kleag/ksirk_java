#ifndef _DIALOGS_H
#define _DIALOGS_H

#include <owl\dialog.h>

class TDialogNbJoueurs : public TDialog
{
public:
	TDialogNbJoueurs(TWindow *, int&);
	~TDialogNbJoueurs();

private:
	int& INbJoueurs;

	BOOL CanClose();
};

class TDialogSetupJoueur : public TDialog
{
public:
	TDialogSetupJoueur(TWindow* parent, char far *nom, int& numero, TModule* module = 0);

private:
	char far *Nom;
	int& Numero;

	void SetupWindow();
	BOOL CanClose();
};

#endif

#include <owl\owlpch.h>
#include <owl\applicat.h>
#include <owl\button.h>
#include ".\sources\dialogs.h"
#include ".\sources\risk.rh"

TDialogNbJoueurs::TDialogNbJoueurs(TWindow *Window, int& NbJoueurs)
			: TDialog(Window, IDD_NBJOUEURS), INbJoueurs(NbJoueurs)
{
}

TDialogNbJoueurs::~TDialogNbJoueurs()
{
}

BOOL
TDialogNbJoueurs::CanClose()
{
	INbJoueurs = GetDlgItemInt(IDC_NBJOUEURS);
	return TRUE;
}

TDialogSetupJoueur::TDialogSetupJoueur(TWindow* parent,
								char far *nom, int& numero, TModule* module)
			: TDialog(parent, "IDD_SETUPJOUEUR", module), Numero(numero)
{
	Nom = nom;
}

void
TDialogSetupJoueur::SetupWindow()
{
	char Mes[35];

	wsprintf(Mes, "Joueur n° %d, entrez votre nom:", Numero+1);
	SetDlgItemText(IDT_INVITE, Mes);
}

BOOL
TDialogSetupJoueur::CanClose()
{
	GetDlgItemText(IDC_NOM, Nom, 14);
	return TRUE;
}
