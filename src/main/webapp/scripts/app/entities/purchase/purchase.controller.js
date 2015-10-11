'use strict';

angular.module('krysBurgerApp')
    .controller('PurchaseController', function ($scope, Purchase) {
        $scope.purchases = [];
        $scope.loadAll = function() {
            Purchase.query(function(result) {
               $scope.purchases = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Purchase.get({id: id}, function(result) {
                $scope.purchase = result;
                $('#deletePurchaseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Purchase.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePurchaseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.purchase = {
                date: null,
                fries: null,
                delivery: null,
                id: null
            };
        };
    });
