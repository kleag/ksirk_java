import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.applet.*;
import java.util.*;

class GameWindow extends JComponent
		implements MouseMotionListener
{
	public ONU leMonde;				// Les pays
	boolean keyLeft, keyRight, keyUp, keyDown, keyJump;// Touches enfoncées ?
	GfxPool gfxPool;    	      	// Gfx-Pool utilisé
	public BackGnd backGnd;			 	// Arrière-plan utilisé
	SpritesSet movingCanons;		// Canons en déplacement
	SpritesSet movingChevaux;		// Chevaux en déplacement
	SpritesSet movingSoldats;		// Soldats en déplacement
	SpritesSet movingCombattants;	// combattants en déplacement
	SpritesSet animCombattants;		// combattants combattant
	SpritesSet animExplosants;		// combattants explosant
	SpritesSet combattants;			// combattants statiques
	int compteur;
	boolean LMouseButtonDown;
	boolean LMouseButtonUp;
	boolean RMouseButtonDown;
	Point point;
	Point mousePosition = new Point();

  AudioClip SoundRoule;
  AudioClip SoundCrash;
  AudioClip SoundCanon;
  int levelMaxX;
	int levelMaxY;
	char frontieresTab[/*45*45*/];

	GameWindow()
	{		//Initialise la fenêtre

		leMonde = new ONU();
		gfxPool = new GfxPool();			//Créer Gfx-Pool
		backGnd = new BackGnd(gfxPool, 640, 480, leMonde.levelMaxX, leMonde.levelMaxY);      //Créer arrière-plan

		//Construction graphique
		movingCanons = new SpritesSet(1);
		movingChevaux = new SpritesSet(1);
		movingSoldats = new SpritesSet(1);
		movingCombattants = new SpritesSet(1);
		animCombattants = new SpritesSet(1);
		animExplosants = new SpritesSet(1);
		combattants = new SpritesSet(1);

		setLayout(new BorderLayout());
		add(backGnd, BorderLayout.CENTER);
//		newGame();

	}

	int cliqueDans(Point point)
	{
		Point coord = new Point(point.x + backGnd.x, point.y + backGnd.y);
		if (leMonde.pays[0].isIn(coord)) return(0);
		if (leMonde.pays[1].isIn(coord)) return(1);
		if (leMonde.pays[2].isIn(coord)) return(2);
		if (leMonde.pays[3].isIn(coord)) return(3);
		if (leMonde.pays[4].isIn(coord)) return(4);
		if (leMonde.pays[5].isIn(coord)) return(5);
		if (leMonde.pays[6].isIn(coord)) return(7);
		if (leMonde.pays[7].isIn(coord)) return(7);
		if (leMonde.pays[8].isIn(coord)) return(8);
		if (leMonde.pays[9].isIn(coord)) return(9);
		if (leMonde.pays[10].isIn(coord)) return(10);
		if (leMonde.pays[11].isIn(coord)) return(11);
		if (leMonde.pays[12].isIn(coord)) return(12);
		if (leMonde.pays[13].isIn(coord)) return(13);
		if (leMonde.pays[14].isIn(coord)) return(14);
		if (leMonde.pays[15].isIn(coord)) return(15);
		if (leMonde.pays[16].isIn(coord)) return(16);
		if (leMonde.pays[17].isIn(coord)) return(17);
		if (leMonde.pays[18].isIn(coord)) return(18);
		if (leMonde.pays[19].isIn(coord)) return(19);
		if (leMonde.pays[20].isIn(coord)) return(20);
		if (leMonde.pays[21].isIn(coord)) return(21);
		if (leMonde.pays[22].isIn(coord)) return(22);
		if (leMonde.pays[23].isIn(coord)) return(23);
		if (leMonde.pays[24].isIn(coord)) return(24);
		if (leMonde.pays[25].isIn(coord)) return(25);
		if (leMonde.pays[26].isIn(coord)) return(26);
		if (leMonde.pays[27].isIn(coord)) return(27);
		if (leMonde.pays[28].isIn(coord)) return(28);
		if (leMonde.pays[29].isIn(coord)) return(29);
		if (leMonde.pays[30].isIn(coord)) return(31);
		if (leMonde.pays[31].isIn(coord)) return(31);
		if (leMonde.pays[32].isIn(coord)) return(32);
		if (leMonde.pays[33].isIn(coord)) return(33);
		if (leMonde.pays[34].isIn(coord)) return(34);
		if (leMonde.pays[35].isIn(coord)) return(35);
		if (leMonde.pays[36].isIn(coord)) return(36);
		if (leMonde.pays[37].isIn(coord)) return(37);
		if (leMonde.pays[38].isIn(coord)) return(38);
		if (leMonde.pays[39].isIn(coord)) return(40);
		if (leMonde.pays[40].isIn(coord)) return(40);
		if (leMonde.pays[41].isIn(coord)) return(41);
		if (leMonde.pays[42].isIn(coord)) return(42);
		if (leMonde.pays[43].isIn(coord)) return(43);
		if (leMonde.pays[44].isIn(coord)) return(44);
		return(45);
	}

	void newGame()
	{		//Démarre un niveau

		keyJump=false; 				//Pas de touche enfonçée
		keyLeft=false; keyRight=false; keyUp=false; keyDown=false;
		//Destruction des images existantes
		if (movingCanons != null) movingCanons.flush();
		if (movingChevaux != null) movingChevaux.flush();
		if (movingSoldats != null) movingSoldats.flush();
		if (movingCombattants != null) movingCombattants.flush();
		if (animCombattants != null) animCombattants.flush();
		if (animExplosants != null) animExplosants.flush();
		if (combattants != null) combattants.flush();

		leMonde = new ONU();
	}

	public void paint(Graphics g)
	{		//Reconstruit l'ensemble de la fenêtre, réagit à des messages WM_PAINT
		int i;
		Object[] args = {g};
		backGnd.paint(g);								//Dessiner arrière-plan

		if (leMonde != null)
		{
		for (i = 0; i <= 44; i++)
			{
				if ((i != 6)&&(i!=30)&&(i!=39))
				{
					if (!leMonde.pays[i].spritesCanons.isEmpty())
					{
						leMonde.pays[i].spritesCanons.forEach("kill");//Supprimer les sprites
						leMonde.pays[i].spritesCanons.forEach("paint", args);//Dessiner les sprites
					}
					if (!leMonde.pays[i].spritesChevaux.isEmpty())
					{
						leMonde.pays[i].spritesChevaux.forEach("kill");//Supprimer les sprites
						leMonde.pays[i].spritesChevaux.forEach("paint", args);//Dessiner les sprites
					}
					if (!leMonde.pays[i].spritesSoldats.isEmpty())
					{
						leMonde.pays[i].spritesSoldats.forEach("kill");//Supprimer les sprites
						leMonde.pays[i].spritesSoldats.forEach("paint", args);//Dessiner les sprites
					}
					if (leMonde.pays[i].drapeau != null)
					{
						leMonde.pays[i].drapeau.kill();//Supprimer les sprites
						leMonde.pays[i].drapeau.nextFrame();    //Image suivante
						leMonde.pays[i].drapeau.paint(g);//Dessiner les sprites
					}
				}
			}
		}
		if (movingCanons != null) if (!movingCanons.isEmpty())
		{
      movingCanons.forEach("kill"); //Supprimer les sprites
      movingCanons.forEach("paint", args);  //Dessiner les sprites
		}
		if (movingChevaux != null) if (!movingChevaux.isEmpty())
		{
      movingChevaux.forEach("kill");  //Supprimer les sprites
      movingChevaux.forEach("paint", args); //Dessiner les sprites
		}
		if (movingSoldats != null) if (!movingSoldats.isEmpty())
		{
      movingSoldats.forEach("kill");  //Supprimer les sprites
      movingSoldats.forEach("paint", args); //Dessiner les sprites
		}
		if (movingCombattants != null) if (!movingCombattants.isEmpty())
		{
      movingCombattants.forEach("kill");  //Supprimer les sprites
      movingCombattants.forEach("paint", args); //Dessiner les sprites
		}
		if (animCombattants != null) if (!animCombattants.isEmpty())
		{
      animCombattants.forEach("kill");  //Supprimer les sprites
      animCombattants.forEach("paint", args); //Dessiner les sprites
			if (animCombattants.firstThat("isLastFrame") != null) stopCombat();
		}
		if (animExplosants != null) if (!animExplosants.isEmpty())
		{
      animExplosants.forEach("kill"); //Supprimer les sprites
      animExplosants.forEach("paint", args);  //Dessiner les sprites
			if (animExplosants.firstThat("isLastFrame") != null) stopExplosion();
		}
		if (combattants != null) if (!combattants.isEmpty())
		{
      combattants.forEach("kill");  //Supprimer les sprites
      combattants.forEach("paint", args); //Dessiner les sprites
    }
  }

	void evKeyDown(int key, int a, int b)
	{		//Handle de clavier partie 1, réagit à des messages WM_keyDown
/*		switch (key)
		{
			case VK_SPACE :
			break;//...ou augmenter la puissance de saut
			case VK_RIGHT :
				BackGnd.Scroll(left, 1024);
				::InvalidateRect(HWindow, NULL, false);				//Et reconstruction
				keyRight = true;
			break;   	//et les touches de direction
			case VK_LEFT  :
				BackGnd.Scroll(right, 1024);
				::InvalidateRect(HWindow, NULL, false);				//Et reconstruction
				keyLeft = true;
			break;
			case VK_UP	  :
				BackGnd.Scroll(down, 1024);
				::InvalidateRect(HWindow, NULL, false);				//Et reconstruction
				keyUp = true;
			break;
			case VK_DOWN  :
				BackGnd.Scroll(up, 1024);
				repaint();/*::InvalidateRect(HWindow,NULL,false);				//Et reconstruction
				keyDown=true;
			break;
		}
*/
	}

	void evKeyUp(int key, int a, int b)
	{		//Handle de clavier partie 2, réagit à des messages WM_keyUp
/*		switch (key)
		{
			case VK_SPACE :
			break;					//Annuler le saut
			case VK_RIGHT :
				keyRight=false;
			break;        //et les touches de direction
			case VK_LEFT  :
				keyLeft=false;
			break;
			case VK_UP	  :
				keyUp=false;
			break;
			case VK_DOWN  :
				keyDown=false;
			break;
		}
*/
	}

	void evLButtonDown(int a, Point point)
	{
		this.point = point;
		LMouseButtonDown = true;
	}

	void evRButtonDown(int a, Point point)
	{
			this.point = point;
			RMouseButtonDown = true;
	}

	void evLButtonUp(int a, Point point)
	{
		this.point = point;
		LMouseButtonUp = true;
	}

	void evenementTimer(boolean mes)
	{		//Appel du Timer toutes les 50 ms
		boolean flag = mes;

    if ((mousePosition.getX() >= 0) && (mousePosition.getX() < 10)
      && (mousePosition.getY() >= 0) && (mousePosition.getY() < backGnd.hauteur))
		{
			backGnd.scroll(new Dir(Dir.right), 10);
			flag = true;
		}
    if ((mousePosition.getX() >= backGnd.largeur - 10) &&
      (mousePosition.getX() < backGnd.largeur)
      && (mousePosition.getY() >= 0) && (mousePosition.getY() < backGnd.hauteur))
		{
			backGnd.scroll(new Dir(Dir.left), 10);
			flag = true;
		}
    if ((mousePosition.getY() >= 0) && (mousePosition.getY() < 10)
      && (mousePosition.getX() >= 0) && (mousePosition.getX() < backGnd.largeur))
		{
			backGnd.scroll(new Dir(Dir.down), 10);
			flag = true;
		}
    if ((mousePosition.getY() >= backGnd.hauteur -10) &&
      (mousePosition.getY() < backGnd.hauteur)
      && (mousePosition.getX() >= 0) && (mousePosition.getX() < backGnd.largeur))
		{
			backGnd.scroll(new Dir(Dir.up), 10);
			flag = true;
		}
		if (movingCanons != null) if (!movingCanons.isEmpty()) moveCanons();
		if (movingChevaux != null) if (!movingChevaux.isEmpty()) moveChevaux();
		if (movingSoldats != null) if (!movingSoldats.isEmpty()) moveSoldats();
		if (movingCombattants != null) if (!movingCombattants.isEmpty()) moveMovingCombattants();
		if (animCombattants != null) if (!animCombattants.isEmpty())
		{
			animCombattants.forEach("nextFrame");
			flag = true;
		}
		if (animExplosants != null) if (!animExplosants.isEmpty())
		{
			animExplosants.forEach("nextFrame");
			flag = true;
		}
		if (flag)
			repaint();
	};

	void initBougeArmees(int nbABouger, int premierPays, int deuxiemePays)
	{
		AnimSprite sprite;

		if ((leMonde.pays[premierPays].nbArmees > 10) && (nbABouger == 10)
				&& (nbABouger < leMonde.pays[premierPays].nbArmees))
		{
			sprite = new AnimSprite(1024, 0, gfxPool, 32, 32, backGnd, 2);
			sprite.destination = deuxiemePays;
      sprite.x = (int)leMonde.pays[premierPays].pointCanon.getX();
      sprite.y = (int)leMonde.pays[premierPays].pointCanon.getY();
			if (movingCanons != null) movingCanons.add(sprite);
			leMonde.pays[premierPays].nbArmees -= 10;
		}
		if ((leMonde.pays[premierPays].nbArmees > 5) && (nbABouger == 5)
				&& (nbABouger < leMonde.pays[premierPays].nbArmees))
		{
			sprite = new AnimSprite(1024, 96, gfxPool, 32, 32, backGnd, 1);
			sprite.destination = deuxiemePays;
      sprite.x = (int)leMonde.pays[premierPays].pointCheval.getX();
      sprite.y = (int)leMonde.pays[premierPays].pointCheval.getY();
			if (movingChevaux != null) movingChevaux.add(sprite);
			leMonde.pays[premierPays].nbArmees -= 5;
		}
		if ((leMonde.pays[premierPays].nbArmees > 1) && (nbABouger == 1)
				&& (nbABouger < leMonde.pays[premierPays].nbArmees))
		{
			sprite = new AnimSprite(1024, 192, gfxPool, 32, 32, backGnd, 1);
			sprite.destination = deuxiemePays;
      sprite.x = (int)leMonde.pays[premierPays].pointSoldat.getX();
      sprite.y = (int)leMonde.pays[premierPays].pointSoldat.getY();
			if (movingSoldats != null) movingSoldats.add(sprite);
			leMonde.pays[premierPays].nbArmees--;
		}
		leMonde.pays[premierPays].ajouteArmees(this.gfxPool, this.backGnd);
	}

	void moveCanons()
	{
		Point destinationPoint;
		AnimSprite canon;
    AnimSprite mc;
		Iterator iter = movingCanons.iterator();

		while(iter.hasNext())
		{
			canon = (AnimSprite)iter.next();
			if (canon != null)
			{
				destinationPoint = leMonde.pays[canon.destination].pointCanon;
				canon.moveIt(destinationPoint);
				if ((canon.x == destinationPoint.x)&&(canon.y == destinationPoint.y))
				{
					leMonde.pays[canon.destination].nbArmees += 10;
					movingCanons.remove(canon);
					leMonde.pays[canon.destination].spritesCanons.flush();
					leMonde.pays[canon.destination].spritesChevaux.flush();
					leMonde.pays[canon.destination].spritesSoldats.flush();
					leMonde.pays[canon.destination].ajouteArmees(this.gfxPool, this.backGnd);
				}
			}
			repaint(); 	//Et reconstruction
		}
	}

	void moveChevaux()
	{
		Point destinationPoint;
		AnimSprite cheval;
		Iterator iter = movingChevaux.iterator();

		while ( iter.hasNext() )
		{
			cheval = (AnimSprite)iter.next();
			if (cheval != null)
			{
				destinationPoint = leMonde.pays[cheval.destination].pointCheval;
				cheval.moveIt(destinationPoint);
				if ((cheval.x == destinationPoint.x) && (cheval.y == destinationPoint.y))
				{
					leMonde.pays[cheval.destination].nbArmees += 5;
					movingChevaux.remove(cheval);
					leMonde.pays[cheval.destination].spritesCanons.flush();
					leMonde.pays[cheval.destination].spritesChevaux.flush();
					leMonde.pays[cheval.destination].spritesSoldats.flush();
					leMonde.pays[cheval.destination].ajouteArmees(this.gfxPool, this.backGnd);
				}
			}
			repaint();				//Et reconstruction
		}
	}

	void moveSoldats()
	{
		Point destinationPoint;
		AnimSprite soldat;
		Iterator iter = movingSoldats.iterator();

		while(iter.hasNext())
		{
			soldat = (AnimSprite)iter.next();
			if (soldat != null)
			{
				destinationPoint = leMonde.pays[soldat.destination].pointSoldat;
				soldat.moveIt(destinationPoint);
				if ((soldat.x == destinationPoint.x)&&(soldat.y == destinationPoint.y))
				{
					leMonde.pays[soldat.destination].nbArmees++;
					movingSoldats.remove(soldat);
					leMonde.pays[soldat.destination].spritesCanons.flush();
					leMonde.pays[soldat.destination].spritesChevaux.flush();
					leMonde.pays[soldat.destination].spritesSoldats.flush();
					leMonde.pays[soldat.destination].ajouteArmees(this.gfxPool, this.backGnd);
				}
			}
      repaint(); //Et reconstruction
		}
	}

	void initBougeCombat(int paysAttaquant, int paysDefenseur)
	{
		AnimSprite sprite;
    if (movingCombattants != null) movingCombattants.flush();
		if (animCombattants != null) animCombattants.flush();
		if (animExplosants != null) animExplosants.flush();
		if (combattants != null) combattants.flush();
		sprite = new AnimSprite(1024, 0, gfxPool, 32, 32, backGnd, 2);
		sprite.destination = paysDefenseur;
    if (leMonde.pays[paysAttaquant].pointDrapeau.getX() <=
          leMonde.pays[paysDefenseur].pointDrapeau.getX())
		{
      sprite.destinationPoint = new Point(leMonde.pays[paysDefenseur].pointDrapeau);
      sprite.destinationPoint.translate(-64, -12);
		}
		else
    {
      sprite.destinationPoint = new Point(leMonde.pays[paysDefenseur].pointDrapeau);
      sprite.destinationPoint.translate(52,-12);
		}
    sprite.x = (int)leMonde.pays[paysAttaquant].pointCanon.getX();
    sprite.y = (int)leMonde.pays[paysAttaquant].pointCanon.getY();
		if (movingCombattants != null) movingCombattants.add(sprite);

		sprite = new AnimSprite(1024, 0, gfxPool, 32, 32, backGnd, 2);
		sprite.destination = paysDefenseur;
    if (leMonde.pays[paysAttaquant].pointDrapeau.getX() <=
          leMonde.pays[paysDefenseur].pointDrapeau.getX())
		{
      sprite.destinationPoint = new Point(leMonde.pays[paysDefenseur].pointDrapeau);
      sprite.destinationPoint.translate(52,-12);
		}
		else
		{
      sprite.destinationPoint = new Point(leMonde.pays[paysDefenseur].pointDrapeau);
      sprite.destinationPoint.translate(-64, -12);
		}
    sprite.x = (int)leMonde.pays[paysDefenseur].pointCanon.getX();
    sprite.y = (int)leMonde.pays[paysDefenseur].pointCanon.getY();
		if (movingCombattants != null) movingCombattants.add(sprite);
	}

	void moveMovingCombattants()
	{
		AnimSprite combattant;
    AnimSprite mc;
		Iterator iter = movingCombattants.iterator();
		SpritesSet toRemove = new SpritesSet(movingCombattants.size());

		while(iter.hasNext())
		{
      combattant = (AnimSprite)iter.next();
			if (combattant != null)
			{
				combattant.moveIt(combattant.destinationPoint);

        if ((combattant.getPoint()).equals(combattant.destinationPoint))
        {
          if (combattant.getX() < (int)leMonde.pays[combattant.destination].pointDrapeau.getX())
						combattant.look = new Dir(Dir.right);
          else combattant.look = new Dir(Dir.left);
          if (combattants != null) combattants.add(combattant);
					toRemove.add(combattant);
				}
        repaint();
      }
		}
		iter = toRemove.iterator();
		while ( iter.hasNext() ) movingCombattants.remove(iter.next());
		System.out.println("movingCombattants.size() = "+ movingCombattants.size());
	}

	void animCombat()
	{
    AnimSprite sprite, as;
    Iterator iter = combattants.iterator();

		if (animCombattants != null) if (!animCombattants.isEmpty()) animCombattants.flush();
    while (iter.hasNext())
		{
      as = (AnimSprite)iter.next();
      sprite = new AnimSprite(1024, 408,  gfxPool, 64, 32, backGnd, 4);
      sprite.look = as.look;
      if (sprite.look == new Dir(Dir.left)) sprite.x = as.x-32;
      else sprite.x = as.x;
      sprite.y = as.y;
      if (animCombattants != null) animCombattants.add(sprite);
		}
		if (combattants != null) if (!combattants.isEmpty()) combattants.flush();
    repaint();
  }

	void stopCombat()
	{
		AnimSprite sprite, ac;
		Iterator iter = animCombattants.iterator();

		if (combattants != null) if (!combattants.isEmpty()) combattants.flush();
		if (animCombattants!= null) while (iter.hasNext())
		{
			ac = (AnimSprite)iter.next();
			sprite = new AnimSprite(1024, 0, gfxPool, 32, 32, backGnd, 2);
			sprite.look = ac.look;
			if (sprite.look == new Dir(Dir.left)) sprite.x = ac.x;//+32;
			else sprite.x = ac.x;
			sprite.y = ac.y;
			if (combattants != null) combattants.add(sprite);
		}
		if (animCombattants != null) if (!animCombattants.isEmpty()) animCombattants.flush();
    repaint();
	}

	void animExplosion(int qui, int paysAttaquant, int paysDefenseur)
	{
		AnimSprite sprite, combattant;
		Iterator iter = combattants.iterator();

    if (animExplosants != null) if (!animExplosants.isEmpty()) animExplosants.flush();

		while (iter.hasNext())
		{
			combattant = (AnimSprite)iter.next();
			if ( qui == 0 ) // Seul l'Attaquant est tué
			{//on détermine si le sprite combattant est celui de l'attaquant ou pas
				// càd, s'il vient de gauche et regarde à droite
				// si oui, on crée un sprite explosant sinon on met un sprite normal
				if (((combattant.look.equal(new Dir(Dir.right)))
             && (leMonde.pays[paysAttaquant].pointDrapeau.getX() <=
                 leMonde.pays[paysDefenseur].pointDrapeau.getX()))
					||((combattant.look.equal(new Dir(Dir.left)))
             &&(leMonde.pays[paysAttaquant].pointDrapeau.getX() >
                 leMonde.pays[paysDefenseur].pointDrapeau.getX())))
				{
					sprite = new AnimSprite(1024, 504,	gfxPool, 32, 32, backGnd, 4);
					sprite.x = combattant.x;
					sprite.y = combattant.y;
					sprite.look = combattant.look;
					if (animExplosants != null) animExplosants.add(sprite);
				}
				else
				{
					sprite = new AnimSprite(1024, 0, gfxPool, 32, 32, backGnd, 2);
					sprite.x = combattant.x;
					sprite.y = combattant.y;
					sprite.look = combattant.look;
					if (animExplosants != null) animExplosants.add(sprite);
				}
			}
			else if ( qui == 1 ) // Seul le Défenseur est Tué
			{//on détermine si le sprite combattant est celui du défenseur ou pas
				// si oui, on crée un sprite explosant sinon on met un sprite normal
				if ( ((combattant.look.equal(new Dir(Dir.left)))
             && (leMonde.pays[paysAttaquant].pointDrapeau.getX() <=
                 leMonde.pays[paysDefenseur].pointDrapeau.getX()))
					|| ((combattant.look.equal(new Dir(Dir.right)))
             &&(leMonde.pays[paysAttaquant].pointDrapeau.getX() >
               leMonde.pays[paysDefenseur].pointDrapeau.getX())))
					{
					sprite = new AnimSprite(1024, 504, gfxPool, 32, 32, backGnd, 4);
					sprite.x = combattant.x;
					sprite.y = combattant.y;
					sprite.look = combattant.look;
					if (animExplosants != null) animExplosants.add(sprite);
				}
				else
				{
					sprite = new AnimSprite(1024, 0, gfxPool, 32, 32, backGnd, 2);
					sprite.x = combattant.x;
					sprite.y = combattant.y;
					sprite.look = combattant.look;
					if (animExplosants != null) animExplosants.add(sprite);
				}
			}
			else //qui == 2 Les deux sont tués, on crée un sprite explosant pour tous les deux
			{
				sprite = new AnimSprite(1024, 504, gfxPool, 32, 32, backGnd, 4);
				sprite.x = combattant.x;
				sprite.y = combattant.y;
				sprite.look = combattant.look;
				if (animExplosants != null) animExplosants.add(sprite);
 			}

		}
 		if (combattants != null) combattants.flush();
  	repaint();
	}

	void stopExplosion()
	{
		if (combattants != null) if (!combattants.isEmpty()) combattants.flush();
		if (animExplosants != null) if (!animExplosants.isEmpty()) animExplosants.flush();
    repaint();
	}

	void initRameneCombat(int qui, int paysAttaquant, int paysDefenseur)
	{
		AnimSprite sprite;

		if (movingCombattants != null) if (!movingCombattants.isEmpty()) movingCombattants.flush();
		if (qui == 0) //Attaquant détruit, ramène défenseur
		{
			sprite = new AnimSprite(1024, 0, gfxPool, 32, 32, backGnd, 2);
			sprite.destination = paysDefenseur;
      sprite.destinationPoint = new Point(leMonde.pays[paysDefenseur].pointCanon);
      if (leMonde.pays[paysAttaquant].pointDrapeau.getX() <=
            leMonde.pays[paysDefenseur].pointDrapeau.getX())
			{
        sprite.x = (int)leMonde.pays[paysDefenseur].pointDrapeau.getX()+52;
        sprite.y = (int)leMonde.pays[paysDefenseur].pointDrapeau.getY()-12;
			}
			else
			{
        sprite.x = (int)leMonde.pays[paysDefenseur].pointDrapeau.getX()-64;
        sprite.y = (int)leMonde.pays[paysDefenseur].pointDrapeau.getY()-12;
			}
			if (movingCombattants != null) movingCombattants.add(sprite);
		}
		if (qui == 1) //Défenseur détruit, ramène Attaquant
		{
			sprite = new AnimSprite(1024, 0, gfxPool, 32, 32, backGnd, 2);
			sprite.destination = paysAttaquant;
      sprite.destinationPoint = new Point(leMonde.pays[paysAttaquant].pointCanon);
      if (leMonde.pays[paysAttaquant].pointDrapeau.getX() <=
            leMonde.pays[paysDefenseur].pointDrapeau.getX())
			{
        sprite.x = (int)leMonde.pays[paysDefenseur].pointDrapeau.getX()-64;
        sprite.y = (int)leMonde.pays[paysDefenseur].pointDrapeau.getY()-12;
			}
			else
			{
        sprite.x = (int)leMonde.pays[paysDefenseur].pointDrapeau.getX()+52;
        sprite.y = (int)leMonde.pays[paysDefenseur].pointDrapeau.getY()-12;
			}
			if (movingCombattants != null) movingCombattants.add(sprite);
		}
		if (qui == 2) {} //Attaquant ET Défenseur détruits
	}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e)
	{
		e.translatePoint(-8, -57);
		mousePosition = e.getPoint();
	}

	public Point getCursorPos()
	{
		return mousePosition;
	}

}