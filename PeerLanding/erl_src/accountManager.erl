-module(accountManager).
-export([loop/1, accountService/0, login/2, logout/1]).


accountService() ->
    register(?MODULE, erlang:spawn(fun() -> loop(#{
        "noite" => {"mongo", 1, false},
        "sapin" => {"mongo", 1, false},
        "adin" => {"mongo", 1, false},
        "3dentes" => {"mongo", 1, false},
        "CanecaLda" => {"mongo", 2, false},
        "SapatoLda" => {"mongo", 2, false},
        "IsqueiroLda" => {"mongo", 2, false},
        "MesasLda" => {"mongo", 2, false},
        "AguaLda" => {"mongo", 2, false},
        "VinhoLda" => {"mongo", 2, false},
        "SandesLda" => {"mongo", 2, false},
        "OreoLda" => {"mongo", 2, false},
        "MongoLda" => {"mongo", 2, false},
        "RelogioLda" => {"mongo", 2, false} } ) end)).


loop(Map) ->
    receive
        {{User, Password,login}, From} ->
            case maps:find(User,Map) of
                {ok, {Password, Tipo, false}} ->
                    From ! Tipo,
                    loop(maps:update(User, {Password, Tipo, true}, Map));
                _ -> From ! invalid, loop(Map)
            end;
        {{User,logout}, From} ->
            case maps:find(User,Map) of
                {ok, {Password, Tipo,true}} ->
                    From ! ok,
                    io:format("logo\n"),
                    loop(maps:update(User, {Password, Tipo, false   }, Map));
                _ -> From ! invalid, loop(Map)
            end
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


