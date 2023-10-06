import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

class DialogSetupJoueur extends JDialog
		implements ActionListener
{
	public String name;
	private int Numero;


	private int INbJoueurs;
	private Box box, topBox, bottomBox;
	private JTextField textField;
	private JLabel label;
	private JButton okButton, cancelButton;

	DialogSetupJoueur(Frame risk, int numero)
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
		label = new JLabel("Joueur n° "+numero+", entrez votre nom :");
		textField = new JTextField("");

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

	public void execute() {}

	public void actionPerformed(ActionEvent e) 
	{
	    JComponent c = (JComponent) e.getSource();
	    if (c == okButton) 
	    {
			name = textField.getText();
	    	dispose();
	    }
	    if (c == cancelButton) 
	    {
			name = "";
	    	dispose();
	    }
	}
}