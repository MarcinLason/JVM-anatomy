#!/bin/bash
 cd ..
 ant &> ../compile_zad_1_2.txt
 CLASSPATH=.;
 cd lib

 for j in $( ls *.jar ); do
   export CLASSPATH=$CLASSPATH\;../lib/$j
 done
 
 cd ../classes
 echo $CLASSPATH
 javac Test.java
 java Transform Test.class &> Test_transform_zad_1_2_out.txt
 java Test &> ../../Test_zad_1_2_out.txt
