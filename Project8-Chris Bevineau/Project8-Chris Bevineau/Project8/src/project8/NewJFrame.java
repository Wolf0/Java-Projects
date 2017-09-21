/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package project8;

import java.util.*;
import java.text.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

/**
 *
 * @author P
 */
public class NewJFrame extends javax.swing.JFrame {
     HashMap<String, Integer> priceList;
    List<String> cb1Array = new ArrayList<>();// = new String[]{"P4 2.2 GHz", "P4 2.4 GHz", "P4 2.6 GHz"};
    List<String> cb2Array = new ArrayList<>();// = new String[]{"256 MB","512 MB","1 GB","2 GB"};
    List<String> cb3Array = new ArrayList<>();// = new String[]{"80 GB","120 GB","170 GB"};
    List<String> chbArray = new ArrayList<>();// = new String[]{"Office package","Accounting package","Graphics package"};
    List<String> rbArray = new ArrayList<>(); // = new String[]{"Windows 7 Professional","Windows 7 Ultimate"};
   
    private final String caption = "Price: ";
    private final String fileName = "PriceList.txt";
    
    int comboBoxPreviousItem=0;
    JRadioButton deselectedRadioButton = null;
    boolean isComboBoxDeselected = false;
    boolean isRadioButtonDeselected = false;
    ComboBoxItemListener cbItemListener = new ComboBoxItemListener();
    ComponentActionListener cActionListener = new ComponentActionListener();
    RadioButtonChangeListener rbChangeListener = new RadioButtonChangeListener();
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
         this.priceList = new HashMap<>();
        initComponents();
      
        nf.setMinimumFractionDigits(2);
        loadPriceListTable();
        loadComponents();
        
    }
    private void loadPriceListTable()
    {
        if (ReadFile.isFileReal(fileName))
        {
            List<String> list = new ArrayList<>();
            ReadFile file = new ReadFile(fileName);
            file.getFileContent();
            for (String line : file.fileContent)
            {
                if (file.isLineCategory(line))
                {
                    switch (file.getCategory(line))
                    {
                        case "Processors":
                            list = cb1Array;
                            break;
                        case "Memory":
                            list = cb2Array;
                            break;
                        case "Disk":
                            list = cb3Array;
                            break;
                        case "Software":
                            list = chbArray;
                            break;
                        case "OS":
                            list = rbArray;
                            break;
                    }
                }
                else
                {
                    priceList.put(file.getItem(line), file.getPrice(line));
                    list.add(file.getItem(line));
                }
            }
        }
    }
    private void loadComponents()
    {
        for (String s : cb1Array)
            jComboBox1.addItem(s);
        for (String s : cb2Array)
            jComboBox2.addItem(s);
        for (String s : cb3Array)
            jComboBox3.addItem(s);
        jRadioButton1.setText(rbArray.get(0));
        jRadioButton1.setName(rbArray.get(0));
        jRadioButton2.setText(rbArray.get(1));
        jRadioButton2.setName(rbArray.get(1));
        jCheckBox1.setText(chbArray.get(0)+" (add $"+priceList.get(chbArray.get(0))+")");
        jCheckBox1.setName(chbArray.get(0));
        jCheckBox2.setText(chbArray.get(1)+" (add $"+priceList.get(chbArray.get(1))+")");
        jCheckBox2.setName(chbArray.get(1));
        jCheckBox3.setText(chbArray.get(2)+" (add $"+priceList.get(chbArray.get(2))+")");
        jCheckBox3.setName(chbArray.get(2));
        
        jComboBox1.addItemListener(cbItemListener);
        jComboBox1.addActionListener(cActionListener);
        jComboBox2.addItemListener(cbItemListener);
        jComboBox2.addActionListener(cActionListener);
        jComboBox3.addItemListener(cbItemListener);
        jComboBox3.addActionListener(cActionListener);
        jRadioButton1.addActionListener(cActionListener);
        jRadioButton1.addChangeListener(rbChangeListener);
        jRadioButton2.addActionListener(cActionListener);
        jRadioButton2.addChangeListener(rbChangeListener);
        jCheckBox1.addActionListener(cActionListener);
        jCheckBox2.addActionListener(cActionListener);
        jCheckBox3.addActionListener(cActionListener);

    }
    private class ComboBoxItemListener implements ItemListener{
        @Override
        public void itemStateChanged(ItemEvent itemEvent){
            if (itemEvent.getStateChange()==ItemEvent.DESELECTED)
            {
                JComboBox cb =(JComboBox)itemEvent.getSource();
                DefaultComboBoxModel dcbm = (DefaultComboBoxModel)cb.getModel();
                comboBoxPreviousItem = dcbm.getIndexOf(itemEvent.getItem().toString());
                isComboBoxDeselected = true;
            }
        }
    }
    private class RadioButtonChangeListener implements ChangeListener{
       @Override
       public void stateChanged(ChangeEvent evt)
       {
           JRadioButton radioButton = (JRadioButton)evt.getSource();
           if (!radioButton.isSelected())
               deselectedRadioButton = radioButton;
       }
    }
    
    private class ComponentActionListener implements ActionListener{
        
        @Override
        public void actionPerformed(ActionEvent evt)
        {
            if (evt.getSource() instanceof JComboBox)
            {
                comboBoxAction(evt);
            }
            else if (evt.getSource() instanceof JRadioButton)
            {
                radioButtonAction(evt);
            }
            else if (evt.getSource() instanceof JCheckBox)
            {
                checkBoxAction(evt);
            }
        }
    }
    private void radioButtonAction(ActionEvent evt)
     {
        JRadioButton radioButton = (JRadioButton)evt.getSource();
        double price = getPrice(jLabelPrice.getText());
        String text="";
        int difference = priceList.get(radioButton.getName())-priceList.get(deselectedRadioButton.getName());
        if (radioButton.isSelected())
        {
            radioButton.setText(radioButton.getName());
            if (difference<0)
                text= " (add $"+Math.abs(difference)+")";
            else 
                text = " (subtract $"+Math.abs(difference)+")";
            deselectedRadioButton.setText(deselectedRadioButton.getName()+text);
            price+=difference;
            jLabelPrice.setText(caption+nf.format(price));
        }
    }

    private void checkBoxAction(ActionEvent evt)
    {
        JCheckBox checkBox = (JCheckBox)evt.getSource();
         double price = getPrice(jLabelPrice.getText());
        if (checkBox.isSelected())
        {
            price+=priceList.get(checkBox.getName());
            checkBox.setText(checkBox.getName()+" (subtract $"+priceList.get(checkBox.getName())+")");
        }
        else
        {
            price-=priceList.get(checkBox.getName());
            checkBox.setText(checkBox.getName()+" (add $"+priceList.get(checkBox.getName())+")");
        }
        jLabelPrice.setText(caption+nf.format(price));
    
    }
    
    private void comboBoxAction(java.awt.event.ActionEvent evt)
    {
        if (isComboBoxDeselected)
        {
            JComboBox comboBox = (JComboBox)evt.getSource();
            List<String> cbItems = getCurrentComboBoxArray(comboBox);
            double price = getPrice(jLabelPrice.getText());

            int currentItem = comboBox.getSelectedIndex();
            price-=priceList.get(cbItems.get(comboBoxPreviousItem));
            price+=priceList.get(cbItems.get(currentItem));
            jLabelPrice.setText(caption+nf.format(price));
            isComboBoxDeselected = false;
            enhanceComboBox(comboBox);
        }

    }

    private List<String> getCurrentComboBoxArray(JComboBox cb)
    {
        List<String> array = null;
        switch (cb.getName())
        {
            case "Processor":
                array = cb1Array;
                break;
            case "Memory":
                array = cb2Array;
                break;
            case "Disk":
                array = cb3Array;
                break;
        }
        return array;
    }
    
   private double getPrice(String text)
    {
        double price = 0;
        try{
            price = nf.parse(jLabelPrice.getText().replace(caption, "")).doubleValue();
        } catch (ParseException pe){}
        return price;
    }
