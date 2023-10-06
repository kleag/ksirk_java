import java.awt.*;
import java.awt.image.*;
import java.net.URL;

public class Dir
{
	public static int stat=0, left=1, right=2;
	public static int up=3, down=4, N=5, S=6;
	public static int E=7, O=8, NO=9, SO=10;
	public static int SE=11, NE=12;
	public int dir;

	Dir(int dir) {this.dir = dir;}

	public boolean equal(Dir autreDir)
	{
		return(dir == autreDir.dir);
	}
}