%%%-------------------------------------------------------------------
%%% @author trident
%%% @copyright (C) 2018, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 27. Dec 2018 22:39
%%%-------------------------------------------------------------------
-module(frontend).
-author("trident").

%% API
-export([start/0, createConsumers/0, createConsumers/3]).
%%% aceitar coneções
%%% spawn actor com user session e do exchange manager
%%%spawn( fun() -> accountManager:accountService() end),


start() ->
  case gen_tcp:listen(3000, [binary,{packet, 0}, {reuseaddr, true}, {active, true}]) of
    {ok, LSock} ->
      io:format("server iniciado"),
      createConsumers(),
      spawn( fun() -> accountManager:accountService() end),
      spawn( fun() -> acceptorService(LSock) end);
    _ -> io:format("Erro")
  end.


createConsumers()->
  createConsumers(0,5,5000).
createConsumers(Start, End, Port) ->
  if
    Start < End ->
      spawn( fun() -> consumer:run(string:concat("tcp://*:",integer_to_list(Port+Start))) end ),
      createConsumers(Start+1, End, Port);
    true -> true
  end.


acceptorService(LSock) ->
  {ok, Sock} = gen_tcp:accept(LSock),
  spawn(fun () -> acceptorService(LSock) end),
  io:format("conexão recebida\n"),
  usersession:usersession(Sock, #{
    "CanecaLda" => "tcp://*:4000","SapatoLda" => "tcp://*:4000",
    "IsqueiroLda" => "tcp://*:4001", "MesasLda" => "tcp://*:4001",
    "AguaLda" => "tcp://*:4002", "VinhoLda" => "tcp://*:4002",
    "SandesLda" => "tcp://*:4003", "OreoLda" => "tcp://*:4003",
    "MongoLda" => "tcp://*:4004", "RelogioLda" => "tcp://*:4004"
    }).