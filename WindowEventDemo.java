/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WindowEventDemo.java
 *
 * Created on Apr 25, 2018, 5:33:17 PM
 */
package centrolControl;

import clojure.core$resultset_seq$thisfn__4495;
import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.micromanager.MMStudio;
import org.micromanager.utils.MMException;
import org.micromanager.utils.MMScriptException;

import org.micromanager.api.PositionList;

/**
 *
 * @author Nikon
 */
public class WindowEventDemo extends javax.swing.JFrame {

    //Fluidic Control
    protected Fluidic experiment;
    protected Parser instr_set;
    protected MMStudio gui_;
    private Preferences pref = Preferences.userRoot().node(getClass().getName());
    

    /** Creates new form WindowEventDemo */
    public WindowEventDemo() {
        experiment = new Fluidic();
        instr_set = new Parser();
        gui_ = new MMStudio(false);
        initComponents();

        Fluidic.showMessage("Please choose the intruction .txt file", "File Selection");
        String instructionListFile = fileChooser();
        instructionListFile = instructionListFile.replace("\\", "\\\\");
        instr_set.parseFile(instructionListFile);

        

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnWash = new javax.swing.JButton();
        btnInjectBuffer = new javax.swing.JButton();
        btnCyc0 = new javax.swing.JButton();
        btnInvokeMM = new javax.swing.JButton();
        btnSequencing = new javax.swing.JButton();
        btnContinueCyc0 = new javax.swing.JButton();
        LabelNumOfCyc = new javax.swing.JLabel();
        jFormattedTextFieldOutput = new javax.swing.JFormattedTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaOutput = new javax.swing.JTextArea();
        jLabelOutput = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnWash.setText("Wash");
        btnWash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWashActionPerformed(evt);
            }
        });

        btnInjectBuffer.setText("Inject Buffer");
        btnInjectBuffer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInjectBufferActionPerformed(evt);
            }
        });

        btnCyc0.setText("Cycle 0");
        btnCyc0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCyc0ActionPerformed(evt);
            }
        });

        btnInvokeMM.setText("Set FOVs");
        btnInvokeMM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvokeMMActionPerformed(evt);
            }
        });

        btnSequencing.setText("Run Sequencing");
        btnSequencing.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSequencingMouseClicked(evt);
            }
        });
        btnSequencing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSequencingActionPerformed(evt);
            }
        });

        btnContinueCyc0.setText("IM buffer");
        btnContinueCyc0.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnContinueCyc0MouseClicked(evt);
            }
        });
        btnContinueCyc0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContinueCyc0ActionPerformed(evt);
            }
        });

        LabelNumOfCyc.setText("Number Of Cycles");

        jFormattedTextFieldOutput.setText("1");
        jFormattedTextFieldOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextFieldOutputActionPerformed(evt);
            }
        });

        jTextAreaOutput.setColumns(20);
        jTextAreaOutput.setEditable(false);
        jTextAreaOutput.setLineWrap(true);
        jTextAreaOutput.setRows(5);
        jTextAreaOutput.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextAreaOutput);

        jLabelOutput.setText("Output");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(btnWash)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnInjectBuffer))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(btnCyc0)
                        .addGap(14, 14, 14)
                        .addComponent(btnInvokeMM)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnContinueCyc0))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelOutput)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSequencing)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextFieldOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LabelNumOfCyc)))
                .addGap(149, 149, 149))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnWash)
                    .addComponent(btnInjectBuffer))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCyc0)
                    .addComponent(btnInvokeMM)
                    .addComponent(btnContinueCyc0))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSequencing)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jFormattedTextFieldOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(LabelNumOfCyc)))
                .addGap(31, 31, 31)
                .addComponent(jLabelOutput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {LabelNumOfCyc, btnSequencing});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void updateTextArea(final String inputs) {
        System.out.println(inputs);
        (new Thread() {
            @Override
            public void run(){
                try{
                    jTextAreaOutput.append(inputs + "\n");
                } catch (Exception exception) {
                    System.out.println("There was an error in the thread.");
                }
            }
        }).start();
    }

    private void btnWashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWashActionPerformed
        try {
            // TODO add your handling code here:
            // experiment.wash();

            List<Instruction> washInstr = instr_set.getSectionInstructions("WASH");
            experiment.initiate();
            for (Instruction ins : washInstr) {
                if (ins.getName().equals("IMAGING")) {
                    continue;
                } else if (ins.isWaitUserInstruction()) {
                    experiment.showMessage("Wash is done.", "Wash");
                } else {
                    experiment.runInstruction(ins);
                    updateTextArea(ins.toString());

                }

            }


        } catch (InterruptedException ex) {
            Logger.getLogger(WindowEventDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnWashActionPerformed

    private void btnInjectBufferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInjectBufferActionPerformed
        try {
            // TODO add your handling code here:
            // experiment.injectBuffer();

            List<Instruction> injectBufferInstr = instr_set.getSectionInstructions("BUFFER INJECTION");
            experiment.initiate();
            for (Instruction ins : injectBufferInstr) {
                if (ins.getName().equals("IMAGING")) {
                    continue;
                } else if (ins.isWaitUserInstruction()) {
                    experiment.showMessage("Buffer Injection is done.", "Message");
                } else {
                    experiment.runInstruction(ins);
                    updateTextArea(ins.toString());
                }

            }




        } catch (InterruptedException ex) {
            Logger.getLogger(WindowEventDemo.class.getName()).log(Level.SEVERE, null, ex);
            //} catch (FileNotFoundException ex) {
            //    Logger.getLogger(WindowEventDemo.class.getName()).log(Level.SEVERE, null, ex);
            //} catch (IOException ex) {
            Logger.getLogger(WindowEventDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("There was an error.");
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnInjectBufferActionPerformed

    private void btnCyc0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCyc0ActionPerformed
        try {
            // TODO add your handling code here:
            //experiment.startIncorp0();

            List<Instruction> incorp0Instr = instr_set.getSectionInstructions("INCORP 0 START");
            experiment.initiate();
            for (Instruction ins : incorp0Instr) {
                if (ins.getName().equals("IMAGING")) {
                    experiment.showMessage("Please run MultiD acquisition manually", "Message");
                } else if (ins.isWaitUserInstruction()) {
                    experiment.showMessage("Please select FOV", "Message");
                    break;
                } else {
                    experiment.runInstruction(ins);
                    updateTextArea(ins.toString());
                }

            }



        } catch (InterruptedException ex) {
            Logger.getLogger(WindowEventDemo.class.getName()).log(Level.SEVERE, null, ex);
            //} catch (FileNotFoundException ex) {
            //   Logger.getLogger(WindowEventDemo.class.getName()).log(Level.SEVERE, null, ex);
            //} catch (IOException ex) {
            Logger.getLogger(WindowEventDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("There was an error.");
        }
    }//GEN-LAST:event_btnCyc0ActionPerformed

    private void btnInvokeMMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvokeMMActionPerformed
        // TODO add your handling code here:
        //Imaging System Control

        //open Acquisition Control Dialog for cycle 0
        gui_.openAcqControlDialog();

    }//GEN-LAST:event_btnInvokeMMActionPerformed

    private void btnSequencingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSequencingMouseClicked
        try {
            int numOfCyc = Integer.parseInt(jFormattedTextFieldOutput.getText());

            Fluidic.showMessage("Please choose the Multi-D configuration file", "File Selection");
            String acquisitionConfigFile = fileChooser();
            acquisitionConfigFile = acquisitionConfigFile.replace("\\", "\\\\");
            gui_.loadAcquisition(acquisitionConfigFile);
//            gui_.loadAcquisition("C:\\Users\\Nikon\\Desktop\\Micromanager_test\\20180427_testrun\\AcqSettings20180427.xml");


            PositionList positionList = gui_.getPositionList();
            Fluidic.showMessage("Please choose the position list file", "File Selection");
            String positionListFile = fileChooser();
            positionListFile = positionListFile.replace("\\", "\\\\");
            positionList.load(positionListFile);
//            positionList.load("C:\\Users\\Nikon\\Desktop\\Micromanager_test\\20180427_testrun\\20180427_test.pos");


            Fluidic.showMessage("Please choose where you want to save the images", "Save Directory");
            String saveDirectory = dirChooser();
            saveDirectory = saveDirectory.replace("\\", "\\\\");

            if (numOfCyc > 1) {
                for (int i = 0; i < numOfCyc; i++) {

                    List<Instruction> incorpNInstr = instr_set.getSectionInstructions("INCORP N");
                    experiment.initiate();
                    for (Instruction ins : incorpNInstr) {
                        if (ins.getName().equals("IMAGING")) {
                            gui_.runAcquisition("Incorp", saveDirectory);
                        } else if (ins.isWaitUserInstruction()) {
                            //TODO: Fill this in with what to do
                        } else {
                            experiment.runInstruction(ins);
                            updateTextArea(ins.toString());
                        }

                    }
                }

            } else if (numOfCyc == 1) {

                List<Instruction> incorpNInstr = instr_set.getSectionInstructions("INCORP N");
                experiment.initiate();
                for (Instruction ins : incorpNInstr) {
                    if (ins.getName().equals("IMAGING")) {
                        gui_.runAcquisition("Incorp", saveDirectory);
                    } else if (ins.isWaitUserInstruction()) {
                        //TODO: Fill this in with what to do
                    } else {
                        experiment.runInstruction(ins);
                        updateTextArea(ins.toString());
                    }

                }
            } else {
                throw new IllegalArgumentException("Cannot run negative number of incorporation cycles.");
            }
            gui_.closeSequence(true);
            gui_.closeAllAcquisitions();

        } catch (InterruptedException ex) {
            Logger.getLogger(WindowEventDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MMException ex) {
            Logger.getLogger(WindowEventDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MMScriptException ex) {
            Logger.getLogger(WindowEventDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("There was an error.");
        }
    }//GEN-LAST:event_btnSequencingMouseClicked

    private String fileChooser() {
        // Retrieve the selected path or use
        // an empty string if no path has
        // previously been selected
        String path = pref.get("DEFAULT_PATH", "");

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("position, xml and txt files", "xml", "pos", "txt");
        chooser.addChoosableFileFilter(fileFilter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // set the path that was saved in preferences
        chooser.setCurrentDirectory(new File(path));

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            chooser.setCurrentDirectory(f);

            // Save the selected path
            pref.put("DEFAULT_PATH", f.getAbsolutePath());
            return f.getAbsolutePath();
        }

        return chooser.getSelectedFile().getAbsolutePath();
    }

    private String dirChooser() {
        // Retrieve the selected path or use
        // an empty string if no path has
        // previously been selected
        String path = pref.get("DEFAULT_PATH", "");

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // set the path that was saved in preferences
        chooser.setCurrentDirectory(new File(path));

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            chooser.setCurrentDirectory(f);

            // Save the selected path
            pref.put("DEFAULT_PATH", f.getAbsolutePath());
            return f.getAbsolutePath();
        }
        return chooser.getSelectedFile().getAbsolutePath();
    }

    private void btnSequencingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSequencingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSequencingActionPerformed

    private void btnContinueCyc0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnContinueCyc0MouseClicked
        try {
            // TODO add your handling code here:
            // experiment.cyc0LastStep();

            List<Instruction> incorp0Instr = instr_set.getSectionInstructions("INCORP 0 END");
            experiment.initiate();
            for (Instruction ins : incorp0Instr) {
                if (ins.getName().equals("IMAGING")) {
                    experiment.showMessage("Please run MultiD acquisition manually", "Message");
                } else if (ins.isWaitUserInstruction()) {
                    break;
                } else {
                    experiment.runInstruction(ins);
                    updateTextArea(ins.toString());
                }

            }

        } catch (InterruptedException ex) {
            Logger.getLogger(WindowEventDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("There was an error.");
        }
    }//GEN-LAST:event_btnContinueCyc0MouseClicked

    private void jFormattedTextFieldOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextFieldOutputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextFieldOutputActionPerformed

    private void btnContinueCyc0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContinueCyc0ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnContinueCyc0ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new WindowEventDemo().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelNumOfCyc;
    private javax.swing.JButton btnContinueCyc0;
    private javax.swing.JButton btnCyc0;
    private javax.swing.JButton btnInjectBuffer;
    private javax.swing.JButton btnInvokeMM;
    private javax.swing.JButton btnSequencing;
    private javax.swing.JButton btnWash;
    private javax.swing.JFormattedTextField jFormattedTextFieldOutput;
    private javax.swing.JLabel jLabelOutput;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextArea jTextAreaOutput;
    // End of variables declaration//GEN-END:variables
}
