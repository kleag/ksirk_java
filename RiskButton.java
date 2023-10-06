import javax.swing.*;
import javax.swing.plaf.metal.*;
import java.net.URL;

/**
 * ToolBar
 */
public class RiskButton extends JButton
{
        public String id;

        public RiskButton(String str)
        {
                super(new ImageIcon(ToolBar.class.getResource("Images/" + str + ".jpg")));
                id = str;
        }

}
