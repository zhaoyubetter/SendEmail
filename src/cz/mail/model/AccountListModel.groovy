package cz.mail.model

import javax.swing.DefaultListModel

/**
 * Created by Administrator on 2016/11/19.
 */
class AccountListModel extends DefaultListModel{
    AccountListModel(list) {
        super()
        list?.each{addElement(it)}
    }
}
