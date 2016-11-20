package cz.mail.ui

import cz.mail.Constants
import cz.mail.util.FilePrefs
import groovy.swing.SwingBuilder
import groovy.xml.MarkupBuilder
import groovy.xml.QName

import javax.swing.BorderFactory
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.WindowConstants
import java.awt.Dimension
import java.awt.GridBagLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

import static java.awt.GridBagConstraints.HORIZONTAL

/**
 * Created by Administrator on 2016/11/19.
 */
class AddEmailAccountFrame {
    def final WIDTH=400;
    def final HEIGHT=240;
    def final MAX_WIDTH=500;
    def final MAX_HEIGHT=340;
    def frame=null
    def swingBuilder=null

    AddEmailAccountFrame() {
        def defaultInserts=[10,10,10,0]
        swingBuilder=new SwingBuilder()
        swingBuilder.panel().setPreferredSize(new Dimension(80,WIDTH))
        def emailField=null
        def passwordTextField=null
        def addAccountAction={
            def matcher=emailField.text=~Constants.REGEX_EMAIL;
            if(!emailField.text){
                swingBuilder.optionPane(message:"Email address empty!",messageType:JOptionPane.WARNING_MESSAGE).createDialog("Warning!").setVisible(true)
            } else if(!matcher){
                swingBuilder.optionPane(message:"Not an email address!",messageType:JOptionPane.WARNING_MESSAGE).createDialog("Warning!").setVisible(true)
            } else if(!passwordTextField.text){
                swingBuilder.optionPane(message:"Password is empty!",messageType:JOptionPane.WARNING_MESSAGE).createDialog("Warning!").setVisible(true)
            } else if(6>passwordTextField.text.length()){
                swingBuilder.optionPane(message:"Password is to short!",messageType:JOptionPane.WARNING_MESSAGE).createDialog("Warning!").setVisible(true)
            } else {
                updateAccount(emailField.text,passwordTextField.text)
            }
        }
        def cancelAction={ frame.setVisible(false)}
        frame=swingBuilder.frame(title: "Add Account", size: [WIDTH,HEIGHT],
                minimumSize: [WIDTH,HEIGHT],
                maximumSize: [MAX_WIDTH,MAX_HEIGHT],
                locationRelativeTo:null,
                visible: true){
            swingBuilder.panel(layout:new GridBagLayout(),border:BorderFactory.createEmptyBorder(10,10,10,10)){
                label(text:"Input Account:",horizontalAlignment: JLabel.LEFT,constraints: gbc(gridx: 0,gridy: 0,insets: defaultInserts,fill:HORIZONTAL))
                emailField=textField(toolTipText: "Email account name",constraints:gbc(gridx: 1,gridy: 0,insets: defaultInserts,fill:HORIZONTAL))
                label(text:"Input Password:",horizontalAlignment: JLabel.LEFT,constraints:gbc(gridx: 0,gridy: 1,insets: defaultInserts,fill:HORIZONTAL))
                passwordTextField=passwordField(toolTipText: "Email account password",columns: 16,constraints:gbc(gridx: 1,gridy: 1,insets: defaultInserts,fill:HORIZONTAL))
                swingBuilder.panel(constraints:gbc(gridx: 0, gridy: 2,gridwidth: 3, insets: [20,20,20,20],fill:HORIZONTAL)){
                    button(text:"Cancel",actionPerformed: cancelAction)
                    button(text:"Add Account",actionPerformed: addAccountAction)
                }
            }
        }
        frame.setVisible(true)
    }

    /**
     * update the account config
     * @param name
     * @param password
     */
    def updateAccount(accountName,password){
        def accountFile=FilePrefs.getConfigFile()
        if(accountFile){
            def accountItems=FilePrefs.readAccountItems()
            accountItems.put(accountName,password)
            writeAccountFile(accountFile,accountItems)
        }
    }

    /**
     * write all account to file
     * @param file
     */
    def writeAccountFile(file,accountItems){
        def out = new FileWriter(file)
        def document = new MarkupBuilder(out)
        document.account{
            accountItems.each{ item("name":it.key,"password":it.value) }
        }
        JDialog dialog=swingBuilder.optionPane(message:"Add email account complete!",messageType:JOptionPane.CANCEL_OPTION).createDialog("Complete!")
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            void windowDeactivated(WindowEvent e) {
                super.windowDeactivated(e)
                frame.setVisible(false)
            }
        })
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        dialog.setVisible(true)
    }
}
