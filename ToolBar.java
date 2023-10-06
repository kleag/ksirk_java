import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.metal.*;
import java.awt.event.*;
import java.net.URL;


/**
 * ToolBar
 */
public class ToolBar extends JPanel
		implements ActionListener, Runnable
{
	private static String[] iconsFilesNames =
		{"Images/quitter.jpg", "Images/aide.jpg",
		"Images/annuler.jpg", "Images/attackOne.jpg",
		"Images/attackTwo.jpg", "Images/attackThree.jpg",
		"Images/distributionArmees.jpg", "Images/distributionArmeesFinie.jpg",
		"Images/joueurSuivant.jpg", "Images/moveArmies.jpg",
		"Images/moveBackOne.jpg", "Images/moveBackFive.jpg",
		"Images/moveBackTen.jpg", "Images/moveFinish.jpg",
		"Images/moveOne.jpg", "Images/moveFive.jpg",
		"Images/moveTen.jpg", "Images/nouveauJeu.jpg",
		"Images/ouvrirJeu.jpg"};
    private static ImageIcon[] icons;
    private static Font font = new Font("serif", Font.PLAIN, 10);

    public JToolBar toolbar;
    private Thread thread;
    private JButton quitButton, newGameButton, openGameButton, helpButton,
                    joueurSuivantButton, distributionArmeesButton,
                    distributionArmeesFinieButton;
    private Risk risk;

    public ToolBar(Risk risk)
	{
		this.risk = risk;
		toolbar = new JToolBar();
		toolbar.setUI(new MetalToolBarUI());
        setLayout(new BorderLayout());
        setBackground(Color.gray);
		quitButton = addTool("quitter", "Quitter", this);
    newGameButton = addTool("nouveauJeu", "Démarrer un nouveau jeu", this);
		openGameButton = addTool("ouvrirJeu", "Ouvrir un jeu existant", this);
    toolbar.addSeparator(new Dimension(20,6));
		helpButton = addTool("aide", "Aide", this);
        add(toolbar);
    }


  public RiskButton addTool(String str,
                           String toolTip,
                           ActionListener al)
	{
    RiskButton b = null;
    b = (RiskButton) toolbar.add(new RiskButton(str));
		b.setSelected(true);
    b.setToolTipText(toolTip);
    b.addActionListener(al);
    return b;
  }


	public void actionPerformed(ActionEvent e)
	{
    Object obj = e.getSource();
    if (obj instanceof RiskButton)
		{
      RiskButton b = (RiskButton) obj;
      b.setSelected(!b.isSelected());
      if (b.getIcon() == null)
			{
        b.setBackground(b.isSelected() ? Color.green : Color.lightGray);
			}
		}
    if (obj.equals(quitButton))
		{
			System.exit(0);
    }
    if (obj.equals(newGameButton))
		{
			risk.cmNewGame();
    }
    if (obj.equals(openGameButton))
		{
    }
    if (obj.equals(helpButton))
		{
      risk.cmInfo();
    }
    if ( obj.equals(joueurSuivantButton) )
    {
      risk.cmJoueurSuivant();
    }
  }


//    public Dimension getPreferredSize()
//	{
//        return new Dimension(200,38);
//    }


    public void start()
	{
        thread = new Thread(this);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setName("");
        thread.start();
    }


    public synchronized void stop()
	{
        thread = null;
        notifyAll();
    }

    public void run()
	{
        boolean stopped = false;
        thread = null;
    }

  public RiskButton gadgetWithId(String id)
  {
    Component comp;
    RiskButton button;

    for ( int i = 0 ; i < toolbar.getComponentCount(); i++ )
    {
      comp = toolbar.getComponentAtIndex(i);
      if ( comp instanceof RiskButton )
      {
        button = (RiskButton) comp;
        if (button.id == id)
          return button;
      }
    }
    return(null);
  }
}