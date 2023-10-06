import java.util.*;
import java.awt.*;
import java.lang.*;
import java.lang.reflect.*;

public class SpritesSet extends HashSet
{
	SpritesSet() {super();}

	SpritesSet(int initialSize) {super(initialSize);}

	void flush()
	{
		removeAll(this);
	}

	void forEach(String voidVoidMethodName)
	{
		AnimSprite as;
		Class c;
		Method m;
		Iterator iter = this.iterator();

		try
		{
			c = Class.forName("AnimSprite");
			m = c.getMethod(voidVoidMethodName, null);

    	while (iter.hasNext())
			{
     		as = (AnimSprite)iter.next();
				m.invoke(as, new Object[0]);
			}
		}
    catch(Exception e) {System.out.println("Execption in SpritesSet.forEach(String) : "+e);}
	}

	void forEach(String voidVoidMethodName, Object[] args)
	{
		AnimSprite as;
		Class c;
		Class[] argsClasses;
		Method m;
    Iterator iter = this.iterator();

		try
		{
			argsClasses = new Class[args.length];
			for (int i = 0; i < args.length; i++) argsClasses[i] = Class.forName("java.awt.Graphics");
			c = Class.forName("AnimSprite");
			m = c.getMethod(voidVoidMethodName, argsClasses);
    	while (iter.hasNext())
			{
     		as = (AnimSprite)iter.next();
				m.invoke(as, args);
			}
		}
    catch(Exception e) {System.out.println("Execption in SpritesSet.forEach(String, Object[]) : "+e);}
	}

	AnimSprite firstThat(String voidVoidMethodName)
	{
		AnimSprite as;
    Iterator iter = this.iterator();
		Class c;
		Method m;

		try
		{
			c = Class.forName("AnimSprite");
			m = c.getMethod(voidVoidMethodName, null);
    	while (iter.hasNext())
			{
     		as = (AnimSprite)iter.next();
				if (((Boolean)m.invoke(as, new Object[0])).booleanValue())
					return(as);
			}
		}
    catch(Exception e) {System.out.println("Execption in SpritesSet.firstThat(String) : "+e);}
		return(null);
	}

	AnimSprite removeEntry(int index)
	{
		AnimSprite[] as = (AnimSprite[])this.toArray();
		AnimSprite removedElem;

		removedElem = as[index];
		remove((Object)removedElem);

		return(removedElem);
	}

  public AnimSprite[] asArray()
  {
    return((AnimSprite[])toArray(new AnimSprite[size()]));
  }
}