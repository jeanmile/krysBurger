'use strict';

angular.module('krysBurgerApp')
    .controller('FoodController', function ($scope, Food) {
        $scope.foods = [];
        $scope.loadAll = function() {
            Food.query(function(result) {
               $scope.foods = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Food.get({id: id}, function(result) {
                $scope.food = result;
                $('#deleteFoodConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Food.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFoodConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.food = {
                name: null,
                price: null,
                comment: null,
                id: null
            };
        };
    });
