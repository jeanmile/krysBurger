'use strict';

angular.module('krysBurgerApp')
    .controller('DrinkDetailController', function ($scope, $rootScope, $stateParams, entity, Drink, Purchase) {
        $scope.drink = entity;
        $scope.load = function (id) {
            Drink.get({id: id}, function(result) {
                $scope.drink = result;
            });
        };
        $rootScope.$on('krysBurgerApp:drinkUpdate', function(event, result) {
            $scope.drink = result;
        });
    });
