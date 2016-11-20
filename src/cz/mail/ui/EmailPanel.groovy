package cz.mail.ui

import cz.mail.model.AccountListModel
import cz.mail.util.FilePrefs
import groovy.swing.SwingBuilder

import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.ListSelectionModel
import java.awt.BorderLayout
import java.awt.GridLayout

/**
 * Created by Administrator on 2016/11/19.
 */
class EmailPanel extends JFrame{
    def final WIDTH=600;
    def final HEIGHT=500;

    def saveAccountAction={
        //TODO something to do
    }

    EmailPanel() {
        def swingBuilder=new SwingBuilder()
        def frame=null
        def accountItems=FilePrefs.readAccountItems()
        def accountNameItems=new TreeSet()
        accountItems.each{ accountNameItems<<it.key}
        def detailModel=new AccountListModel(accountNameItems)
        def addNewAction={new AddEmailAccountFrame()}
        def cancelAction={ frame.setVisible(false)}
        frame=swingBuilder.frame(title: "Edit Email Template", layout: new BorderLayout(),minimumSize: [600,500],locationRelativeTo:null){
            swingBuilder.panel(layout: new BorderLayout(12, 10), border: BorderFactory.createEmptyBorder(0, 10, 10, 10)) {
                swingBuilder.panel(layout: new BorderLayout(), constraints: BorderLayout.WEST, preferredSize: [160, HEIGHT]) {
                    label(text: "Account", constraints: BorderLayout.NORTH, preferredSize: [160, 24])
                    swingBuilder.panel(layout: new GridLayout(1,1), constraints: BorderLayout.CENTER) {
                        scrollPane() { list(model : detailModel, selectionMode: ListSelectionModel.SINGLE_SELECTION, selectedIndex: 0) }
                    }
                }
                swingBuilder.panel(layout: new BorderLayout(), constraints: BorderLayout.CENTER) {
                    swingBuilder.panel(layout: new BorderLayout(), constraints: BorderLayout.NORTH) {
                        swingBuilder.panel(layout: new BorderLayout(), constraints: BorderLayout.NORTH, preferredSize: [WIDTH, 24]) {
                            label(text: "Subject:", horizontalAlignment: JLabel.LEFT, preferredSize: [80, 24])
                        }
                        swingBuilder.panel(layout: new BorderLayout(), constraints: BorderLayout.CENTER, preferredSize: [WIDTH, 24]) {
                            textField(preferredSize: [20, 200])
                        }
                    }
                    swingBuilder.panel(layout: new BorderLayout(), constraints: BorderLayout.CENTER) {
                        swingBuilder.panel(layout: new BorderLayout(), constraints: BorderLayout.NORTH, preferredSize: [80, 24]) {
                            label(text: "Content:", horizontalAlignment: JLabel.LEFT)
                        }
                        swingBuilder.panel(layout: new BorderLayout(), constraints: BorderLayout.CENTER) {
                            scrollPane() { textArea(toolTipText: "Email account password", columns: 16, rows: 4) }
                        }
                    }
                }
                swingBuilder.panel(layout: new BorderLayout(), constraints: BorderLayout.SOUTH) {
                    swingBuilder.panel(constraints: BorderLayout.EAST) {
                        button(text: "Cancel", actionPerformed: cancelAction)
                        button(text: "Add New Account", actionPerformed: addNewAction)
                        button(text: "Save", actionPerformed: saveAccountAction)
                    }
                }
            }
        }
        frame.setVisible(true)
    }
}
