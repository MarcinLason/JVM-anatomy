#!/bin/bash
cd Lab3

for i in $( ls ); do
 cd $i
 cd zadanie3
 ant &> ../compile.txt

 CLASSPATH=.
 cd lib

 for j in $( ls *.jar ); do
   export CLASSPATH=$CLASSPATH\;../lib/$j
 done

 cd ../classes
 java -Dnpj.heap.size=1024 Interpreter test1.npj &> test1_out.txt
 java -Dnpj.heap.size=1024 Interpreter test2.npj &> test2_out.txt
 java -Dnpj.heap.size=1024 Interpreter test3.npj &> test3_out.txt
 cd ..
done
