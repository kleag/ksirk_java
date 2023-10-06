import java.awt.*;
import java.awt.image.*;
import java.net.URL;

//Traiter le pb de WinGLargeur et Hauteur
public class AnimSprite extends GfxObject
{		//Sprites animés
	GfxPool gfxPool;			//Indique le Gfx-Pool
	BackGnd backGnd;        	//Arrière-plan utilisé
	int destination;			//Pays de destination du sprite
	Point destinationPoint;
  private Image[] images;     //Images des sprites utilisées

	int last_x,last_y;
  private Image[] src_Ofs_left, src_Ofs_right, src_Ofs_vert;
              //Images des sprites pour gauche, droite, haut et bas
	Dir look;  //Direction du "regard"
	int frames, actFrame;       //Nombre d'images individuelles, Image active
	boolean toBePaint;

	AnimSprite(int aSrc_x, int aSrc_y, GfxPool aGfxPool,
								int aLargeur, int aHauteur,
								BackGnd aBackGnd, int aFrames)
	{		//Crée un Sprite animé
		gfxPool = aGfxPool;	//Reprendre Gfx-Pool
		backGnd = aBackGnd;	//Pointeur sur arrière-plan
		hauteur = aHauteur;
		largeur = aLargeur; //Charger largeur et hauteur
		destination = 45;

    src_Ofs_right=      //Pointeur sur Anim. droite
      new Image[aFrames];
		src_Ofs_left=				// Pointeur sur Anim. gauche
      new Image[aFrames];
    src_Ofs_vert=       // Pointeur sur Anim. vertical
      new Image[aFrames];

		frames = aFrames;		//Nombre d'images individuelles
		actFrame = 0;        //Commencer par 0
		images = new Image[frames];
		look = new Dir(Dir.right);       //Et regarder vers la droite
		last_x = 32000; last_y = 32000;

    //Chargement des images pour droite
    for (int i = 0; i < frames; i++)
		{
			PixelGrabber pg = new PixelGrabber(gfxPool.getImage(), aSrc_x+(i*largeur), aSrc_y, largeur, hauteur, true);
	    	try
			{
	    		pg.grabPixels();
				if ((pg.getStatus() & ImageObserver.ABORT) != 0)
				{
					System.err.println("image fetch aborted or errored");
					return;
	    		}
	    		int[] pixels = (int[])pg.getPixels();
        src_Ofs_right[i] = createImage(new MemoryImageSource(largeur, hauteur, pixels, 0, largeur));
			}
			catch (Exception e)
			{
				System.err.println("interrupted waiting for pixels!");
				return;
			}
		}
    //Chargement des images pour gauche
    for (int i = 0; i < frames; i++)
		{
      PixelGrabber pg = new PixelGrabber(gfxPool.getImage(), aSrc_x+(i*largeur), aSrc_y + hauteur, largeur, hauteur, true);
	    	try
			{
	    		pg.grabPixels();
				if ((pg.getStatus() & ImageObserver.ABORT) != 0)
				{
					System.err.println("image fetch aborted or errored");
					return;
	    		}
	    		int[] pixels = (int[])pg.getPixels();
        src_Ofs_left[i] = createImage(new MemoryImageSource(largeur, hauteur, pixels, 0, largeur));
			}
			catch (Exception e)
			{
				System.err.println("interrupted waiting for pixels!");
				return;
			}
		}
    //Chargement des images pour vertical
    for (int i = 0; i < frames; i++)
		{
      PixelGrabber pg = new PixelGrabber(gfxPool.getImage(), aSrc_x+(i*largeur), aSrc_y + (hauteur*2), largeur, hauteur, true);
	    	try
			{
	    		pg.grabPixels();
				if ((pg.getStatus() & ImageObserver.ABORT) != 0)
				{
					System.err.println("image fetch aborted or errored");
					return;
	    		}
	    		int[] pixels = (int[])pg.getPixels();
        src_Ofs_vert[i] = createImage(new MemoryImageSource(largeur, hauteur, pixels, 0, largeur));
			}
			catch (Exception e)
			{
				System.err.println("interrupted waiting for pixels!");
				return;
			}
		}
    images = src_Ofs_right;
    toBePaint = true;
	}

	public void paint(Graphics g)
	{		//Dessin d'un Sprite animé
		long src_Ofs = (long)0.0, source_eff;
		int x_eff, y_eff, largeur_eff, hauteur_eff;

    if (look.dir == Dir.right) images = src_Ofs_right;
    if (look.dir == Dir.left) images = src_Ofs_left;
    if (look.dir == Dir.up) images = src_Ofs_vert;
    if (look.dir == Dir.down) images = src_Ofs_vert;

		src_Ofs += largeur*(actFrame >> 1); //Sur l'image active
		x_eff = x - backGnd.getX();    //Coordonnées de la fenêtre
		y_eff = y - backGnd.getY();
		largeur_eff = largeur;         //largeur effective
		hauteur_eff = hauteur;         //hauteur
		source_eff = src_Ofs;          //Et Offset
		if (x_eff < 0)
		{								//Clipping gauche
			source_eff -= x_eff;       	//Augmenter Offset
			largeur_eff += x_eff;		//Réduire largeur
			x_eff = 0;                  //à Coordonnée 0
		}
		if (y_eff < 0)
		{	 							//Clipping en haut
			hauteur_eff += y_eff;		//Réduire hauteur
			y_eff = 0;					//en cooordonnée 0
		}
										//Clipping droite
		if (( x_eff > backGnd.levelMaxX ) || ( y_eff > backGnd.levelMaxY ))
			toBePaint = false;

		if (toBePaint) g.drawImage(images[actFrame],x_eff,y_eff,this);
	}

	public void kill()
	{		//Effacer Sprite de l'écran
//    toBePaint = false;
	}

	public void nextFrame()
	{
			actFrame++;    //Image suivante
			if (actFrame >= frames)
					actFrame=0; //Revenir au départ
	}

	void moveIt(Point destinationPoint)
	{
		if (x < destinationPoint.getX())
		{
			look = new Dir(Dir.right);
      if (destinationPoint.getX() - x > 5) x += 5;
      if (destinationPoint.getX() - x <= 5) x++;
		}
    if (x > destinationPoint.getX())
		{
			look = new Dir(Dir.left);
      if (x - destinationPoint.getX() > 5) x -= 5;
      if (x - destinationPoint.getX() <= 5) x--;
		}
    if (y < destinationPoint.getY())
		{
      if (destinationPoint.getY() - y > 5) y += 5;
      if (destinationPoint.getY() - y <= 5) y++;
		}
    if (y > destinationPoint.getY())
		{
      if (y - destinationPoint.getY() > 5) y -= 5;
      if (y - destinationPoint.getY() <= 5) y--;
		}
		nextFrame();
	}

	public boolean isLastFrame()
	{
		if (actFrame == (frames-1)) return true;
		else return false;
	}
}