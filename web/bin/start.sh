#!/bin/bash

folder=$(dirname $(readlink -f  "$0"))

/usr/bin/java -cp $folder/../lib/cafeteros.jar com.laetienda.web.bin.Run -start &>> $folder/../var/log/catalina.out 
