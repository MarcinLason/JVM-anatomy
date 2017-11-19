#!/bin/bash
cd Lab4

for i in $( ls ); do
 cd $i
 cd zadanie4
 ant &> ../compile.txt

 CLASSPATH=.
 cd lib

 for j in $( ls *.jar ); do
   export CLASSPATH=$CLASSPATH\;../lib/$j
 done

 cd ../classes
 java ProblemSixSolver First Second
 java ProblemSixSolver C A
 cd ..
done
