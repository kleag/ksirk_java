import java.awt.*;

public class ONU
{
	Pays[] pays;

	//Informations globales du monde
	public int levelMaxX = 1024; // largeur totale du monde
	public int levelMaxY = 768;  // hauteur totale du monde
	char frontieresTab[];

	// Modèle : NOM[] = {NbPoints, drapeau, canons, chevaux, soldats, points}
	int WUSA[] = {5, 182, 346, 126, 282, 162, 292, 147, 329, 131, 274, 130, 330, 153, 378, 206, 386, 206, 274};
	int EUSA[] = {5, 269, 283, 213, 282, 250, 300, 214, 330, 206, 274, 206, 386, 254, 391, 258, 384, 308, 274};
	int QUEBEC[] = {5, 258, 232, 247, 202, 223, 239, 275, 235, 217, 274, 308, 274, 323, 261, 323, 213, 217, 213};
	int ONTARIO[] = {5, 192, 248, 96, 213, 134, 240, 183, 217, 131, 274, 217, 274, 217, 213, 82, 213, 82, 237};
	int ALBERTA[] = {4, 189, 187, 93, 164, 149, 172, 189, 154, 82, 213, 319, 213, 308, 113, 82, 113};
	int ALASKA[] = {4, 61, 163, 20, 147, 45, 174, 16, 191, 82, 113, 1, 113, 1, 247, 82, 247};
	int GROENLAND1[] = {5, 0, 0, 0, 0, 0, 0, 0, 0, 319, 213, 360, 213, 553, 23, 308, 23, 308, 113};
	int GROENLAND2[] = {4, 369, 129, 245, 36, 337, 72, 331, 117, 204, 23, 204, 113, 308, 113, 308, 23};
	int MEXIQUE[] = {4, 225, 425, 170, 381, 197, 396, 236, 387, 153, 378, 229, 464, 295, 428, 254, 391};
	int COLOMBIE[] = {4, 277, 508, 247, 450, 261, 471, 249, 497, 229, 464, 229, 545, 295, 545, 295, 428};
	int BRESIL[] = {4, 318, 494, 300, 458, 298, 500, 335, 497, 295, 545, 372, 545, 372, 500, 295, 428};
	int PARAGUAY[] = {4, 282, 575, 274, 546, 295, 581, 319, 547, 267, 545, 271, 614, 372, 614, 372, 545};
	int CHILI[] = {4, 310, 620, 273, 617, 270, 647, 263, 677, 265, 614, 265, 731, 291, 731, 337, 614};
	int ISLANDE[] = {4, 429, 189, 425, 155, 393, 155, 400, 180, 405, 169, 405,  202, 438, 202, 438, 169};
	int RU[] = {4, 464, 214, 434, 209, 472, 205, 446, 230, 445, 225, 479, 270, 506, 244, 485, 204};
	int NEUROPE[] = {5, 537, 240, 491, 232, 499, 214, 519, 216, 506, 244, 494, 260, 556, 260, 556, 222, 521, 233};
	int SEUROPE[] = {4, 538, 262, 483, 255, 464, 282, 510, 255, 494, 260, 427, 315, 556, 315, 552, 260};
	int EEUROPE[] = {4, 581, 240, 559, 200, 561, 225, 558, 245, 556, 315, 603, 294, 601, 179, 556, 222};
	int SCANDINAVIE[] = {7, 529, 165, 538, 134, 559, 158, 498, 169, 504, 205, 513, 227, 521, 233, 556, 222, 601, 179, 558, 129, 508, 185};
	int RUSSIE[] ={5, 673, 160, 642, 174, 744, 176, 720, 123, 601, 179, 602, 214, 788, 214, 788, 79, 657, 79};
	int IRAN[] = {5, 631, 325, 600, 286, 624, 307, 609, 335, 613, 354, 649, 394, 654, 394, 654, 294, 603, 294};
	int CHINE[] = {5, 792, 321, 716, 268, 733, 310, 775, 278, 718, 368, 827, 368, 861, 328, 879, 263, 704, 263};
	int PAKISTAN[] = {4, 660, 340, 667, 269, 650, 294, 675, 320, 654, 294, 654, 368, 718, 368, 704, 263};
	int KAZAKSTAN[] = {5, 650, 255, 628, 216, 676, 221, 605, 246, 601, 214, 603, 294, 654, 294, 704, 263, 780, 214};
	int SIBERIE[] = {4, 871, 162, 809, 159, 933, 166, 835, 191, 788, 214, 934, 260, 1022, 170, 788, 79};
	int MONGOLIE[] = {4, 803, 249, 744, 232, 776, 213, 824, 226, 704, 263, 875, 263, 934, 260, 788, 214};
	int JAPON[] = {4, 884, 327, 887, 283, 851, 335, 861, 302, 889, 296, 852, 352, 884, 352, 908, 296};
	int ALGERIE[] = {4, 468, 346, 467, 329, 446, 358, 490, 352, 435, 391, 533, 391, 533, 325, 435, 325};
	int NIGER[] = {4, 502, 411, 437, 390, 467, 411, 485, 386, 533,391, 533, 442, 435, 442, 435, 391};
	int EGYPTE[] = {5, 564, 397, 538, 366, 530, 395, 560, 420, 556, 354, 619, 445, 619, 459, 533, 459, 533, 354};
	int GABON1[] = {4, 0, 0, 0, 0, 0, 0, 0, 0, 435, 442, 510, 503, 533, 503, 533, 442};
	int GABON2[] = {4, 571, 476, 497, 436, 551, 457, 516, 460, 533, 503, 619, 503, 619, 459, 533, 459};
	int ZAIRE[] = {4, 566, 533, 520, 499, 559, 500, 532, 522, 589, 561, 619, 503, 510, 503, 512, 561};
	int AF_DU_SUD[] = {4, 548, 575, 529, 600, 552, 558, 511, 560, 512, 561, 534, 629, 567, 629, 589, 561};
	int MADAGASCAR[] = {5, 622, 530, 595, 546, 598, 521, 597, 570, 580, 590, 625, 590, 625, 526, 607, 526, 589, 561};
	int ARABIE[] = {4, 600, 368, 598, 396, 610, 366, 569, 355, 556, 354, 613, 354, 657, 405, 619, 443};
	int MOY_ORIENT[] = {4, 595, 328, 549, 304, 577, 297, 571, 318, 603, 294, 556, 315, 556, 354, 613, 354};
	int INDE[] = {5, 700, 388, 699, 369, 682, 400, 663, 370, 654, 368, 654, 394, 702, 467, 742, 390, 742, 368};
	int VIETNAM[] = {4, 768, 403, 738, 366, 772, 369, 741, 403, 742, 390, 742, 468, 777, 468, 827, 368};
	int INDONESIE1[] = {3, 0, 0, 0, 0, 0, 0, 0, 0, 742, 468, 779, 528, 828, 488};
	int INDONESIE2[] = {4, 797, 460, 752, 471, 783, 481, 797, 438, 828, 488, 825, 454, 784, 454, 777, 468};
	int PAPOUASIE[] = {4, 855, 472, 861, 470, 846, 491, 879, 489, 875, 526, 912, 526, 886, 485, 855, 485};
	int WAUSTRALIE[] = {6, 843, 563, 803, 560, 816, 587, 826, 530, 875, 526, 850, 526, 801, 571, 808, 628, 831, 635, 859, 621};
	int EAUSTRALIE[] = {5, 908, 584, 871, 600, 872, 538, 861, 569, 875, 526, 859, 621, 895, 655, 925, 611, 912, 526};
	int NVLLE_ZEL[] = {4, 960, 635, 964, 616, 940, 648, 955, 645, 972, 612, 936, 688, 977, 683, 1000, 642};
	
