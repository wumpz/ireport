# IReport

This is a fork of my favourite jasperreports designer IReport. This fork started with the latest sources of IReport 5.6. Unfortunately since 2015 I think the work on it was stopped 
in favourite of the now defacto standard JasperReports Studio. So it is a kind of taste which one you like. As I mentioned I prefer IReport. 

If you want to participate, please contact me. 

If I have time I will do more.

## News

* first "release" of Netbeans 16 based iReports (https://github.com/wumpz/ireport/releases/tag/nb-platform-16)
* https://github.com/wumpz/ireport/issues/3  It starts for the first time on Java 8 and Apache Netbeans 12.2 :)


## Goals

**Reviving this designer is the main goal**

* **(done)** make IReport a maven multi module project
* **(done)** make IReport Java 8 compilable and startable
* **(done)** upgrade Netbeans Platform to Apache Netbeans V12.2 or higher.
* upgrade JasperReports to a more recent version, since this fork starts with version 5.6.
  * avoid to immediate implement all new features of new JasperReport versions
* refactor IReport to have a wrapper module. At the moment all third party jars are put directly into the main modules
* refactor IReport and split it into multiple smaller modules, this one large module technique its like a smell
  * remove extensive module openess, nearly all packages are exported
* begin to add tests, strangely it is quite untested via automation
* performance, performance, performance
* maybe removal of unneeded plugin (is hadoop hive required, mongodb?)

## TODO

* remove legacy dependencies or upgrade it
  * jakarta-regexp
  * hsqldb
  * swing-layout (what is this for?)
  * jasperreports-htmlcomponent (https://stackoverflow.com/questions/42560528/how-to-add-the-htmlcomponent-to-jasperdesign) obsolete
  * look into into local maven repository imported and needed libraries
  * replace the jars below by something available at maven central

## Preparation

Some of the used libraries are not available from maven central. Therefore in the **library.zip** are all missing libraries. You have to install those into your local maven repository. Use this maven plugin calls to achieve this. Extract this zip file and put the directory in the following maven plugin calls.

```
org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=library\jasperreports-chart-themes-5.6.0.jar

org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=library\sqleonardo-2009.03.rc1.jar -DgroupId=nickyb -DartifactId=sqleonardo -Dversion=2009.03.rc1 -Dpackaging=jar

org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=library\mondrian-3.2.0-13661-JS.jar -DgroupId=mondrian -DartifactId=mondrian -Dversion=3.2.0-13661-JS-3 -Dpackaging=jar

org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=library\rex-0.8.1.jar -DgroupId=rex -DartifactId=rex -Dversion=0.8.1 -Dpackaging=jar

org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=library\js_jasperserver-common-ws-4.7.1.jar

org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=library\js-hive-datasource-1.0.4.jar

org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=library\jasperreports-htmlcomponent-5.0.1.jar -DgroupId=net.sf.jasperreports -DartifactId=jasperreports-htmlcomponent -Dversion=5.0.1 -Dpackaging=jar

org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=library\jasperreports-extensions-3.5.3.jar -DgroupId=net.sf.jasperreports -DartifactId=jasperreports-extensions -Dversion=3.5.3 -Dpackaging=jar
```

I downgraded beanshell bsh to version 2.0b5 since 2.1b5 is not at maven anymore.
