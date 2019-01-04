%%%-------------------------------------------------------------------
%%% @author trident
%%% @copyright (C) 2018, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 28. Dec 2018 00:57
%%%-------------------------------------------------------------------
-module(consumer).
-author("trident").

%% API
-export([run/1]).

%%% associado a uma exchange
%%% dar bind a um socket pull
%%% recv do socket -> enviar para o user certo


run(Port) ->
  {ok, Context} = erlzmq:context(),
  {ok, Sock} = erlzmq:socket(Context, [pull, {active, false}]),
  erlzmq:bind(Sock,Port),
  consumerLoop(Sock).


consumerLoop(Sock) ->
  case erlzmq:recv(Sock) of
    {ok, Packet} ->
      Msg = protocolo:decode_msg(Packet,'ExchangeReply'),
      Pid = maps:get(pid, Msg),
      Pid ! {consumer,maps:get(sucesso,Msg)},
      consumerLoop(Sock);
    _ ->
      io:format("Erro receive consumer")
  end.
