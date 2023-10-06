import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.URL;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.metal.*;
import javax.swing.JOptionPane;
import javax.swing.border.*;

public class Risk
		extends JFrame
		implements ItemListener, ActionListener, Runnable, MouseMotionListener, MouseListener
{
	Random randGen;
	int etat;
	String status1Text, status2Text;
	int nbArmeesDeplacees;
	int nbJoueurs;
	int joueurCourant;
	int premierPays, deuxiemePays;
	int NDT, NAT;
	ONU leMonde;				// Les pays
	SpritesSet joueurs;
	Dialog dialog;
	DecoratedGameFrame frame;
  static GameWindow gameWindow;
	ToolBar actions;
	JTextField statusBar1;
	JTextField statusBar2;
	GridBagLayout gridBag;
	GridBagConstraints bagConstraints;
  volatile Thread myThread;
	private Point mousePosition;
  RiskButton distributionArmeesButton, distributionArmeesFinieButton;
  RiskButton attackOneButton, attackTwoButton, attackThreeButton;
  RiskButton moveArmiesButton, joueurSuivantButton, annulerButton;
  RiskButton defendOneButton, defendTwoButton, moveOneButton;
  RiskButton moveFiveButton, moveTenButton, moveBackOneButton;
  RiskButton moveBackFiveButton, moveBackTenButton, moveFinishButton;

	public boolean test = false;
	public boolean lMouseButtonDown = false ;
	public boolean lMouseButtonUp = false ;
	public boolean rMouseButtonDown = false ;

	Point point;

  AudioClip soundRoule;  //Pointeur sur échantillon: Pas
  AudioClip soundCrash;  // Pointeur sur échantillon: Joueur mort
  AudioClip soundCanon;  // Pointeur sur échantillon: contact avec Ennemi

	//Informations globales du monde définies dans la classe ONU


	public Risk()
	{
		randGen = new Random();
		//Charger sons de ressource
//    soundCrash = (new Applet()).getAudioClip(Object.class.getResource("Sons/Crash.wav"));
//    soundCanon = (new Applet()).getAudioClip(Object.class.getResource("Sons/Canon.wav"));

//    soundCrash.play();

    NDT = NAT = 0;
		etat = Etat.ATTEND;
		premierPays = 45;
		deuxiemePays = 45;
		status1Text = "";
		status2Text = "";
		mousePosition = new Point(0,0);
		getContentPane().setLayout(new BorderLayout());

		getAccessibleContext().setAccessibleDescription("A game by Gaël de Chalendar !");
		JOptionPane.setRootFrame(this);
		WindowListener l = new WindowAdapter() {public void windowClosing(WindowEvent e) {System.exit(0);}};
		addWindowListener(l);
		setLocation(0,0);

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		initMainWindow();
    setSize(gameWindow.backGnd.largeur+8, gameWindow.backGnd.hauteur+statusBar1.getHeight()+statusBar2.getHeight()+actions.getHeight()+103);

    addMouseMotionListener(gameWindow);
		addMouseMotionListener(this);
		addMouseListener(this);

		show();

    myThreadStart();
	}

	void initMainWindow()
  {

    gameWindow = new GameWindow();
    leMonde = gameWindow.leMonde;

    frame = new DecoratedGameFrame(leMonde, null, "Risk", gameWindow, true, 0);

		// Construct and build a control bar
		//
		actions = new ToolBar(this);
		actions.setSize(new Dimension(this.getWidth(), actions.getHeight()));

//		frame.setIcon(new ImageIcon(getToolkit().createImage("Images/Soldat à genoux 1.jpg")));

		// Construct, build and insert two status bars into the frame
		//
		statusBar1 = new JTextField("aaaaaaa");
		statusBar1.setUI(new MetalTextFieldUI());
		statusBar2 = new JTextField("bbbbb");
		statusBar2.setUI(new MetalTextFieldUI());

		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BorderLayout());
		statusPanel.add(statusBar1, BorderLayout.NORTH);
		statusPanel.add(statusBar2,BorderLayout.SOUTH);

		getContentPane().add(actions, BorderLayout.NORTH);
		getContentPane().add(frame, BorderLayout.CENTER); // ????
		getContentPane().add(statusPanel, BorderLayout.SOUTH);
	}

  void playWave(int canal,int sound) {}

	void distribueArmees()
	{
		int i;

		for (i = 0; i < nbJoueurs; i++)
		{
			((Joueur)(Joueur)(joueurs.toArray())[i]).nbArmeesDisponibles = nbNouvArmees(i);
		}
	}

	int nbNouvArmees(int joueur)
	{
		int i, res = 0;

		for (i = 0; i<= 44; i++)
			if (leMonde.pays[i].appartientA == joueur) res++;
		if ((res/3) < 3) return 3;
		else return (res/3);
	}

	void evTimer()
	{
    myThreadStop();
    gameWindow.evenementTimer(true);
		if (lMouseButtonDown)
		{
			lMouseButtonDown = false;
			evenementLButtonDown(point);
		}
		if (lMouseButtonUp)
		{
			lMouseButtonUp = false;
			evenementLButtonUp(point);
		}
		if (rMouseButtonDown)
		{
			rMouseButtonDown = false;
			evenementRButtonDown(point);
		}
    if ((etat == Etat.COMBAT_AMENE) && (gameWindow.movingCombattants.isEmpty()))
		{
      etat = Etat.COMBAT_ANIME;
      gameWindow.animCombat();
			//playWave(1, SoundCanon);
		}
    if ((etat == Etat.COMBAT_ANIME) && (gameWindow.animCombattants.isEmpty()))
		{
			etat = Etat.EXPLOSION_ANIME;
			resoudAttaque();
      System.out.println("NDT = "+NDT+" NAT = "+NAT);
      if ((NDT != 0)&&(NAT != 0)) gameWindow.animExplosion(2, premierPays, deuxiemePays);
      else if (NAT != 0) gameWindow.animExplosion(0, premierPays, deuxiemePays);
      else if (NDT != 0) gameWindow.animExplosion(1, premierPays, deuxiemePays);
			//PlayWave(2, SoundCrash);
		}
    if ((etat == Etat.EXPLOSION_ANIME) && (gameWindow.animExplosants.isEmpty()))
		{
			etat = Etat.COMBAT_RETOUR;
      if ((NDT != 0)&&(NAT != 0)) gameWindow.initRameneCombat(2, premierPays, deuxiemePays);
      else if (NAT != 0) gameWindow.initRameneCombat(0, premierPays, deuxiemePays);
      else if (NDT != 0) gameWindow.initRameneCombat(1, premierPays, deuxiemePays);
			else ;
		}
    if ((etat == Etat.COMBAT_RETOUR) && (gameWindow.movingCombattants.isEmpty()))
		{
      gameWindow.combattants.flush();
      gameWindow.movingCombattants.flush();
      gameWindow.animCombattants.flush();
      gameWindow.animExplosants.flush();
			etat = Etat.FINATTAQUE;
      finAttaque();
		}
    myThreadStart();
	}

	void evenementLButtonDown(Point point)
	{
		int paysClique;

    paysClique = gameWindow.cliqueDans(point);
		if (etat == Etat.INIT)
		{
			if (leMonde.pays[paysClique].appartientA == joueurCourant)
			{
				(((Joueur)((Joueur)(joueurs.toArray())[joueurCourant]))).nbArmeesDisponibles--;
				leMonde.pays[paysClique].nbArmees++;
				leMonde.pays[paysClique].
              ajouteArmees(gameWindow.gfxPool, gameWindow.backGnd);
        gameWindow.evenementTimer(true);
				if ((((Joueur)((Joueur)(joueurs.toArray())[joueurCourant]))).nbArmeesDisponibles == 0)
				{
					if (joueurCourant == nbJoueurs-1)
					{
						joueurCourant = 0;
						afficheBoutonsRecycle();
						status2Text =  ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + " :";
            if (statusBar2.getText() != status2Text)
              statusBar2.setText(status2Text);
						etat = Etat.ATTEND;
					}
					else joueurCourant++;
				}
				if (etat == Etat.INIT) status2Text = ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom
											+ " : " + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbArmeesDisponibles
											+ " armées à placer";
					statusBar2.setText(status2Text);
				}
			}
			else if ( ((etat == Etat.INTERMEDE) || (etat == Etat.NOUVARMEES) )
				&& (leMonde.pays[paysClique].appartientA == joueurCourant)
				&& (((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbArmeesDisponibles > 0))
			{
			(((Joueur)((Joueur)(joueurs.toArray())[joueurCourant]))).nbArmeesDisponibles--;
			if (etat == Etat.NOUVARMEES) leMonde.pays[paysClique].nbArmeesAjoutees++;
			leMonde.pays[paysClique].nbArmees++;
			leMonde.pays[paysClique].
              ajouteArmees(gameWindow.gfxPool, gameWindow.backGnd);
      gameWindow.evenementTimer(true);
			status2Text = ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + " : "
							+ ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbArmeesDisponibles + " armées à placer";
			if (statusBar2.getText() != status2Text)
				statusBar2.setText(status2Text);
		}
		else if (etat == Etat.ATTAQUE)
		{
			premierPays = paysClique;
			if (leMonde.pays[premierPays].nbArmees
				<= ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbAttaque)
			{
				status2Text = "Il n'y a que " + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbAttaque
					+ " armées en " + leMonde.pays[premierPays].nom + " !";
				statusBar2.setText(status2Text);
				enleveBoutonAnnuler();
				afficheBoutonsJeuNormal();
				etat = Etat.ATTEND;
			}
			else
			{
				status2Text =  ""+ ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + " attaque depuis " + leMonde.pays[premierPays].nom + " avec " + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbAttaque + " armées";
				statusBar2.setText(status2Text);
				etat = Etat.ATTAQUE2;
			}
		}
		else if (etat == Etat.DEPLACE) premierPays = paysClique;
	}

	void evenementLButtonUp(Point point)
	{
		int i;

		if (etat == Etat.ATTAQUE2)
		{
      deuxiemePays = gameWindow.cliqueDans(point);
			i = leMonde.pays[deuxiemePays].appartientA;
			if ((premierPays == deuxiemePays)
				|| (premierPays == 45) && (deuxiemePays == 45)
				|| (leMonde.pays[premierPays].communiqueAvec[deuxiemePays]==0)
				|| (leMonde.pays[premierPays].appartientA != joueurCourant)
				|| (leMonde.pays[premierPays].appartientA ==
									leMonde.pays[deuxiemePays].appartientA))
			{
				status2Text =  "Voyons " + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + " ! Cette attaque est impossible !";
				statusBar2.setText(status2Text);
				enleveBoutonAnnuler();
				afficheBoutonsJeuNormal();
				etat = Etat.ATTEND;
			}
			else if ((leMonde.pays[deuxiemePays].nbArmees > 1)
				&& ((((Joueur)(joueurs.toArray())[joueurCourant])).nbAttaque >= 2))
			{
				status2Text =  "" +	((Joueur)(joueurs.toArray())[i]).nom + " avec combien d'armées défendez-vous " + ((Pays)(leMonde.pays[deuxiemePays])).nom + " ?";
				statusBar2.setText(status2Text);
				enleveBoutonAnnuler();
				afficheBoutonsDefense();
				etat = Etat.ATTENDDEFENSE;
			}
			else
			{
				((Joueur)((joueurs.toArray())[i])).nbDefense = 1;
				status2Text =  "(" + leMonde.pays[premierPays].nom + ", " + (((Joueur)((joueurs.toArray())[joueurCourant])).nbAttaque) + ") <.(" + leMonde.pays[deuxiemePays].nom + ", " + (( (Joueur) (( joueurs.toArray() )[i]) ).nbDefense) + ")";
				statusBar2.setText(status2Text);
				enleveBoutonAnnuler();
				etat = Etat.COMBAT_AMENE;
        gameWindow.initBougeCombat(premierPays, deuxiemePays);
			}
		}
		else if (etat == Etat.DEPLACE)
		{
        deuxiemePays = gameWindow.cliqueDans(point);
			if ((leMonde.pays[premierPays].appartientA == joueurCourant)
				&& (leMonde.pays[deuxiemePays].appartientA == joueurCourant)
				&& (leMonde.pays[premierPays].communiqueAvec[deuxiemePays] == 1))
			{
				nbArmeesDeplacees = 0;
				etat = Etat.DEPLACE2;
				afficheBoutonsEnvahit();
			}
		}
	}

	void evenementRButtonDown(Point point)
	{
		int paysClique;

		if ( (etat == Etat.INTERMEDE) || (etat == Etat.NOUVARMEES) )
		{
      paysClique = gameWindow.cliqueDans(point);
			if ((leMonde.pays[paysClique].appartientA == joueurCourant)
				&& (leMonde.pays[paysClique].nbArmees > 1)
				&& ((etat == Etat.INTERMEDE)
					|| ((etat == Etat.NOUVARMEES)
						&& (leMonde.pays[paysClique].nbArmeesAjoutees>0))))
			{
				((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbArmeesDisponibles++;
				if (etat == Etat.NOUVARMEES) leMonde.pays[paysClique].nbArmeesAjoutees--;
				leMonde.pays[paysClique].nbArmees--;
				leMonde.pays[paysClique].
                ajouteArmees(gameWindow.gfxPool, gameWindow.backGnd);
        gameWindow.evenementTimer(true);
				status2Text =  "" + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + " : " + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbArmeesDisponibles + " armées à placer";
				if (statusBar2.getText() != status2Text)
					statusBar2.setText(status2Text);
			}
		}
	}

	void finAttaque()
	{
		String mes;

		int i = leMonde.pays[deuxiemePays].appartientA;

		if (leMonde.pays[deuxiemePays].nbArmees <= 0)
		{
			(((Joueur)(joueurs.toArray())[joueurCourant])).nbPays++;
			((Joueur)((joueurs.toArray())[i])).nbPays--;
			if (((Joueur)((joueurs.toArray())[i])).nbPays <= 0)
			{
				nbJoueurs--;
				joueurs.removeEntry(leMonde.pays[deuxiemePays].appartientA);
				if (nbJoueurs == 1)
				{
					mes = "" + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + ", vous avez gagné !\nVoulez-vous rejouer ?";
					if (JOptionPane.showInternalConfirmDialog(frame, mes, "Risk - Partie terminée !", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
						cmNewGame();
					else System.exit(0);
				}
			}
			else
			{
				status2Text =  "" + leMonde.pays[premierPays].nom + " a conquis " + leMonde.pays[deuxiemePays].nom + "";
				statusBar2.setText(status2Text);
				leMonde.pays[deuxiemePays].appartientA = joueurCourant;
				leMonde.pays[deuxiemePays].nbArmees = 1;
				leMonde.pays[premierPays].nbArmees--;
        leMonde.pays[deuxiemePays].affecteDrapeau(gameWindow.gfxPool, gameWindow.backGnd);;
				afficheBoutonsEnvahit();
				etat = Etat.ENVAHIT;
			}
		}
		test = true;
    leMonde.pays[premierPays].ajouteArmees(gameWindow.gfxPool, gameWindow.backGnd);
    leMonde.pays[deuxiemePays].ajouteArmees(gameWindow.gfxPool, gameWindow.backGnd);
		test = false;
    gameWindow.evenementTimer(true);
		if (etat != Etat.ENVAHIT)
		{
			afficheBoutonsJeuNormal();
			status2Text =  "" + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + " : ";
			statusBar2.setText(status2Text);
			etat = Etat.ATTEND;
		}
	}

	void resoudAttaque()
	{
		int A1, A2, A3, AE, D1, D2, DE;
		int i = leMonde.pays[deuxiemePays].appartientA;

		NDT = NAT = 0;
		A1 = randGen.nextInt(6)+1;
		if (((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbAttaque > 1) A2 = randGen.nextInt(6)+1;
		else A2 = -1;
		if (((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbAttaque > 2) A3 = randGen.nextInt(6)+1;
		else A3 = -1;

		//on place A2 par rapport a A3
		if ( A2<A3 ) {AE=A2;A2=A3;A3=AE;}
		//On va placer A1 par rapport a A2 et A3
		//On commence par regarder s'il faut swapper A1 et A2
		if ( A1<A2 ) {AE=A1;A1=A2;A2=AE;}
		//On verifie si le nouveau A2 est encore bien place par rapport a A3
		if ( A2<A3 ) {AE=A2;A2=A3;A3=AE;}

		D1 = randGen.nextInt(6)+1;
		if (((Joueur)((joueurs.toArray())[i])).nbDefense > 1)
			D2 = randGen.nextInt(6)+1;
		else D2 = -1;
		if (D2>D1) {DE=D1;D1=D2;D2=DE;}

		if (A1>D1)
		{
			leMonde.pays[deuxiemePays].nbArmees--;
			NDT++;
		}
		else
		{
			leMonde.pays[premierPays].nbArmees--;
			NAT++;
		}
		if ((A2>0)&&(D2>0))
		{
			if (A2>D2)
			{
				leMonde.pays[deuxiemePays].nbArmees--;
				NDT++;
			}
			else
			{
				leMonde.pays[premierPays].nbArmees--;
				NAT++;
			}
		}
	}

	void cmExitGame()
	{
		System.exit(0);
	}

	void cmNewGame()
	{
		setupJoueurs();
		etat = Etat.INIT;
	}

	void cmInfo()
	{
    myThreadStop();                    //Arrêt du Timer
		JOptionPane.showMessageDialog(frame, (Object)"afficher la boite de dialogue Info", "Risk", JOptionPane.QUESTION_MESSAGE);
    myThreadStart();                    //Et redémarrer le Timer
	}

	void cmRecycl()
	{
		enleveBoutonsRecycle();
		afficheBoutonJoueurSuivant();
		etat = Etat.INTERMEDE;
	}

	void cmRecyclFini()
	{
		enleveBoutonsRecycle();
		etat = Etat.ATTEND;
		afficheBoutonsJeuNormal();
	}

	void cmJoueurSuivant()
	{
		int i;

		if ((etat == Etat.INTERMEDE)||(etat == Etat.NOUVARMEES))
		{
			if (((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbArmeesDisponibles > 0)
				JOptionPane.showMessageDialog(frame, (Object)"Vous devez distribuer\ntoutes vos armées", "Risk",  JOptionPane.INFORMATION_MESSAGE);
			else
			{
				if (joueurCourant == nbJoueurs-1)
				{
					enleveBoutonJoueurSuivant();
					afficheBoutonsRecycle();
					joueurCourant = 0;
					status2Text = "Echanger à nouveau des armées ou continuer ?";
					statusBar2.setText(status2Text);
				}
				else
				{
					joueurCourant++;
					status2Text =  "" + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + " : " + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbArmeesDisponibles + " armées à placer";
					if (statusBar2.getText() !=  status2Text)
							statusBar2.setText(status2Text);
				}
			}
		}
		else
		{
			if (joueurCourant == nbJoueurs-1)
			{
				enleveBoutonsJeuNormal();
				distribueArmees();
				afficheBoutonJoueurSuivant();
				joueurCourant = 0;
				status2Text =  "" + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + " : " + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbArmeesDisponibles + " armées à placer";
				statusBar2.setText(status2Text);
				nbArmeesDeplacees = 0;
				premierPays = deuxiemePays = 45;
				NDT = NAT = 0;
        gameWindow.movingCanons.flush(/*TShouldDelete::Delete*/);   // Canons en déplacement
        gameWindow.movingChevaux.flush(/*TShouldDelete::Delete*/);    // Chevaux en déplacement
        gameWindow.movingSoldats.flush(/*TShouldDelete::Delete*/);    // Soldats en déplacement
        gameWindow.movingCombattants.flush(/*TShouldDelete::Delete*/);  // Combattants en déplacement
        gameWindow.animCombattants.flush(/*TShouldDelete::Delete*/);    // Combattants combattant
        gameWindow.animExplosants.flush(/*TShouldDelete::Delete*/);   // Combattants explosant
				etat = Etat.ATTEND;
				for (i = 0; i <= 44; i++) leMonde.pays[i].nbArmeesAjoutees = 0;
				etat = Etat.NOUVARMEES;
			}
			else
			{
				joueurCourant++;
				status2Text =  "" + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + " : ";
				statusBar2.setText(status2Text);
				nbArmeesDeplacees = 0;
				premierPays = deuxiemePays = 45;
				NDT = NAT = 0;
        gameWindow.movingCanons.flush(/*TShouldDelete::Delete*/);   // Canons en déplacement
        gameWindow.movingChevaux.flush(/*TShouldDelete::Delete*/);    // Chevaux en déplacement
        gameWindow.movingSoldats.flush(/*TShouldDelete::Delete*/);    // Soldats en déplacement
        gameWindow.movingCombattants.flush(/*TShouldDelete::Delete*/);  // Combattants en déplacement
        gameWindow.animCombattants.flush(/*TShouldDelete::Delete*/);    // Combattants combattant
        gameWindow.animExplosants.flush(/*TShouldDelete::Delete*/);   // Combattants explosant
				etat = Etat.ATTEND;
			}
		}
	}

	void cmAttaque1()
	{
		if (etat == Etat.ATTEND)
		{
			etat = Etat.ATTAQUE;
			enleveBoutonsJeuNormal();
			afficheBoutonAnnuler();
			((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbAttaque = 1;
			status2Text = "Attaque avec 1 armée : Désignez les belligérants";
			statusBar2.setText(status2Text);
		}
	}

	void cmAttaque2()
	{
		if (etat == Etat.ATTEND)
		{
			etat = Etat.ATTAQUE;
			enleveBoutonsJeuNormal();
			afficheBoutonAnnuler();
			((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbAttaque = 2;
			status2Text = "Attaque avec 2 armées : Désignez les belligérants";
			statusBar2.setText(status2Text);
		}
	}

	void cmAttaque3()
	{
		if (etat == Etat.ATTEND)
		{
			etat = Etat.ATTAQUE;
			enleveBoutonsJeuNormal();
			afficheBoutonAnnuler();
			((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbAttaque = 3;
			status2Text = "Attaque avec 3 armées : Désignez les belligérants";
			statusBar2.setText(status2Text);
		}
	}

	void cmDefense1()
	{
		enleveBoutonsDefense();
		((Joueur)(joueurs.toArray())[leMonde.pays[deuxiemePays].appartientA]).nbDefense = 1;
		etat = Etat.COMBAT_AMENE;
    gameWindow.initBougeCombat(premierPays, deuxiemePays);
	}

	void cmDefense2()
	{
		enleveBoutonsDefense();
		((Joueur)(joueurs.toArray())[leMonde.pays[deuxiemePays].appartientA]).nbDefense = 2;
		etat = Etat.COMBAT_AMENE;
    gameWindow.initBougeCombat(premierPays, deuxiemePays);
	}

	void cmEnvahit1()
	{
		if (leMonde.pays[premierPays].nbArmees > 1)
		{
      gameWindow.initBougeArmees(1, premierPays, deuxiemePays);
			if (etat == Etat.DEPLACE2) nbArmeesDeplacees++;
		}
	}

	void cmEnvahit5()
	{
		if (leMonde.pays[premierPays].nbArmees > 5)
		{
      gameWindow.initBougeArmees(5, premierPays, deuxiemePays);
			if (etat == Etat.DEPLACE2) nbArmeesDeplacees+=5;
		}
	}

	void cmEnvahit10()
	{
		if (leMonde.pays[premierPays].nbArmees > 10)
		{
      gameWindow.initBougeArmees(10, premierPays, deuxiemePays);
			if (etat == Etat.DEPLACE2) nbArmeesDeplacees+=10;
		}
	}

	void cmEnvFini()
	{
		enleveBoutonsEnvahit();
		if (etat == Etat.ENVAHIT)
		{
			afficheBoutonsJeuNormal();
			status2Text =  "" + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + " :";
			statusBar2.setText(status2Text);
			etat = Etat.ATTEND;
		}
		else if (etat == Etat.DEPLACE2)
		{
			enleveBoutonAnnuler();
			afficheBoutonsJeuNormal();
			cmJoueurSuivant();
		}
	}

	void cmRetraite1()
	{
		if (leMonde.pays[deuxiemePays].nbArmees > 1)
		{
      gameWindow.initBougeArmees(1, deuxiemePays, premierPays);
			if (etat == Etat.DEPLACE2) nbArmeesDeplacees--;
		}
	}

	void cmRetraite5()
	{
		if (leMonde.pays[deuxiemePays].nbArmees > 5)
		{
      gameWindow.initBougeArmees(5, deuxiemePays, premierPays);
			if (etat == Etat.DEPLACE2) nbArmeesDeplacees-= 5;
		}
	}

	void cmRetraite10()
	{
		if (leMonde.pays[deuxiemePays].nbArmees > 10)
		{
      gameWindow.initBougeArmees(10, deuxiemePays, premierPays);
			if (etat == Etat.DEPLACE2) nbArmeesDeplacees-= 10;
		}
	}

	void cmDeplace()
	{
		etat = Etat.DEPLACE;
		enleveBoutonsJeuNormal();
		afficheBoutonAnnuler();
	}

	void cmAnnuler()
	{
		if ((etat == Etat.DEPLACE) || (etat == Etat.ATTAQUE))
		{
			etat = Etat.ATTEND;
			enleveBoutonAnnuler();
			afficheBoutonsJeuNormal();
			status2Text =  "" + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + " :";
			statusBar2.setText(status2Text);
		}
		else if (etat == Etat.DEPLACE2)
		{
			enleveBoutonsEnvahit();
			etat = Etat.DEPLACE;
			if (nbArmeesDeplacees < 0)
			{
				leMonde.pays[premierPays].nbArmees-= nbArmeesDeplacees;
				leMonde.pays[deuxiemePays].nbArmees+= nbArmeesDeplacees;
        leMonde.pays[premierPays].ajouteArmees(gameWindow.gfxPool, gameWindow.backGnd);
        leMonde.pays[deuxiemePays].ajouteArmees(gameWindow.gfxPool, gameWindow.backGnd);
				nbArmeesDeplacees = 0;
			}
			if (nbArmeesDeplacees > 0)
			{
				leMonde.pays[premierPays].nbArmees+= nbArmeesDeplacees;
				leMonde.pays[deuxiemePays].nbArmees-= nbArmeesDeplacees;
        leMonde.pays[premierPays].ajouteArmees(gameWindow.gfxPool, gameWindow.backGnd);
        leMonde.pays[deuxiemePays].ajouteArmees(gameWindow.gfxPool, gameWindow.backGnd);
				nbArmeesDeplacees = 0;
			}
		}
	}

	void afficheBoutonJoueurSuivant()
	{
    joueurSuivantButton = actions.addTool("joueurSuivant", "Joueur suivant", this);
		actions.repaint();
	}

	void enleveBoutonJoueurSuivant()
	{
    actions.toolbar.remove(actions.gadgetWithId("joueurSuivant"));
		actions.repaint();
	}

	void afficheBoutonsRecycle()
	{
    distributionArmeesButton = actions.addTool("distributionArmees", "Commencer à échanger des armées", this);
    distributionArmeesFinieButton = actions.addTool("distributionArmeesFinie", "Continuer sans échanges", this);
    actions.repaint();
	}

	void enleveBoutonsRecycle()
	{
    actions.toolbar.remove(actions.gadgetWithId("distributionArmees"));
		actions.repaint();
    actions.toolbar.remove(actions.gadgetWithId("distributionArmeesFinie"));
		actions.repaint();
	}

	void afficheBoutonsJeuNormal()
	{
    joueurSuivantButton = actions.addTool("joueurSuivant", "Joueur suivant", this);
    attackOneButton = actions.addTool("attackOne", "Attaquer avec 1 armée", this);
    attackTwoButton = actions.addTool("attackTwo", "Attaquer avec 2 armées", this);
    attackThreeButton = actions.addTool("attackThree", "Attaquer avec 3 armées", this);
    moveArmiesButton = actions.addTool("moveArmies", "Déplacer des armées", this);
		actions.repaint();
	}

	void enleveBoutonsJeuNormal()
	{
    actions.toolbar.remove(actions.gadgetWithId("joueurSuivant"));
		actions.repaint();
    actions.toolbar.remove(actions.gadgetWithId("attackOne"));
		actions.repaint();
    actions.toolbar.remove(actions.gadgetWithId("attackTwo"));
    actions.repaint();
    actions.toolbar.remove(actions.gadgetWithId("attackThree"));
		actions.repaint();
    actions.toolbar.remove(actions.gadgetWithId("moveArmies"));
		actions.repaint();
	}

	void afficheBoutonsDefense()
	{
      defendOneButton = actions.addTool("defendOne", "Défendre avec 1 armée", this);
      defendTwoButton = actions.addTool("defendTwo", "Défendre avec 2 armées", this);
			actions.repaint();
	}

	void enleveBoutonsDefense()
	{
    actions.toolbar.remove(actions.gadgetWithId("defendOne"));
		actions.repaint();
    actions.toolbar.remove(actions.gadgetWithId("defendTwo"));
		actions.repaint();
	}

	void afficheBoutonsEnvahit()
	{
    moveOneButton = actions.addTool("moveOne", "Envahir avec 1 armée", this);
    moveFiveButton = actions.addTool("moveFive", "Envahir avec 5 armée", this);
    moveTenButton = actions.addTool("moveTen", "Envahir avec 10 armée", this);
    moveFinishButton = actions.addTool("moveFinish", "Terminé d'envahir", this);
    moveBackOneButton = actions.addTool("moveBackOne", "Retirer 1 armée", this);
    moveBackFiveButton = actions.addTool("moveBackFive", "Retirer 5 armées", this);
    moveBackTenButton = actions.addTool("moveBackTen", "Retirer 10 armées", this);
		actions.repaint();
	}

	void enleveBoutonsEnvahit()
	{
    actions.toolbar.remove(actions.gadgetWithId("moveOne"));
		actions.repaint();
    actions.toolbar.remove(actions.gadgetWithId("moveFive"));
		actions.repaint();
    actions.toolbar.remove(actions.gadgetWithId("moveTen"));
		actions.repaint();
    actions.toolbar.remove(actions.gadgetWithId("moveFinish"));
		actions.repaint();
    actions.toolbar.remove(actions.gadgetWithId("moveBackOne"));
		actions.repaint();
    actions.toolbar.remove(actions.gadgetWithId("moveBackFive"));
		actions.repaint();
    actions.toolbar.remove(actions.gadgetWithId("moveBackTen"));
		actions.repaint();
	}

	void afficheBoutonAnnuler()
	{
    annulerButton = actions.addTool("annuler", "Annuler", this);
		actions.repaint();
	}

	void enleveBoutonAnnuler()
	{
    actions.toolbar.remove(actions.gadgetWithId("annuler"));
		actions.repaint();
	}

	void setupJoueurs()
	{
		String mes;
		boolean randNums[]; //45
		boolean trouve = false;
		int i;
		int joueur = 0, h, nbArmeesDisponibles = 0;
		DialogNbJoueurs dialogNbJoueurs;
		DialogSetupJoueur dialogSetupJoueur;

		randNums = new boolean[45];
    myThreadStop();
		if (joueurs != null)
		{
			leMonde.reset();
		}
		nbJoueurs = 0;
		// Nombre de joueurs
		while ((nbJoueurs < 2) || (nbJoueurs > 6))
			dialogNbJoueurs = new DialogNbJoueurs((Frame)this, nbJoueurs);

    gameWindow.evenementTimer(true);
		joueurs = new SpritesSet();
		switch (nbJoueurs)
		{
			case 2: nbArmeesDisponibles = 40; break;
			case 3: nbArmeesDisponibles = 35; break;
			case 4: nbArmeesDisponibles = 30; break;
			case 5: nbArmeesDisponibles = 25; break;
			case 6: nbArmeesDisponibles = 20; break;
		}
		// Noms des joueurs
		mes = "";
		for (i = 0; i < nbJoueurs; i++)
		{

			while (mes == "")
			{
				dialogSetupJoueur = new DialogSetupJoueur((Frame)this, i);
				mes = dialogSetupJoueur.name;
			}

      gameWindow.evenementTimer(true);
			if (i == 0) joueurs.add(new Joueur(mes, nbArmeesDisponibles));
			else
			{
				trouve = false;
				for (h = 0; h < i; h++)
					if (((Joueur)(joueurs.toArray())[h]).nom == mes) trouve = true;
				if (!trouve) joueurs.add(new Joueur(mes, nbArmeesDisponibles));
				else i--;
			}
			mes = "";
		}
    myThreadStart();
		for (i = 0; i < 45; i++) randNums[i] = false;
		for (i = 44; i >= 0; i--)
		{
			if (i==0) h = 0; else h = new Random().nextInt(i);
			if (!randNums[h])
			{
				leMonde.pays[h].appartientA = joueur;
					randNums[h] = true; //??
			}
			else
			{
				while (h < 45)
				{
					if (!randNums[h])
					{
						leMonde.pays[h].appartientA = joueur;
						randNums[h] = true;//??
						h = 46;
					}
					h++;
				}
				if (h == 45)
				{
					while (h >= 0)
					{
						if (!randNums[h])
						{
							leMonde.pays[h].appartientA = joueur;
							randNums[h] = true;
							h = -1;
						}
						h--;
					}
				}
			}
			((Joueur)(joueurs.toArray())[joueur]).nbPays++;
			joueur = (joueur + 1) % nbJoueurs;
		}
		for (i = 44; i >= 0; i--)
		{
      leMonde.pays[i].ajouteArmees(gameWindow.gfxPool, gameWindow.backGnd);
      leMonde.pays[i].affecteDrapeau(gameWindow.gfxPool, gameWindow.backGnd);
      gameWindow.evenementTimer(true);
		}
		for (i = 0; i < nbJoueurs; i++)
			((Joueur)(Joueur)(joueurs.toArray())[i]).nbArmeesDisponibles -= ((Joueur)(Joueur)(joueurs.toArray())[i]).nbPays;
		joueurCourant = 0;
		status2Text =  "" + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nom + " : " + ((Joueur)((Joueur)(joueurs.toArray())[joueurCourant])).nbArmeesDisponibles + " armées à placer";
		if (statusBar2.getText() !=  status2Text)
			statusBar2.setText(status2Text);
	}

  public void actionPerformed(ActionEvent e)
  {
    Object obj = e.getSource();
    if (obj instanceof RiskButton)
		{
      RiskButton b = (RiskButton) obj;
      b.setSelected(!b.isSelected());
      if (b.getIcon() == null)
			{
        b.setBackground(b.isSelected() ? Color.green : Color.lightGray);
			}
		}
    if (obj.equals(distributionArmeesButton))
		{
      cmRecycl();
    }
    if (obj.equals(distributionArmeesFinieButton))
		{
      cmRecyclFini();
    }
    if (obj.equals(joueurSuivantButton))
		{
      cmJoueurSuivant();
    }
    if (obj.equals(attackOneButton))
		{
      cmAttaque1();
    }
    if (obj.equals(attackTwoButton))
		{
      cmAttaque2();
    }
    if (obj.equals(attackThreeButton))
		{
      cmAttaque3();
    }
    if (obj.equals(moveArmiesButton))
		{
      cmDeplace();
    }
    if (obj.equals(annulerButton))
		{
      cmAnnuler();
    }
    if (obj.equals(defendOneButton))
		{
      cmDefense1();
    }
    if (obj.equals(defendTwoButton))
		{
      cmDefense2();
    }
    if (obj.equals(moveOneButton))
		{
      cmEnvahit1();
    }
    if (obj.equals(moveFiveButton))
		{
      cmEnvahit5();
    }
    if (obj.equals(moveTenButton))
		{
      cmEnvahit10();
    }
    if (obj.equals(moveBackOneButton))
		{
      cmRetraite1();
    }
    if (obj.equals(moveBackFiveButton))
		{
      cmRetraite5();
    }
    if (obj.equals(moveBackTenButton))
		{
      cmRetraite10();
    }
    if (obj.equals(moveFinishButton))
		{
      cmEnvFini();
    }
}

	public void itemStateChanged(ItemEvent e) {}


	public static void main(String args[])
	{
		new Risk();

	}

	public void run()
	{
    Thread thisThread = Thread.currentThread();
    while (myThread == thisThread)
    {
			try {myThread.sleep(100);}
			catch (InterruptedException e){}
			evTimer();
		}
	}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e)
	{
		String nomPays;
		Point mousePosition;
		int mouseLocalisation, numeroJoueur;
		Joueur joueur;

		mousePosition = e.getPoint();
    mouseLocalisation = gameWindow.cliqueDans(mousePosition);
		nomPays = leMonde.pays[mouseLocalisation].nom;
		numeroJoueur = leMonde.pays[mouseLocalisation].appartientA;
		if (nomPays != "") status1Text = nomPays;
		else status1Text = "";
		if (joueurs != null && numeroJoueur != -1)
		{
			if ( status1Text != "")
			{
				joueur = (Joueur)(Joueur)(joueurs.toArray())[numeroJoueur];
				status1Text = status1Text + ", appartient à " + joueur.nom;
				if (statusBar1.getText() != status1Text)
						statusBar1.setText(status1Text);
			}
		}
		if (statusBar1.getText() != status1Text)
				statusBar1.setText(status1Text);
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
    e.translatePoint(-8, -57);
    evenementLButtonDown(e.getPoint());
	}

	public void mouseReleased(MouseEvent e)
	{
    e.translatePoint(-8, -57);
    evenementLButtonUp(e.getPoint());
	}

  void myThreadStop()
  {
    myThread = null;
  }

  void myThreadStart()
  {
    myThread = new Thread(this);
    myThread.start();
  }
}