	ONU()
	{
	
		pays = new Pays[46];
		pays[0] = new Pays("Etats Unis de l'Ouest", WUSA, 0);
		pays[1] = new Pays("Etats Unis de l'Est", EUSA, 1);
		pays[2] = new Pays("Québec", QUEBEC, 2);
		pays[3] = new Pays("Ontario", ONTARIO, 3);
		pays[4] = new Pays("Alberta", ALBERTA, 4);
		pays[5] = new Pays("Alaska", ALASKA, 5);
		pays[6] = new Pays("Groenland", GROENLAND1, 6);
		pays[7] = new Pays("Groenland", GROENLAND2, 7);
		pays[8] = new Pays("Mexique", MEXIQUE, 8);
		pays[9] = new Pays("Colombie", COLOMBIE, 9);
		pays[10] = new Pays("Brésil", BRESIL, 10);
		pays[11] = new Pays("Paraguay", PARAGUAY, 11);
		pays[12] = new Pays("Chili", CHILI, 12);
		pays[13] = new Pays("Islande", ISLANDE, 13);
		pays[14] = new Pays("Royaume Uni", RU, 14);
		pays[15] = new Pays("Bénélux", NEUROPE, 15);
		pays[16] = new Pays("France", SEUROPE, 16);
		pays[17] = new Pays("Prusse", EEUROPE, 17);
		pays[18] = new Pays("Scandinavie", SCANDINAVIE, 18);
		pays[19] = new Pays("Russie", RUSSIE, 19);
		pays[20] = new Pays("Iran", IRAN, 20);
		pays[21] = new Pays("Chine", CHINE, 21);
		pays[22] = new Pays("Pakistan", PAKISTAN, 22);
		pays[23] = new Pays("Kazakstan", KAZAKSTAN, 23);
		pays[24] = new Pays("Sibérie", SIBERIE, 24);
		pays[25] = new Pays("Mongolie", MONGOLIE, 25);
		pays[26] = new Pays("Japon", JAPON, 26);
		pays[27] = new Pays("Algérie", ALGERIE, 27);
		pays[28] = new Pays("Niger", NIGER, 28);
		pays[29] = new Pays("Egypte", EGYPTE, 29);
		pays[30] = new Pays("Gabon", GABON1, 30);
		pays[31] = new Pays("Gabon", GABON2, 31);
		pays[32] = new Pays("Zaire", ZAIRE, 32);
		pays[33] = new Pays("Afrique du Sud", AF_DU_SUD, 33);
		pays[34] = new Pays("Madagascar", MADAGASCAR, 34);
		pays[35] = new Pays("Arabie", ARABIE, 35);
		pays[36] = new Pays("Moyen Orient", MOY_ORIENT, 36);
		pays[37] = new Pays("Inde", INDE, 37);
		pays[38] = new Pays("Vietnam", VIETNAM, 38);
		pays[39] = new Pays("Indonésie", INDONESIE1, 39);
		pays[40] = new Pays("Indonésie", INDONESIE2, 40);
		pays[41] = new Pays("Papouasie", PAPOUASIE, 41);
		pays[42] = new Pays("Australie Occidentale", WAUSTRALIE, 42);
		pays[43] = new Pays("Nouvelles Galles du Sud", EAUSTRALIE, 43);
		pays[44] = new Pays("Nouvelle Zélande", NVLLE_ZEL, 44);
		pays[45] = new Pays("", null, 45);
	}

	void reset()
	{
		int i;

		for (i = 0; i <= 44; i++) pays[i].reset();
	}
}