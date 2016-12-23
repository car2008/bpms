grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
		mavenRepo "http://repo.grails.org/grails/plugins" //http://repo.grails.org/grails/core改为http://repo.grails.org/grails/plugins就可以用了
        // uncomment these to enable remote dependency resolution from public Maven repositories
        //mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
		compile "org.grails.plugins:quartz:1.0.2"
        runtime 'mysql:mysql-connector-java:5.1.20'
		compile "org.grails.plugins:mail:1.0.7"
    }

    plugins {
        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.4"
        //compile ":spring-security-ui:1.0-RC2"

        compile ":hibernate:3.6.10.16"
        compile ":webxml:1.4.1"
        compile ":spring-security-core:1.2.7.4"
        //compile ':quartz:1.0.1'
        compile ":executor:0.3"
        //compile ":mongodb:3.0.2"
        //compile ":mongodb-create-drop:1.0.1"
        
        build ":tomcat:7.0.54"
        
        runtime ":resources:1.1.6"
        
    }
}
