JDKPATH = /Library/Java/JavaVirtualMachines/jdk1.8.0_102.jdk/Contents/Home
LIBPATH = /p/course/cs784-raghu/minibase_share/javaminibase/construction
CLASSPATH = $(JDKPATH)/lib/classes.zip:.:..:$(LIBPATH)
BINPATH = $(JDKPATH)/bin
JAVAC = $(JDKPATH)/bin/javac -classpath $(CLASSPATH)
JAVA  = $(JDKPATH)/bin/java  -classpath $(CLASSPATH)

PROGS = together

all: $(PROGS)

nhf:NodeHeapfile.java
	$(JAVAC) NodeHeapfile.java

nd:Node.java
	$(JAVAC) Node.java

nhp:NHFPage.java
	$(JAVAC) NHFPage.java

nscan:NScan.java
	$(JAVAC) NScan.java

together:*.java
	$(JAVAC) *.java

clean:
	\rm -f *.class *~ \#* core 
