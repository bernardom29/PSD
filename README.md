# TrustPet

Distributed Systems Paradigms (2018/2019), Informatics Engineering, Universidade do Minho, Portugal

Auction intermediary service with components developed with various distributed systems paradigms. The client sends a request to the front-end server (Erlang) which forwards it to the exchange servers (Java). The client can consult the directory (RESTful Service) for information on the companies and is notified about auction they've shown interest in (ZeroMQ).

##Technologies Used
- Java
- Erlang
- ZeroMQ
- Protocol Buffers
- Atomix