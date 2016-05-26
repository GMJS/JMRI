/*============================================================================*
 * WARNING      This class contains automatically modified code.      WARNING *
 *                                                                            *
 * The method initComponents() and the variable declarations between the      *
 * "// Variables declaration - do not modify" and                             *
 * "// End of variables declaration" comments will be overwritten if modified *
 * by hand. Using the NetBeans IDE to edit this file is strongly recommended. *
 *                                                                            *
 * See http://jmri.org/help/en/html/doc/Technical/NetBeansGUIEditor.shtml for *
 * more information.                                                          *
 *============================================================================*/
package jmri.profile;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import jmri.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Display a list of {@link Profile}s that can be selected to start a JMRI
 * application.
 * <p>
 * This dialog is designed to be displayed while an application is starting. If
 * the last profile used for the application can be found, this dialog will
 * automatically start the application with that profile after 10 seconds unless
 * the user intervenes.
 *
 * @author rhwood
 */
public class ProfileManagerDialog extends JDialog {

    private static final long serialVersionUID = 8335767552519729376L;
    private Timer timer;
    private int countDown;

    /**
     * Creates new form ProfileManagerDialog
     *
     * @param parent {@inheritDoc}
     * @param modal  {@inheritDoc}
     */
    public ProfileManagerDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        ProfileManager.getDefault().addPropertyChangeListener(ProfileManager.ACTIVE_PROFILE, (PropertyChangeEvent evt) -> {
            profiles.setSelectedValue(ProfileManager.getDefault().getActiveProfile(), true);
            profiles.ensureIndexIsVisible(profiles.getSelectedIndex());
            profiles.repaint();
        });
        ProfileManager.getDefault().addPropertyChangeListener(Profile.NAME, (PropertyChangeEvent evt) -> {
            if (evt.getSource().getClass().equals(Profile.class) && evt.getPropertyName().equals(Profile.NAME)) {
                profileNameChanged(((Profile) evt.getSource()));
            }
        });
        this.jScrollPane1.getVerticalScrollBar().addAdjustmentListener((AdjustmentEvent e) -> {
            profilesValueChanged(null);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        listLabel = new JLabel();
        jScrollPane1 = new JScrollPane();
        profiles = new JList<>();
        btnSelect = new JButton();
        btnCreate = new JButton();
        btnUseExisting = new JButton();
        countDownLbl = new JLabel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ResourceBundle bundle = ResourceBundle.getBundle("jmri/profile/Bundle"); // NOI18N
        setTitle(bundle.getString("ProfileManagerDialog.title")); // NOI18N
        setMinimumSize(new Dimension(310, 110));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent evt) {
                formWindowOpened(evt);
            }
            public void windowClosing(WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        listLabel.setText(bundle.getString("ProfileManagerDialog.listLabel.text")); // NOI18N

        profiles.setModel(new ProfileListModel());
        profiles.setSelectedValue(ProfileManager.getDefault().getActiveProfile(), true);
        profiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        profiles.setToolTipText(bundle.getString("ProfileManagerDialog.profiles.toolTipText")); // NOI18N
        profiles.setNextFocusableComponent(btnSelect);
        profiles.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                profilesKeyPressed(evt);
            }
        });
        profiles.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                profilesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(profiles);
        profiles.ensureIndexIsVisible(profiles.getSelectedIndex());
        profiles.getAccessibleContext().setAccessibleName(bundle.getString("ProfileManagerDialog.profiles.AccessibleContext.accessibleName")); // NOI18N

        btnSelect.setText(bundle.getString("ProfileManagerDialog.btnSelect.text")); // NOI18N
        btnSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });

        btnCreate.setText(bundle.getString("ProfileManagerDialog.btnCreate.text")); // NOI18N
        btnCreate.setToolTipText(bundle.getString("ProfilePreferencesPanel.btnCreateNewProfile.toolTipText")); // NOI18N
        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnUseExisting.setText(bundle.getString("ProfileManagerDialog.btnUseExisting.text")); // NOI18N
        btnUseExisting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnUseExistingActionPerformed(evt);
            }
        });

        countDownLbl.setText(bundle.getString("ProfileManagerDialog.countDownLbl.text")); // NOI18N
        countDownLbl.setToolTipText(bundle.getString("ProfileManagerDialog.countDownLbl.toolTipText")); // NOI18N

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(listLabel)
                    .addComponent(countDownLbl))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnUseExisting)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCreate)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelect))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                    .addComponent(listLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSelect)
                    .addComponent(btnCreate)
                    .addComponent(btnUseExisting)
                    .addComponent(countDownLbl))
                .addContainerGap())
        );

        listLabel.getAccessibleContext().setAccessibleName(bundle.getString("ProfileManagerDialog.listLabel.text")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelectActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnSelectActionPerformed
        timer.stop();
        countDown = -1;
        countDownLbl.setVisible(false);
        if (profiles.getSelectedValue() != null) {
            ProfileManager.getDefault().setActiveProfile(profiles.getSelectedValue());
            dispose();
        }
    }//GEN-LAST:event_btnSelectActionPerformed

    private void btnCreateActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        timer.stop();
        countDownLbl.setVisible(false);
        AddProfileDialog apd = new AddProfileDialog(this, true, false);
        apd.setLocationRelativeTo(this);
        apd.setVisible(true);
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnUseExistingActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnUseExistingActionPerformed
        timer.stop();
        countDownLbl.setVisible(false);
        JFileChooser chooser = new JFileChooser(FileUtil.getHomePath());
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setFileFilter(new ProfileFileFilter());
        chooser.setFileView(new ProfileFileView());
        // TODO: Use NetBeans OpenDialog if its availble
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Profile p = new Profile(chooser.getSelectedFile());
                ProfileManager.getDefault().addProfile(p);
                profiles.setSelectedValue(p, true);
            } catch (IOException ex) {
                log.warn("{} is not a profile directory", chooser.getSelectedFile());
                // TODO: Display error dialog - selected file is not a profile directory
            }
        }
    }//GEN-LAST:event_btnUseExistingActionPerformed

    private void formWindowOpened(WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        countDown = ProfileManager.getDefault().getAutoStartActiveProfileTimeout();
        countDownLbl.setText(Integer.toString(countDown));
        timer = new Timer(1000, (ActionEvent e) -> {
            if (countDown > 0) {
                countDown--;
                countDownLbl.setText(Integer.toString(countDown));
            } else {
                setVisible(false);
                ProfileManager.getDefault().setActiveProfile(profiles.getSelectedValue());
                log.info("Automatically starting with profile " + ProfileManager.getDefault().getActiveProfile().getId() + " after timeout.");
                timer.stop();
                countDown = -1;
                dispose();
            }
        });
        timer.setRepeats(true);
        if (profiles.getModel().getSize() > 0
                && null != ProfileManager.getDefault().getActiveProfile()
                && countDown > 0) {
            timer.start();
        } else {
            countDownLbl.setVisible(false);
            btnSelect.setEnabled(false);
        }
    }//GEN-LAST:event_formWindowOpened

    /**
     * Get the active profile or display a dialog to prompt the user for it.
     *
     * @param f - The {@link java.awt.Frame} to display the dialog over.
     * @return the active or selected {@link Profile}
     * @see ProfileManager#getStartingProfile()
     */
    public static Profile getStartingProfile(Frame f) throws IOException {
        if (ProfileManager.getStartingProfile() == null
                || (System.getProperty(ProfileManager.SYSTEM_PROPERTY) == null
                && !ProfileManager.getDefault().isAutoStartActiveProfile())) {
            ProfileManagerDialog pmd = new ProfileManagerDialog(f, true);
            pmd.setLocationRelativeTo(f);
            pmd.setVisible(true);
            ProfileManager.getDefault().saveActiveProfile();
        }
        return ProfileManager.getDefault().getActiveProfile();
    }

    private void profileNameChanged(Profile p) {
        try {
            p.save();
            log.info("Saving profile {}", p.getId());
        } catch (IOException ex) {
            log.error("Unable to save renamed profile: {}", ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    Bundle.getMessage("ProfileManagerDialog.errorRenamingProfile"),
                    Bundle.getMessage("ProfileManagerDialog.errorRenamingProfileTitle"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void profilesValueChanged(ListSelectionEvent evt) {//GEN-FIRST:event_profilesValueChanged
        timer.stop();
        countDownLbl.setVisible(false);
        btnSelect.setEnabled(true);
    }//GEN-LAST:event_profilesValueChanged

    private void formMousePressed(MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        this.profilesValueChanged(null);
    }//GEN-LAST:event_formMousePressed

    private void profilesKeyPressed(KeyEvent evt) {//GEN-FIRST:event_profilesKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.btnSelect.doClick();
        }
    }//GEN-LAST:event_profilesKeyPressed

    @SuppressFBWarnings(value="DM_EXIT", justification="This exit ensures launch is aborted if a profile is not selected or autostarted")
    private void formWindowClosing(WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (countDown != -1) {
            System.exit(255);
        }
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnCreate;
    private JButton btnSelect;
    private JButton btnUseExisting;
    private JLabel countDownLbl;
    private JScrollPane jScrollPane1;
    private JLabel listLabel;
    private JList<Profile> profiles;
    // End of variables declaration//GEN-END:variables
    private static final Logger log = LoggerFactory.getLogger(ProfileManagerDialog.class);
}
