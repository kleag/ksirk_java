class Etat
{
	static int INIT = 1;
	static int INTERMEDE = 2;
	static int NOUVARMEES = 3;
	static int ATTEND = 4;
	static int ATTAQUE = 5;
	static int ATTAQUE2 = 6;
	static int FINATTAQUE = 7;
	static int ENVAHIT = 8;
	static int DEPLACE = 9;
	static int DEPLACE2 = 10;
	static int COMBAT_AMENE = 11;
	static int COMBAT_ANIME = 12;
	static int COMBAT_RETOUR = 13;
	static int ATTENDDEFENSE = 14;
	static int EXPLOSION_ANIME = 15;
  static String[] etats = {"", "INIT", "INTERMEDE", "NOUVARMEES", "ATTEND", "ATTAQUE",
                          "ATTAQUE2", "FINATTAQUE", "ENVAHIT", "DEPLACE", "DEPLACE2",
                          "COMBAT_AMENE", "COMBAT_ANIME", "COMBAT_RETOUR", "ATTENDDEFENSE",
                          "EXPLOSION_ANIME"};

  static String toString(int etat)
  {
    return(etats[etat]);
  }
}