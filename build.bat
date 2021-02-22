SET CLASSPATH=.;babycino\antlr-4.7.2-complete.jar;%CLASSPATH%
CALL java -jar antlr-4.7.2-complete.jar -visitor -package babycino babycino/MiniJava.g4
CALL javac babycino/*.java