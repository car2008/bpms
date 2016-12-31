// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.config.locations = []

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: [
        'text/html',
        'application/xhtml+xml'
    ],
    xml: [
        'text/xml',
        'application/xml'
    ],
    text: 'text/plain',
    js: 'text/javascript',
    rss: 'application/rss+xml',
    atom: 'application/atom+xml',
    css: 'text/css',
    csv: 'text/csv',
    all: '*/*',
    json: [
        'application/json',
        'text/json'
    ],
    form: 'application/x-www-form-urlencoded',
    multipartForm: 'multipart/form-data'
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = [
    '/images/*',
    '/css/*',
    '/js/*',
    '/plugins/*'
]


// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
grails.converters.json.default.deep = true  //最好是配合默认使用deep。
grails.converters.default.circular.reference.behaviour = "INSERT_NULL"//官方文档描述grails的converters类在转换JSON或XML时默认是输出所有环型引用的对象，所以这里要把它改成“INSERT_NULL”，就是如果碰到环型引用，就输出NULL
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    info   'grails.app.bootstrap'

    warn   'org.mortbay.log'
    
    debug  'com.capitelbiotech.bpms',
           'grails.app.jobs'
}

// Uncomment and edit the following lines to start using Grails encoding & escaping improvements

/* remove this line
 // GSP settings
 grails {
 views {
 gsp {
 encoding = 'UTF-8'
 htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
 codecs {
 expression = 'html' // escapes values inside null
 scriptlet = 'none' // escapes output from scriptlets in GSPs
 taglib = 'none' // escapes output from taglibs
 staticparts = 'none' // escapes output from static template parts
 }
 }
 // escapes all not-encoded output at final stage of outputting
 filteringCodecForContentType {
 //'text/html' = 'html'
 }
 }
 }
 remove this line */

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'com.capitalbiotech.bpms.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.capitalbiotech.bpms.UserRole'
grails.plugins.springsecurity.authority.className = 'com.capitalbiotech.bpms.Role'

//sendMail
grails.mail.default.from = "capitalbiotech_info@foxmail.com"
grails {
	mail {
		host = "smtp.qq.com"
		//host = "pop3.capitalbiotech.com"
		port=465
		username = "capitalbiotech_info@foxmail.com"
		//password = "bajd0816101041"
		password = "hgtsuflzsrpbcjbh"
		props = ["mail.smtp.auth":"true",
				"mail.smtp.socketFactory.port":"465",
				"mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
				"mail.smtp.socketFactory.fallback":"false"]
	}
}

// set per-environment serverURL stem for creating absolute links
environments {
    development {
        grails.logging.jul.usebridge = true
        grails.converters.default.pretty.print = true
        
        //bpms.static.directory.root = "/Users/wanglei/Development/Workspace/bpms/bpms/static"
        bpms.static.directory.root = "D:/WorkSpace/WorkSpaceGrails2.5.4/bpms/static"
        bpms.user.avatar.default = "${bpms.static.directory.root}/avatar_default.png"
        bpms.user.avatar.directory = "${bpms.static.directory.root}/avatars"
		
		example.dir.root = "C:/Users/czp/examples/example/static"
		example.dir.backup = "${example.dir.root}/backup"
		example.backup.cmd = "D:/chengzhipeng/soft-2/mysql-5.1/bin/mysqldump -uroot -proot bpms_dev"
		example.restore.cmd1 = "mysql -uroot -proot"
		example.restore.cmd2 = "use bpms_dev;"
		example.restore.cmd3 = "source "
		
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = "http://192.168.2.236:8080/bpms"
        
        bpms.static.directory.root = "/var/lib/tomcat/webapps/bpms/static"
        bpms.user.avatar.default = "${bpms.static.directory.root}/avatar_default.png"
        bpms.user.avatar.directory = "${bpms.static.directory.root}/avatars"
		
		example.dir.root = "C:/example/static"
		example.dir.backup = "${example.dir.root}/backup"
		example.backup.cmd = "C:/xampp/mysql/bin/mysqldump -uroot -proot bpms_dev"
		example.restore.cmd = "C:/xampp/mysql/bin/mysql -uroot -proot bpms_dev"
    }
}
