plugins {
    base
    java
}

base.archivesBaseName = "back-client"

val serverCompileOnly by configurations
val compileOnly: Configuration by configurations
compileOnly.extendsFrom(serverCompileOnly)

dependencies {
    compile(project(":back:back-api"))
//    compile ("org.jboss:jboss-remote-naming:2.0.4.Final")
//    compile("org.wildfly:wildfly-naming-client:1.0.9.Final")
//    compile("org.wildfly:wildfly-naming:10.1.0.Final")
//    compile("org.jboss:jboss-ejb-client:2.1.4.Final")
//    compile("org.jboss.logging:jboss-logging:3.3.0.Final")
    compile("org.jboss.spec.javax.ejb:jboss-ejb-api_3.2_spec:1.0.0.Final")
    compile("org.jboss:jboss-remote-naming:2.0.4.Final")
    compile("org.jboss:jboss-ejb-client:2.1.4.Final")
    compile("org.jboss.xnio:xnio-nio:3.3.4.Final")
    compile("org.jboss.marshalling:jboss-marshalling-river:1.4.10.Final")
    compile("org.jboss.spec.javax.transaction:jboss-transaction-api_1.2_spec:1.0.0.Final")
}


