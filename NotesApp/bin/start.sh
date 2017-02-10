#!/bin/bash

folder=$(dirname $(readlink -f  "$0"))

/usr/bin/java -cp $folder/../lib/notes.jar com.laetienda.notes.bin.Controller -start > /dev/null
