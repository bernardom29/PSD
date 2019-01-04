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
      io:format("Auth req ~w\n",[Packet]),
      AuthReq = protocolo:decode_msg(Packet, 'AuthReq'),
      Username = maps:get(username, AuthReq),
      Password = maps:get(password, AuthReq),
      Tipo = accountManager:login(Username, maps:get(password, AuthReq)),
      io:format("Auth req ~w\n",[Tipo]),
      io:format("user ~s\n",[Username]),
      io:format("pass ~s\n",[Password]),
      case Tipo of
        invalid ->
          io:format("Login insucesso\n"),
          OutPacket = protocolo:encode_msg(#{sucesso => false, tipo => 0 },'AuthRep'),
          gen_tcp:send(Sock,OutPacket),
          usersession(Sock,Empresas);
        _ ->
          io:format("Login sucesso\n"),
          OutPacket = protocolo:encode_msg(#{sucesso => true, tipo=>Tipo},'AuthRep'),
          gen_tcp:send(Sock, OutPacket),
          usersession(Sock,Username,Empresas)
      end;
    _ ->
      io:format("erro")
  end.

usersession(Sock, Username, Empresas) ->
  receive
    {tcp, Sock, Packet} ->
      Msg = protocolo:decode_msg(Packet, 'Mensagem'),
      Tipo = maps:get(tipo, Msg),
      case Tipo of
        "licitar" ->
          Pedido = maps:put(investidor, Username, Msg),
          producer:run(Empresas, Pedido, self()),
          io:format("Licitar");
        "emprestimo"->
          Pedido = maps:put(investidor, Username, Msg),
          producer:run(Empresas, Pedido, self()),
          io:format("Emprestimo");
        "leilao"->
          Pedido = maps:put(empresa, Username, Msg),
          producer:run(Empresas, Pedido, self()),
          io:format("Criar leilao");
        "emissao"->
          Pedido = maps:put(empresa, Username, Msg),
          producer:run(Empresas, Pedido, self()),
          io:format("Emissao")
      end,
      usersession(Sock,Username, Empresas);
    {consumer, Msg} ->
      Packet = protocolo:encode_msg(Msg,'Reply'),
      gen_tcp:send(Sock,Packet),
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