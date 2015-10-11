'use strict';

angular.module('krysBurgerApp')
    .controller('PurchaseDetailController', function ($scope, $rootScope, $stateParams, entity, Purchase, Food, Drink, User) {
        $scope.purchase = entity;
        $scope.load = function (id) {
            Purchase.get({id: id}, function(result) {
                $scope.purchase = result;
            });
        };
        $rootScope.$on('krysBurgerApp:purchaseUpdate', function(event, result) {
            $scope.purchase = result;
        });
    });
