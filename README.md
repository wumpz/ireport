# IReport

This is a fork of my favourite jasperreports designer IReport. This fork started with the latest sources of IReport 5.6. Unfortunately since 2015 I think the work on it was stopped 
in favourite of the now defacto standard JasperReports Studio. So it is a kind of taste which one you like. As I mentioned I prefer IReport. 

## Goals

**Reviving this designer is the main goal**

* make IReport a maven multi module project
* upgrade Netbeans Platform to Apache Netbeans V12.2 or higher.
* upgrade JasperReports to a more recent version, since this fork starts with version 5.6.
* refactor IReport to have a wrapper module. At the moment all third party jars are put directly into the main modules
* refactor IReport and split it into multiple smaller modules, this one large module technique its like a smell
  * remove extensive module openess, nearly all packages are exported
* begin to add tests, strangely it is quite untested via automation

## TODO

* remove legacy dependencies or upgrade it
  * jakarta-regexp
  * hsqldb
  * swing-layout (what is this for?)
  * jasperreports-htmlcomponent (https://stackoverflow.com/questions/42560528/how-to-add-the-htmlcomponent-to-jasperdesign) obsolete