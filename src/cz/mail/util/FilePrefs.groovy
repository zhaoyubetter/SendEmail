package cz.mail.util

import java.text.SimpleDateFormat

/**
 * Created by czz on 2016/11/20.
 */
class FilePrefs {

    static def getConfigFolder(){
        def userPath= System.getProperty("user.home")
        def osName=System.getProperty("os.name")
        def configFile=null
        if(osName.toLowerCase().contains("windows")){
            configFile=new File(userPath,"\\AppData\\Local\\Email")
        } else if(osName.toLowerCase()?.contains("mac")){
            configFile=new File(userPath,"/Email")
        }
        configFile?.mkdirs()
        configFile
    }
    static def getConfigFile(){
        def configFile=getConfigFolder()
        new File(configFile, "account.xml")
    }

    static def getCacheFile(){
        def configFile=getConfigFolder()
        def cacheFile=new File(configFile,"/cache/")
        cacheFile?.mkdirs()
        cacheFile
    }

    static def getTodayCacheFile(){
        def formatter=new SimpleDateFormat("yyyy-MM-dd")
        new File(FilePrefs.cacheFile,formatter.format(new Date())+".txt")
    }

    static def readAccountItems(){
        def accountItems=[:]
        def accountFile=getConfigFile()
        if(accountFile.exists()){
            def element=new XmlParser().parse(accountFile)
            element.item.each{ accountItems<<[(it.attribute("name")):(it.attribute("password"))] }
        }
        accountItems
    }
}
