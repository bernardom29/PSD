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
      {ok, Sock} = erlzmq:socket(Context, [push, {active, false}]),
      erlzmq:connect(Sock,Endereco),
      io:format("~s\n",[Endereco]),
      Packet = protocolo:encode_msg(#{pid => term_to_binary(Pid), mensagem => Msg}, 'ExchangeRequest'),
      %TODO erro na passagem de erlang para java
      case erlzmq:send(Sock, Packet, [dontwait]) of
        ok -> io:format("Producer: enviar pedido\n");
        _ -> io:format("Tenta outra vez")
      end;
    _ ->
      io:format("Producer: Empresa nao encontrada\n")
  end.


