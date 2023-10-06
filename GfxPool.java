import java.awt.*;
import java.awt.image.*;
import java.net.URL;

class GfxPool extends GfxObject
{		//Gère le Gfx-Pool
	private String Pool_Name = "Images/pool.gif";
	private Image pool;			//Pointeur sur les données graphiques
	
	public GfxPool()	//Charge et initialise Pool-Bitmap
	{
        URL url = GfxPool.class.getResource(Pool_Name);
        pool = getToolkit().createImage(Pool_Name);
		largeur = pool.getWidth(this);	//Fixer largeur et hauteur
		hauteur = pool.getHeight(this); //Noter pointeur sur les données
	}

	public ImageProducer getSource()
	{
		return(pool.getSource());
	}

	public Image getImage()
	{
		return(pool);
	}
}