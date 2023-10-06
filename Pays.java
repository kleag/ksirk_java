import java.awt.*;
import java.awt.image.*;
import java.net.URL;

public class Pays extends Object
{
	int num;
	String nom;
	int nbPoints;
	Point tableauPoints[];
	Point pointDrapeau;
	Point pointCanon;
	Point pointCheval;
	Point pointSoldat;
	int communiqueAvec[];
	int appartientA;
	int nbArmees;
	int nbArmeesAjoutees;
	AnimSprite drapeau;
	SpritesSet spritesCanons;
	SpritesSet spritesChevaux;
	SpritesSet spritesSoldats;
	static int frontieresTab[] = 
	{
2,1,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
1,2,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,1,2,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
1,0,1,2,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,1,1,2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,1,1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,1,0,1,0,2,2,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,1,0,1,0,2,2,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
1,1,0,0,0,0,0,0,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,1,2,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,1,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,1,1,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,1,1,0,0,0,0,0,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,2,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,0,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1,0,1,2,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,2,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,2,1,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,1,1,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,1,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,1,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,1,1,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,1,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,1,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2
	};

	public Pays(String name, int points[], int numero)
	{
		int i;

		nom = name;
		num = numero;
		communiqueAvec = new int[45];
	
		if (numero != 45)
		{
			nbPoints = points[0];
			tableauPoints = new Point[nbPoints];
			for (i = 0; i < nbPoints; i++)
				tableauPoints[i] = (new Point(points[2*i-1+10], points[2*i+10]));
			for (i = 0; i < 45; i++)
			{
				communiqueAvec[i] = frontieresTab[numero*45 + i];
			}
			pointDrapeau = new Point(points[1], points[2]-20);
			pointCanon = new Point(points[3], points[4]-20);
			pointCheval = new Point(points[5], points[6]-20);
			pointSoldat = new Point(points[7], points[8]-20);
			appartientA = -1;
			nbArmees = 1;
			nbArmeesAjoutees = 0;
	
			//Charger Sprites :
			spritesCanons = new SpritesSet(3);
			spritesChevaux = new SpritesSet(3);
			spritesSoldats = new SpritesSet(3);
		}
	}

	void reset()
	{

		if (spritesCanons != null) 
			spritesCanons.flush();
		if (spritesChevaux != null) 
			spritesChevaux.flush();
		if (spritesChevaux != null)
			spritesSoldats.flush();

		if (drapeau != null) drapeau = null;
		appartientA = -1;
		nbArmees = 1;
	}

	void ajouteArmees(GfxPool gfxPool, BackGnd backGnd)
	{
		int i, armees;
		AnimSprite sprite;
	
		armees = nbArmees;
		i = 0;
		if (spritesCanons != null) if (!spritesCanons.isEmpty()) spritesCanons.flush();
		while (armees >= 10) // Ajout des Sprites de canon
		{
			sprite = new AnimSprite(1024, 0, gfxPool, 32, 32, backGnd, 2);
			sprite.destination = 45;             // Sprite immobile
			sprite.x = pointCanon.x+5*i;
			sprite.y = pointCanon.y+5*i;
			if (spritesCanons != null) spritesCanons.add(sprite);
			i++;
			armees -= 10;
		}
		i = 0;
		if (spritesChevaux != null) if (!spritesChevaux.isEmpty()) spritesChevaux.flush();
		while (armees >= 5) // Ajout des Sprites de cavalier
		{
			sprite = new AnimSprite(1024, 96, gfxPool, 32, 32, backGnd, 1);
			sprite.destination = 45;             // Sprite immobile
			sprite.x = pointCheval.x+5*i;
			sprite.y = pointCheval.y+5*i;
			if (spritesChevaux != null) spritesChevaux.add(sprite);
			i++;
			armees -= 5;
		}
		i = 0;
		if (spritesSoldats != null) if (!spritesSoldats.isEmpty()) spritesSoldats.flush();
		while (armees > 0) // Ajout des Sprites de fantassin
		{
			sprite = new AnimSprite(1024, 192, gfxPool, 32, 32, backGnd, 1);
			sprite.destination = 45;             // Sprite immobile
			sprite.x = pointSoldat.x+5*i;
			sprite.y = pointSoldat.y+5*i;
			if (spritesSoldats != null) spritesSoldats.add(sprite);
			i++;
			armees -= 1;
		}
	}

	void affecteDrapeau(GfxPool gfxPool, BackGnd backGnd)
	{
		if ((num != 6)&&(num != 30)&&(num != 39))
		{
			drapeau = new AnimSprite(1024, 288+20*appartientA, gfxPool, 20, 20, backGnd, 4);
			drapeau.destination = 45;             // Sprite immobile
			drapeau.x = pointDrapeau.x;
			drapeau.y = pointDrapeau.y;
		}
	}

	boolean isIn(Point point)
	{
		int i;
	
		for (i = 0; i <= nbPoints-3; i++)
		{
			if (! memeDemiPlan(i, i+1, i+2, point)) return false;
		}
		if (! memeDemiPlan(nbPoints-2, nbPoints-1, 0, point)) return false;
		if (! memeDemiPlan(nbPoints-1, 0, 1, point)) return false;
		return true;
	}

	boolean memeDemiPlan(int n1, int n2, int n3, Point point)
	{
		float d1x, d1y, d2x, d2y, p1x, p1y, p2x, p2y, a, b;
	
		d1x = tableauPoints[n1].x;
		d1y = tableauPoints[n1].y;
		d2x = tableauPoints[n2].x;
		d2y = tableauPoints[n2].y;
		p1x = tableauPoints[n3].x;
		p1y = tableauPoints[n3].y;
		p2x = point.x;
		p2y = point.y;

		if (d1x == d2x)
		{
			if ((p1x-d1x)*(p2x-d1x) > 0) return(true);
			else return(false);
		}
		a = (d2y - d1y)/(d2x - d1x);
		b = d2y - a * d2x;
		if ( ((p1y - (a*p1x + b))) * ((p2y - (a*p2x + b))) > 0 ) return(true);
		else return(false);
	}
}