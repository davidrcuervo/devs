#!/bin/bash

folder=$(dirname $(readlink -f  "$0"))

/usr/bin/java -cp $folder/../lib/web.jar com.laetienda.web.bin.Run -stop &>> $folder/../var/log/catalina.out > /dev/null