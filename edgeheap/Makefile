JDKPATH = /Library/Java/JavaVirtualMachines/jdk1.8.0_102.jdk/Contents/Home
LIBPATH = /p/course/cs784-raghu/minibase_share/javaminibase/construction
CLASSPATH = $(JDKPATH)/lib/classes.zip:.:..:$(LIBPATH)
BINPATH = $(JDKPATH)/bin
JAVAC = $(JDKPATH)/bin/javac -classpath $(CLASSPATH)
JAVA  = $(JDKPATH)/bin/java  -classpath $(CLASSPATH)

PROGS = together

all: $(PROGS)

ehf:EdgeHeapfile.java
	$(JAVAC) EdgeHeapfile.java

hp:EHFPage.java
	$(JAVAC) EHFPage.java

tuple:Tuple.java
	$(JAVAC) Tuple.java

edge:Edge.java
	$(JAVAC) Edge.java

escan:EScan.java
	$(JAVAC) EScan.java

together:*.java
	$(JAVAC) *.java

clean:
	\rm -f *.class *~ \#* core 
