package cz.mail.model

/**
 * Created by Administrator on 2016/11/19.
 */
class AccountItem {
    def name
    def password

    AccountItem(name, password) {
        this.name = name
        this.password = password
    }
}
