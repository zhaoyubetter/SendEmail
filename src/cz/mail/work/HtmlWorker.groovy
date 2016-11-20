package cz.mail.work

import cz.mail.Constants
import cz.mail.util.FilePrefs

/**
 * Created by Administrator on 2016/11/20.
 */
@Grab(group='org.ccil.cowan.tagsoup', module='tagsoup', version='1.2.1')
class HtmlWorker implements Runnable{
    def static final int MAX_REQUEST_SIZE=10
    def requestUrlItems=[]//请求列,防止请求回路(如a->b->c->b->c->b->c)
    def final url

    HtmlWorker(url) {
        this.url = url
    }

    @Override
    void run() {
        startRequestUrl(url)
    }

    def startRequestUrl(requestUrl){
        def (urlItems,emailItems)=readHtmlInfo(requestUrl)
        FilePrefs.todayCacheFile.withWriterAppend { writer->
            writer.write("email-size:$emailItems.size $requestUrl\n")
            if(emailItems){
                emailItems.each{writer.write(it+"\n")}
            }
        }
        urlItems.each {
            if(checkRequestUrl(it)) {
//                startRequestUrl(it)
            }
        }
        println "email-size:$emailItems.size url-size:$urlItems.size \nurl:$requestUrl"
        urlItems.each {println it?:""}

        println "all email"
        emailItems.each {println it?:""}
    }
    /**
     * 确定请求链在10个以内,是否重复,避免循环请求
     * @param requestUrl
     * @return
     */
    def checkRequestUrl(requestUrl) {
        requestUrlItems << requestUrl
        if (MAX_REQUEST_SIZE < requestUrlItems.size()) {
            requestUrlItems -= requestUrlItems[0]
        }
        def originalSize = requestUrlItems.size()
        requestUrlItems.removeIf({ 1<requestUrlItems.count(it)})
        originalSize == requestUrlItems.size()
    }

    def getHtml(requestUrl) {
        def serverUrl=null
        def html=null
        try{
            def url = new URL(requestUrl);
            def matcher = requestUrl=~/https?:\/\/([\w\.]+)/
            serverUrl=matcher?matcher.group():null
            def connection = url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            connection.connect();
            def parser = new XmlParser(new org.ccil.cowan.tagsoup.Parser())
            html=parser.parse(connection.inputStream)
        } catch (e){
            println "Request Failed:$requestUrl"
        }
        [serverUrl?:[],html]
    }

    def printNodeValue(node,set){
        if(node instanceof Node){
            if(node.value() instanceof NodeList){
                node.value().each{ printNodeValue(it,set) }
            } else {
                println node.value
            }
        } else if(node){
            set.add(node)
        }
    }

    def readHtmlInfo(requestUrl){
        def emailItems=[]
        def urlItems=new HashSet()
        def (serverUrl,html)=getHtml(requestUrl)
        if(null!=html){
            //html.body.'**'.a.@href.grep(~/.*\.html/).each{ println it }
            def divItemValue=new HashSet()
            def divItems=html.body.'**'.div;
            if(divItems){
                divItems.each{
                    printNodeValue(it,divItemValue)
                }
            }
            divItemValue.each{
                //正则来自android-Patterns.EMAIL_ADDRESS,官方的靠谱,找了几个都匹配不全
                def matcher=it=~Constants.REGEX_EMAIL
                if(matcher){
                    emailItems<<matcher.group()
                }
            }
            html.body.'**'.a.each{
                String href= it.attribute("href")
                if(href?.startsWith("http")){
                    urlItems<<href
                } else if(serverUrl&&href?.startsWith("/")){
                    urlItems<<serverUrl+href
                }
            }
        }
        [urlItems as List,emailItems]
    }
}
