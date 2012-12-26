/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import javax.swing.JApplet;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

/**
 *
 * @author Asish
 */
public class MainApplet extends JApplet{
    public void init()
    {
        //SUBSTANCE SKIN ENABLE
/*        javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
        try {
            System.out.println("Using substance!"); 
            javax.swing.UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegistrationPage().setVisible(true);
            }
        });
    }
}
