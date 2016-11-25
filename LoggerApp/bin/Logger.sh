#!/bin/bash

export CLASSPATH=/usr/bin
flag=true
folder=$(dirname $(realpath ../"$0"))

validateJava(){
	if ! type -P java > /dev/null 
	then 
		flag=false
		echo "Java is not installed"
	fi
}

validateJavaVersion(){
	version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
	
	if [[ "$version" < "1.7" ]] 
	then
		flag=false
		echo "Version of java must be at least 1.7"
	fi
}

validateJava;
if [ "$flag" == true ]; then validateJavaVersion; fi
