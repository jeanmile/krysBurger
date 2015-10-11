'use strict';

angular.module('krysBurgerApp')
    .controller('DrinkController', function ($scope, Drink) {
        $scope.drinks = [];
        $scope.loadAll = function() {
            Drink.query(function(result) {
               $scope.drinks = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Drink.get({id: id}, function(result) {
                $scope.drink = result;
                $('#deleteDrinkConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Drink.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDrinkConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.drink = {
                name: null,
                price: null,
                id: null
            };
        };
    });
