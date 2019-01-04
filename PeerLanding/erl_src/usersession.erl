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
-export([usersession/1, usersession/2]).


usersession(Sock) ->
  receive
    {tcp, Sock, Packet} ->
      io:format("Auth req ~w\n",[Packet]),
      AuthReq = protocolo:decode_msg(Packet, 'AuthReq'),
      Username = maps:get(username, AuthReq),
      Tipo = accountManager:login(Username, maps:get(password, AuthReq)),
      io:format("Auth req ~w\n",[Tipo]),
      case Tipo of
        invalid ->
          io:format("Login insucesso\n"),
          OutPacket = protocolo:encode_msg(#{success => true, type => 0 },'AuthRep'),
          gen_tcp:send(Sock,OutPacket),
          usersession(Sock);
        _ ->
          io:format("Login sucesso\n"),
          OutPacket = protocolo:encode_msg(#{success => true, type=>Tipo},'AuthRep'),
          gen_tcp:send(Sock, OutPacket),
          usersession(Sock,Username)
      end;
    _ ->
      io:format("erro")
  end.

usersession(Sock, Username) ->
  case gen_tcp:recv(Sock, []) of
    {ok, Packet} ->
      Msg = protocolo:decode_msg(Packet, 'Message'),
      Tipo = maps:get(type, Msg),
      case Tipo of
        %% licitar
        "licitar" -> io:format("Licitar");
        %% emprestimo
        "emprestimo"->io:format("Emprestimo");
        %% criar leilao
        "leilao"->io:format("Criar leilao");
        %% emissao
        "emissao"->io:format("Emissao")
      end,
      usersession(Sock, Username)
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