private void enhanceComboBox(JComboBox cb)
    {
        List<String> cbItems = getCurrentComboBoxArray(cb);
        cb.removeItemListener(cbItemListener);//removes ItemListener so it doesnt trigger an event
        int selectedItem = cb.getSelectedIndex();
        
        cb.removeAllItems();
        for (int i = 0; i<cbItems.size(); i++)
        {
            cb.addItem(cbItems.get(i)+getEnhancement(cbItems, selectedItem, i));
        }
        cb.setSelectedIndex(selectedItem);
 //       cb.invalidate();
        cb.addItemListener(cbItemListener);
    }
    
    private String getEnhancement(List<String> items, int selected, int updated)
    {
        String text = "";//covers selected==updated
        int difference = Math.abs(priceList.get(items.get(updated))-priceList.get(items.get(selected)));
        if (selected<updated)
            text+="(add $"+Integer.toString(difference)+")";
        else if (selected>updated)
            text+=" (subtract $"+Integer.toString(difference)+")";
        return text;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jButtonExit = new javax.swing.JButton();
        jLabelPrice = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Computer");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Hardware"));

        jComboBox1.setName("Processor"); // NOI18N

        jComboBox2.setName("Memory"); // NOI18N

        jComboBox3.setName("Disk"); // NOI18N

        jLabel1.setText("Processor");

        jLabel2.setText("Memory");

        jLabel3.setText("Disk");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox3, 0, 189, Short.MAX_VALUE)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Software"));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setName("Windows 7 Professional"); // NOI18N

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setName("Windows 7 Ultimate"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator1)
                    .addComponent(jCheckBox3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(211, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButtonExit.setText("Exit");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        jLabelPrice.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelPrice.setText("Price: $500");

        jButton1.setText("Calculate");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 134, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(58, 58, 58)
                .addComponent(jButtonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(152, 152, 152))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabelPrice))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonExit)
                    .addComponent(jButton1))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButtonExitActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
     final JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        
        
JOptionPane.showMessageDialog(frame,
        "Price is " + jLabelPrice,
    "Price of Computer Configuration is " ,
    JOptionPane.INFORMATION_MESSAGE);
        
    }//GEN-LAST:event_jButton1ActionPerformed
                
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelPrice;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
