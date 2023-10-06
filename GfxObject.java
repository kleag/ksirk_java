import java.awt.*;
import java.awt.image.*;
import java.net.URL;

class GfxObject extends Component
{		//Base de tous les objets visibles
	int x,y;                    //Coordonn�es dans la fen�tre
	int largeur, hauteur;		//largeur et hauteur de l'objet

	public GfxObject() { x=0; y=0;}
	public int getX() {return(x);}
	public int getY() {return(y);}
  public Point getPoint() {return(new Point(x, y));}
}