import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class DialogNbJoueurs extends JDialog
				implements ActionListener
{
	private int INbJoueurs;
	private Box box, topBox, bottomBox;
	private JTextField textField;
	private JLabel label;
	private JButton okButton, cancelButton;

	DialogNbJoueurs(Frame risk, int nbJoueurs)
	{
		
		super(risk, true);

		getContentPane().setLayout(new BorderLayout());		

		box = Box.createVerticalBox();
		topBox = Box.createVerticalBox();
		bottomBox = Box.createHorizontalBox();
		
		okButton = new JButton("Ok");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel"); 
		cancelButton.addActionListener(this);
		label = new JLabel("Entrez le nombre de joueurs : ");
		textField = new JTextField(String.valueOf(nbJoueurs));

		topBox.add(label);
		topBox.add(textField);
		bottomBox.add(okButton);
		bottomBox.add(cancelButton);
		box.add(topBox);
		box.add(bottomBox);
		getContentPane().add(box, BorderLayout.CENTER);
		setSize(300,200);
		show();
	}

	boolean	canClose()
	{
		INbJoueurs = 1; //GetDlgItemInt(IDC_NBJOUEURS);
		return true;
	}

	public void execute() {}

	public void actionPerformed(ActionEvent e) 
	{
	    JComponent c = (JComponent) e.getSource();
	    if (c == okButton) 
	    {
	    	((Risk)getOwner()).nbJoueurs = (Integer.valueOf(textField.getText())).intValue();
	    	dispose();
	    }
	    if (c == cancelButton) 
	    {
	    	dispose();
	    }
	}
}