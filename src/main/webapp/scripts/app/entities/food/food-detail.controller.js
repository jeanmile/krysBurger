'use strict';

angular.module('krysBurgerApp')
    .controller('FoodDetailController', function ($scope, $rootScope, $stateParams, entity, Food, Purchase) {
        $scope.food = entity;
        $scope.load = function (id) {
            Food.get({id: id}, function(result) {
                $scope.food = result;
            });
        };
        $rootScope.$on('krysBurgerApp:foodUpdate', function(event, result) {
            $scope.food = result;
        });
    });
