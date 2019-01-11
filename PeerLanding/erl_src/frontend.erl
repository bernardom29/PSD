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
-export([start/0]).
%%% aceitar coneções
%%% spawn actor com user session e do exchange manager
%%%spawn( fun() -> accountManager:accountService() end),


start() ->
  case gen_tcp:listen(3000, [binary,{packet, 0}, {reuseaddr, true}, {active, true}]) of
    {ok, LSock} ->
      io:format("Frontend: iniciado"),
      spawn( fun() -> accountManager:accountService() end),
      spawn( fun() -> acceptorService(LSock) end);
    _ ->
      io:format("Frontend: erro")
  end.



acceptorService(LSock) ->
  {ok, Sock} = gen_tcp:accept(LSock),
  spawn(fun () -> acceptorService(LSock) end),
  io:format("Frontend: conexão recebida\n"),
  usersession:usersession(Sock, #{
    "CanecaLda" => "tcp://localhost:4000","SapatoLda" => "tcp://localhost:4000",
    "IsqueiroLda" => "tcp://localhost:4001", "MesasLda" => "tcp://localhost:4001",
    "AguaLda" => "tcp://localhost:4002", "VinhoLda" => "tcp://localhost:4002",
    "SandesLda" => "tcp://localhost:4003", "OreoLda" => "tcp://localhost:4003",
    "MongoLda" => "tcp://localhost:4004", "RelogioLda" => "tcp://localhost:4004"
    }).