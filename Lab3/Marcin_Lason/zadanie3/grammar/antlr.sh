#!/usr/bin/env bash
java -Xmx1024M -cp ".:../lib/*:$CLASSPATH" org.antlr.v4.Tool $1 -o ../src/npj/generated -package npj.generated