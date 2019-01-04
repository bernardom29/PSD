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
-export([server/0]).
%%% aceitar coneções
%%% spawn actor com user session e do exchange manager
%%%spawn( fun() -> accountManager:accountService() end),


server() ->
  case gen_tcp:listen(3000, [binary,{packet, 0}, {reuseaddr, true}, {active, true}]) of
    {ok, LSock} ->
      io:format("server iniciado"),
      createConsumers(),
      spawn( fun() -> accountManager:accountService() end),
      spawn( fun() -> acceptorService(LSock) end);
    _ -> io:format("Erro")
  end.


createConsumers()->
  createConsumers(0,5,4000).
createConsumers(Start, End, Port) when Start < End ->
  spawn( fun() -> consumer:run(Port+Start) end ),
  createConsumers(Start+1, End, Port).


acceptorService(LSock) ->
  {ok, Sock} = gen_tcp:accept(LSock),
  spawn(fun () -> acceptorService(LSock) end),
  io:format("conexão recebida\n"),
  usersession:usersession(Sock).