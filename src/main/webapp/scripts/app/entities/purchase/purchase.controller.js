'use strict';

angular.module('krysBurgerApp')
    .controller('PurchaseController', function ($scope, $filter, Purchase, PurchaseService) {
        $scope.purchases = [];

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

        $scope.clear = function () {
            $scope.purchase = {
                date: null,
                fries: null,
                delivery: null,
                id: null
            };
        };

        $scope.today = function () {
            var today = new Date();
            $scope.fromDate = today;
            $scope.toDate = today;
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
