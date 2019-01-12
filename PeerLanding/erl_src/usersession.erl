%%%-------------------------------------------------------------------
%%% @author trident
%%% @copyright (C) 2018, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 27. Dec 2018 22:40
%%%-------------------------------------------------------------------
-module(usersession).
-author("trident").

%% API
-export([usersession/2, usersession/3]).


usersession(Sock, Empresas) ->
  receive
    {tcp, Sock, Packet} ->
      AuthReq = protocolo:decode_msg(Packet, 'AuthReq'),
      Username = maps:get(username, AuthReq),
      Tipo = accountManager:login(Username, maps:get(password, AuthReq)),
      case Tipo of
        invalid ->
          io:format("UserSession~p: login insucesso\n",[self()]),
          OutPacket = protocolo:encode_msg(#{sucesso => false, tipo => 0 },'AuthRep'),
          gen_tcp:send(Sock,OutPacket),
          usersession(Sock,Empresas);
        _ ->
          io:format("UserSession~p: login sucesso\n",[self()]),
          OutPacket = protocolo:encode_msg(#{sucesso => true, tipo=>Tipo},'AuthRep'),
          gen_tcp:send(Sock, OutPacket),
          usersession(Sock,Username,Empresas)
      end;
    _ ->
      io:format("UserSession~p: erro",[self()])
  end.

usersession(Sock, Username, Empresas) ->
  receive
    {tcp, Sock, Packet} ->
      Msg = protocolo:decode_msg(Packet, 'Mensagem'),
      Tipo = maps:get(tipo, Msg),
      case Tipo of
        "licitar" ->
          Pedido = maps:put(investidor, Username, Msg),
          Pid = self(),
          spawn(fun() -> producer:run(Empresas, Pedido, Pid) end),
          io:format("UserSession~p: licitar\n",[self()]),
          usersession(Sock,Username, Empresas);
        "emprestimo"->
          Pedido = maps:put(investidor, Username, Msg),
          Pid = self(),
          spawn(fun() -> producer:run(Empresas, Pedido, Pid) end),
          io:format("UserSession~p: emprestimo\n",[self()]),
          usersession(Sock,Username, Empresas);
        "leilao"->
          Pedido = maps:put(empresa, Username, Msg),
          Pid = self(),
          spawn(fun() -> producer:run(Empresas, Pedido, Pid) end),
          io:format("UserSession~p: criar leilao\n",[self()]),
          usersession(Sock,Username, Empresas);
        "emissao"->
          Pedido = maps:put(empresa, Username, Msg),
          Pid = self(),
          spawn(fun() -> producer:run(Empresas, Pedido, Pid) end),
          io:format("UserSession~p: emissao\n",[self()]),
          usersession(Sock,Username, Empresas);
        "logout"->
          accountManager:logout(Username),
          io:format("UserSession~p: logout ~s\n",[self(),Username]),
          exit(ok)
      end;
    {producer, Msg} ->
      io:format("UserSession~p: receber resposta\n",[self()]),
      gen_tcp:send(Sock,Msg),
      usersession(Sock,Username, Empresas)
  end.





%%% autenticação -> return =
%%% criação de leilão -> return =
%%%   especificando o montante e a taxa de juro máxima disposta a ser paga;
%%% emissão de taxa fixa -> return =
%%%   especificando o montante
%%% licitação em leilão -> return = in/sucesso
%%%   especificando a empresa e a taxa de juro a ser paga;
%%% subscrição de empréstimo a taxa fixa -> return =
%%%   especificando a empresa e o montante;



%%% protocolo
%%% msg = ação
%%%   criar producer -> passar args da operacao
%%%   criar consumer(pidUS)