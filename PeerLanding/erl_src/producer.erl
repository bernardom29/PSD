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
  Endereco = maps:get(Empresa, Map),
  {ok, Context} = erlzmq:context(),
  {ok, Sock} = erlzmq:socket(Context, [push, {active, false}]),
  erlzmq:connect(Sock,Endereco),
  Packet = protocolo:encode_msg(#{pid => Pid, mensagem => Msg}, 'ExchangeRequest'),
  erlzmq:send(Sock, Packet).

