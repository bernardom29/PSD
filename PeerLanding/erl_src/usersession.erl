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
-export([]).
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