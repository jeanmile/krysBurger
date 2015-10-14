'use strict';

angular.module('krysBurgerApp')
    .controller('MainController', function ($scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        $scope.principal = {
            date: new Date()
        };
    });
