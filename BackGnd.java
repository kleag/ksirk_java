import java.awt.*;
import java.awt.image.*;
import java.net.URL;

public class BackGnd extends GfxObject
{		//G�re l'arri�re-plan
	private GfxPool gfxPool;		   //R�f�rence sur Pool avec �lements de construction
	private Image bits;                    //Pointeur sur les donn�es graphiques
	private long taille;                   //Taille de l'image en octets

	public int levelMaxX, levelMaxY;
		
	BackGnd(GfxPool aGfxPool, int aLargeur, int aHauteur, int lMX, int lMY)
	{		//Cr�e arri�re-plan
		gfxPool = aGfxPool;
		largeur = aLargeur;						//Fixe largeur et hauteur
		hauteur = aHauteur;
		levelMaxX = lMX;
		levelMaxY = lMY;
		x = (levelMaxX - largeur)/2;
		y = (levelMaxY - hauteur)/2;
		build();
	}

	void build()
	{	//Construire l'arri�re-plan d'apr�s la carte
		// �.-�.-d. fait un clipping ?

		if ((x+largeur > levelMaxX) || (y+hauteur > levelMaxY))
			{System.out.println("BackGnd.build() error!");}
		else
		{
			PixelGrabber pg = new PixelGrabber(gfxPool.getImage(), x, y, largeur, hauteur, true);
	    	try 
			{
	    		pg.grabPixels();
			} 
			catch (InterruptedException e) 
			{
				System.err.println("interrupted waiting for pixels!");
				return;
			}
			
			if ((pg.getStatus() & ImageObserver.ABORT) != 0) 
			{
				System.err.println("image fetch aborted or errored");
				return;
	    	}
			bits = createImage(new MemoryImageSource(largeur, hauteur, (int[])pg.getPixels(), 0, largeur));
		}
	}

	void scroll(Dir dir, int step)
	{		//D�filement de l'arri�re-plan
		if (dir.dir == Dir.left)
		{
			if ((x + largeur + step) < levelMaxX)
					x += step;
			else x = 0;
		}
		else if (dir.dir == Dir.right)
		{
				if ((int)(x - step) > 0)
					x -= step;
				else x = levelMaxX-largeur;
		}
		else if (dir.dir == Dir.up)
		{
				if ((y + hauteur + step) < levelMaxY)
					y+=step;
				else y=0;
		}
		else if (dir.dir == Dir.down)
		{
				if ((y - step) > 0) y -= step;
				else y=levelMaxY-hauteur;
		}
		PixelGrabber pg = new PixelGrabber(gfxPool.getImage(), x, y, largeur, hauteur, true);
    	try 
		{
    		pg.grabPixels();
		} 
		catch (InterruptedException e) 
		{
			System.err.println("interrupted waiting for pixels!");
			return;
		}
		
		if ((pg.getStatus() & ImageObserver.ABORT) != 0) 
		{
			System.err.println("image fetch aborted or errored");
			return;
    	}
		bits = createImage(new MemoryImageSource(largeur, hauteur, (int[])pg.getPixels(), 0, largeur));
		repaint();
	}

	public void paint(Graphics g)
	{
		g.drawImage(bits,0,0,this);
	}
// Fin Classe BackGnd
}