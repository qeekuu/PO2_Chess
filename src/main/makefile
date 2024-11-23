SRC = src
BIN = bin

all:
	mkdir -p $(BIN)
	javac -d $(BIN) -sourcepath $(SRC) $(SRC)/main/Main.java #``$(SRC)/pieces/*.java

run:
	java -cp $(BIN) main.Main

clean:
	rm -rf $(BIN)

