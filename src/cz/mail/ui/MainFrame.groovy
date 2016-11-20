package cz.mail.ui

import cz.mail.Constants
import cz.mail.util.FilePrefs
import cz.mail.work.HtmlWorker
import groovy.swing.SwingBuilder
import org.fusesource.jansi.Ansi

import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.WindowConstants
import javax.swing.event.MenuKeyListener
import javax.swing.event.MenuListener
import java.awt.BorderLayout
import java.awt.Color
import java.util.concurrent.Executors

/**
 * Created by czz on 2016/11/19.
 */
class MainFrame extends JFrame{
    final def executorService = Executors.newFixedThreadPool(10);
    def startButton,stopButton
    def messageTextArea
    def frame

    MainFrame() {
        def swingBuilder=new SwingBuilder()
        swingBuilder.edt {
            frame=swingBuilder.frame(title: 'Send Email', size: [800, 600], visible:true,locationRelativeTo:null,background: Color.WHITE, defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE) {
                menuBar {
                    menu(id:'template', text: 'Template'){
                        menuItem(text: "Add Template",actionPerformed: {new EmailPanel()})
                    }
                    menu(text: 'File'){
                        menuItem(text: "Clean cache",actionPerformed: {println "click item"})
                    }
                    menu(id:"history",text: 'History'){
                        menuItem(text: "Clean cache",actionPerformed: {println "click item"})
                    }
                }
                panel(layout:new BorderLayout(0,10)){
                    swingBuilder.scrollPane(constraints:BorderLayout.CENTER,border: BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(20,10,0,10),"message")){
                        messageTextArea=textArea(rows: 6,columns: 20)
                    }
                    swingBuilder.panel(constraints:BorderLayout.SOUTH,layout: new BorderLayout()){
                        swingBuilder.panel(constraints:BorderLayout.EAST){
                            button(text:"Clean",actionPerformed: {messageTextArea.setText(null)})
                            button(text:"Export Excel")
                            stopButton=button(text:"Stop",enabled: false)
                            startButton=button(text:"Start",enabled: true, actionPerformed: {startAction("http://www.qiushibaike.com/article/5100599")})
                        }
                    }
                }
            }
//            def fillMenu = { ->
//                history.removeAll()
//                new File('C:\\Users\\Administrator\\Desktop\\pdf').listFiles().each {
//                    history.add(menuItem(text: it.name))
//                }
//                new EmailPanel()
//            }
//            history.addMenuListener([ menuCanceled: { e -> }, menuDeselected: { e -> }, menuSelected: { e -> fillMenu() } ] as MenuListener)
        }
    }

    def startAction(requestUrl){
        stopButton.enabled=true
        startButton.enabled=false
        executorService.execute(new HtmlWorker(requestUrl))
    }
}
