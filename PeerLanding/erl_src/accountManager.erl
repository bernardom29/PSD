-module(accountManager).
-export([loop/1, accountService/0, login/2, logout/1]).


accountService() ->
    register(?MODULE, erlang:spawn(fun() -> loop(#{'rabeta' => {'mongo', 1, false} , 'nabo' => {'mongo', 2, false}} ) end)).


loop(Map) ->
    receive
        {{User, Password,login}, From} ->
            case maps:find(User,Map) of
                {ok, {Password, Tipo, false}} ->
                    loop(maps:update(User, {Password, Tipo, true}, Map)),
                    from ! Tipo;
                _ -> From ! invalid, loop(Map)
            end;
        {{User,logout}, From} ->
            case maps:find(User,Map) of
                {ok, {Password, Tipo,false}} ->
                    From ! ok,
                    loop(maps:update(User, {Password, Tipo, false}, Map));
                _ -> From ! invalid, loop(Map)
            end
    end.


rpc(M) ->
    ?MODULE ! {M, self()},
    receive
        Msg -> Msg
    end.


login(Username, Password)->
    ?MODULE ! {{Username,Password, login}, self()},
    receive
        Msg -> Msg
    end.

logout(Username)->
    ?MODULE ! {{Username, logout}, self()},
    receive
        Msg -> Msg
    end.


