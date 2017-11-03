package gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author Abdikhaliq Timer
 */
public class TutorialSlides extends JFrame {

	private JLabel attack;
	private JLabel draft;
	private JLabel fortify;
	private JLabel home;
	private JButton jButton1;

	public TutorialSlides() {
		initComponents();
	}

	private void initComponents() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        
		jButton1 = new JButton();
		home = new JLabel();
		draft = new JLabel();
		attack = new JLabel();
		fortify = new JLabel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(new java.awt.Dimension(600, 400));
		getContentPane().setLayout(new AbsoluteLayout());

		jButton1.setBackground(new java.awt.Color(0, 0, 0));
		jButton1.setContentAreaFilled(false);
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		getContentPane().add(jButton1, new AbsoluteConstraints(0, 0, 600, 400));

		home.setIcon(new ImageIcon(getClass().getResource("/image/home.png")));
		getContentPane().add(home, new AbsoluteConstraints(0, 0, 600, 400));

		draft.setIcon(new ImageIcon(getClass().getResource("/image/draft.png")));
		getContentPane().add(draft, new AbsoluteConstraints(0, 0, 600, 400));

		attack.setIcon(new ImageIcon(getClass().getResource("/image/attack.png")));
		getContentPane().add(attack, new AbsoluteConstraints(0, 0, 600, 400));

		fortify.setIcon(new ImageIcon(getClass().getResource("/image/fortify.png")));
		getContentPane().add(fortify, new AbsoluteConstraints(0, 0, 600, 400));

		pack();
		setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        this.setAlwaysOnTop (true);
	}

	public int counter = 0;

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		if (counter == 0) {
			home.setVisible(false);
		} else if (counter == 1) {
			draft.setVisible(false);
		} else if (counter == 2) {
			attack.setVisible(false);
		} else if (counter == 3) {
			this.dispose();
		}
		counter++;
	}

	public static void main(String args[]) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(TutorialSlides.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(TutorialSlides.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(TutorialSlides.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(TutorialSlides.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new TutorialSlides().setVisible(true);
			}
		});
	}

}
