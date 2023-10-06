import java.awt.*;
import java.awt.image.*;
import java.net.URL;
import javax.swing.*;

class DecoratedGameFrame extends JPanel
{
	public ONU leMonde;			// Les pays
	private GameWindow gameSurface;
	private boolean MIDI;				//Musique MIDI ?

	private int compteur;
	private boolean LMouseButtonDown;
	private boolean LMouseButtonUp;
	private boolean RMouseButtonDown;
	private Point point;

	//Variables globales WinG définies dans monde.cpp
	private Image HWinGOldBitmap;	//Ancien handle de Bitmap pour restauration
	private Image HWinGBitmap;		//Handle de Bitmap

	//Informations globales du monde définies dans monde.cpp
	private int levelMaxX;
	private int levelMaxY;
	private int frontieresTab[];

	DecoratedGameFrame(ONU monde, Window parent,
							String title, GameWindow clientWnd,
							boolean trackMenuSelection, int /*Module**/ module)
	{
		setLayout(new BorderLayout());
		leMonde = monde;
		gameSurface = clientWnd;
		add(clientWnd, BorderLayout.CENTER);
	}

	void setupWindow()
	{
		/*MCI_OPEN_PARMS*/int OpenS;

		MIDI = false;
		//Démarrer fichier MIDI:
/*		OpenS.lpstrElementName = "rain.rmi";		//Ouvrir fichier dans MCI
		MCISend(0, MCI_OPEN, MCI_OPEN_ELEMENT, (DWORD) &OpenS);
		MIDI_DeviceID = OpenS.wDeviceID;			//Noter ID de périphérique
		PlayS.dwCallback = (DWORD)HWindow;			//Message à fenêtre
		if (MIDI) MCISend(MIDI_DeviceID, MCI_PLAY, MCI_NOTIFY, (DWORD) &PlayS);//Démarrer MIDI

		TDecoratedFrame::SetupWindow();
		InitTimer();

		//Extension du menu système:
		AppendMenu(GetSystemMenu(FALSE),MF_SEPARATOR,-1,NULL);
		if (MIDI) AppendMenu(GetSystemMenu(FALSE),MF_STRING | MF_CHECKED,SC_MIDI,"&Musique");
		else AppendMenu(GetSystemMenu(FALSE),MF_STRING | MF_UNCHECKED,SC_MIDI,"&Musique");
*/
	}

	void evSysCommand(int cmdType,Point pnt)
	{		//reagiert auf SC_DOUBLE und SC_INFO
/*		TWindow::EvSysCommand(cmdType,Pnt);       	//Appeler le Handle d'origine!
		switch (cmdType)
		{
			case (SC_MIDI) :
				if (MIDI)
				{							//Musique désactivée:
					CheckMenuItem(GetSystemMenu(FALSE),SC_MIDI,MF_BYCOMMAND | MF_UNCHECKED);
					MIDI=FALSE;
					MCISend(MIDI_DeviceID, MCI_STOP, MCI_NOTIFY, (DWORD) &GenericParms);//Arrêter MCI
				}
				else
				{                                		//Musique activée:
					CheckMenuItem(GetSystemMenu(FALSE),SC_MIDI,MF_BYCOMMAND | MF_CHECKED);
					MIDI=TRUE;
					MCISend(MIDI_DeviceID, MCI_PLAY, MCI_NOTIFY, (DWORD) &PlayS); //Reprendre MCI
				}
			break;
		}
*/
	}

	void evGetMinMaxInfo(/*MINMAXINFO*/ int Info)
	{		//réagit à WM_GetMinMaxInfo, empêche l'agrandissement de la fenêtre
/*		TDecoratedFrame::EvGetMinMaxInfo(Info);
		Info.ptMaxTrackSize.x=WinGLargeur+8;
		Info.ptMaxTrackSize.y=WinGHauteur+99;
			//79 = barre de titre + 2 Status bar + Buttons Bar
*/
	}

	void MCISend(int uDeviceID, int uMessage, long dwParam1, long dwParam2)
	{
		long err;
		char errs[];

		if ((uDeviceID>0) || uMessage==0/*MCI_OPEN*/)
		{
/*			err = mciSendCommand(uDeviceID,uMessage,dwParam1,dwParam2);
			mciGetErrorString(err,errs,256);
			if (err)
			{
				MessageBox(errs, "Erreur MCI!", MB_ICONEXCLAMATION);
				MIDI=FALSE;										//Désactiver MIDI
			}
*/
		}
	}

	void initTimer()
	{		//Démarrage du Timer, Quitter en cas d'erreur
/*		if (!SetTimer(254, 50, 0))
		{															//Créer Timer
			MessageBox(												//Signaler erreur
			"Pas de Timer disponible\nPoursuite du programme impossible",
			"Erreur !", MB_ICONSTOP);
			exit(2);                                     //Et quitter
		}
*/
	}

	void haltTimer()
	{		//Arrête le Timer, quitter en cas d'erreur
/*		if (!KillTimer(254))
		{												//Supprimer Timer
			MessageBox("Impossible de supprimer le Timer\nPoursuite du programme impossible",
			"Erreur !", MB_ICONSTOP);                     //Signaler erreur
			exit(2);
		}
*/
	}

	int /*LRESULT*/ MMNotify(int a /*WPARAM*/, /*LPARAM*/int lParam)
	{		//Réagit à la fin de l'échantillon

		haltTimer();                         			//D'abord arrêter Timer
		if (MIDI && lParam == 0/*MCI_NOTIFY_SUCCESSFUL*/)
		{												//Si MIDI joué jusqu'au bout:
/*			PlayS.dwCallback=(DWORD)HWindow;            //Information à la fenêtre
			PlayS.dwFrom=0;                             //Recommencer au début
			MCISend(MIDI_DeviceID,MCI_PLAY, MCI_NOTIFY | MCI_FROM,(DWORD) &PlayS);//Redémarrer MIDI
*/
		};
		initTimer();                                    //Démarrer Timer
		return(0);
	}

	public void paint(Graphics g)
	{
		gameSurface.paint(g);
	}
}