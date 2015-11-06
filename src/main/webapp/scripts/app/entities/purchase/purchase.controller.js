'use strict';

angular.module('krysBurgerApp')
    .controller('PurchaseController', function ($scope, $filter, Purchase, PurchaseService) {
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

        $scope.today = function () {
            // Today + 1 day - needed if the current day must be included
            var today = new Date();
            $scope.fromDate = new Date(today.getFullYear(), today.getMonth(), today.getDate());
            $scope.toDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
        };

        $scope.onChangeDate = function () {
            var dateFormat = 'dd/MM/yyyy';
            var fromDate = $filter('date')($scope.fromDate, dateFormat);
            var toDate = $filter('date')($scope.toDate, dateFormat);

            PurchaseService.findByDates(fromDate, toDate).then(function (data) {
                $scope.purchases = data;

            });
        };

        $scope.today();
        $scope.onChangeDate();
    });
