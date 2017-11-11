#!/usr/bin/env bash
cd Lab3
cd Marcin_Lason
cd zadanie3
cd src
cd npj

java -Xmx1024M -cp "../../lib/antlr-4.5.3-complete.jar" org.antlr.v4.Tool NPJ.g4 -o ./generated -package npj.generated

