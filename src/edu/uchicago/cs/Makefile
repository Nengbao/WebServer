JC = javac
JFLAGS = -g

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

SRC = \
AbstractListener.java \
ArgsParser.java \
RedirectMap.java \
RegularListener.java \
Request.java \
RequestHandler.java \
Response.java \
Response200.java \
Response301.java \
ResponseError.java \
SecureListener.java \
Server.java \
StatusCodeMap.java

BIN: $(SRC:.java=.class)

all: BIN

run:
	java Server --serverPort=10000 --sslServerPort=10001

clean:
	$(RM) *.class
