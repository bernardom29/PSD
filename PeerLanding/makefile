JAVA_DEPS = dependencies/protobuf-java-3.6.1.jar:dependencies/jeromq-0.4.3.jar:bin:. 
BIN = bin/
ERLANG_ZMQ = dependencies/erlzmq2/include
ERLANG_GPB = dependencies/gpb/include/
JAVA_SRC = java_src/
SERIAL = $(JAVA_SRC)Protos/*.java
PROTO_DIR = java_src/Protos/protocolo.proto



all: DirInstall Compile

Compile: Client Exchange Directory Frontend

Protos: $(SERIAL)
	@protoc -I=. --java_out=$(JAVA_SRC) $(PROTO_DIR) 
	@javac -d $(BIN) \
		-classpath $(JAVA_DEPS) \
	 	$(JAVA_SRC)Protos/Protocolo.java 
	@echo Protos Generation and Compile

Client: Protos $(JAVA_SRC)Client/*.java
	@javac -d $(BIN) \
		-classpath $(JAVA_DEPS) \
		$(JAVA_SRC)Client/*.java 
	@echo Client Compiled


Exchange: Protos $(JAVA_SRC)Exchange/*.java
	@javac -d $(BIN) \
		-classpath $(JAVA_DEPS) \
		$(JAVA_SRC)Exchange/*.java 
	@echo Exchange Compiled
		

Directory: Protos $(JAVA_SRC)Directory/*.java
	@javac -d  $(BIN) \
		-classpath $(JAVA_DEPS) \
		$(JAVA_SRC)Directory/*.java 
	@echo Directory Compiled


Frontend: erl_src/*.erl 
	@dependencies/gpb/bin/protoc-erl -maps -o erl_src/ $(PROTO_DIR)
	@erlc -I $(ERLANG_GPB) -o $(BIN)/frontend erl_src/protocolo.erl 
	@erlc -I $(ERLANG_ZMQ) -o $(BIN)/frontend erl_src/erlzmq_nif.erl 
	@erlc -I $(ERLANG_ZMQ) -o $(BIN)/frontend erl_src/erlzmq.erl 
	@erlc -I $(ERLANG_ZMQ) -o $(BIN)/frontend erl_src/producer.erl 
	@erlc -I $(ERLANG_ZMQ) -o $(BIN)/frontend erl_src/consumer.erl
	@erlc -o $(BIN)/frontend/ erl_src/accountManager.erl erl_src/usersession.erl erl_src/frontend.erl
	@echo Erlang Components Compiled
	@echo Compilation done

run_client: Client
	java -cp bin Client.Client

run_frontend:
	erl -pa bin/frontend/ -run frontend 

clean:
	rm -rf bin/*
	

DirInstall:
	@mkdir -p $(BIN)
	@mkdir -p $(BIN)Exchange
	@mkdir -p $(BIN)frontend
	@mkdir -p $(BIN)Client
	@mkdir -p $(BIN)Directory
	@echo Creating dirs


