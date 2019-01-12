%%%-------------------------------------------------------------------
%%% @author trident
%%% @copyright (C) 2018, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 28. Dec 2018 01:08
%%%-------------------------------------------------------------------
-module(producer).
-author("trident").

%% API
-export([run/3]).


%%% determinar a port
%%% connect a socket push
%%% send pelo socket "serializar"
%%% morre

run(Map, Msg, Pid) ->
  Empresa = maps:get(empresa, Msg),
  case maps:find(Empresa, Map) of
    {ok, Endereco} ->
      {ok, Context} = erlzmq:context(),
      {ok, Sock} = erlzmq:socket(Context, [req, {active, false}]),
      erlzmq:connect(Sock,Endereco),
      io:format("~s\n",[Endereco]),
      Packet = protocolo:encode_msg(Msg, 'Mensagem'),
      case erlzmq:send(Sock, Packet) of
        ok ->
          io:format("Producer: enviar pedido\n"),
          case erlzmq:recv(Sock) of
            {ok, PacketRecv} ->
              io:format("Producer: receber resposta\n"),
              PacketRecvDecode = protocolo:decode_msg(PacketRecv,'ExchangeReply'),
              Sucesso =  maps:get(sucesso,PacketRecvDecode),
              io:format("Producer: ~s\n",[Sucesso]),
              Reply = protocolo:encode_msg(#{sucesso => Sucesso},'Reply'),
              Pid ! {producer, Reply};
              _ -> io:format("Erro a receber\n")
          end;
        _ -> io:format("Tenta outra vez")
      end;
    _ ->
      Reply = protocolo:encode_msg(#{sucesso => false},'Reply'),
      Pid ! {producer, Reply},
      io:format("Producer: Empresa nao encontrada\n")
  end.


