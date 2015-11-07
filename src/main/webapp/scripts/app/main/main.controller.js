'use strict';

angular.module('krysBurgerApp')
    .controller('MainController', function ($scope, Principal, Purchase, PurchaseService) {
        Principal.identity().then(function(account) {
            $scope.purchases = [];

            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.loadMyPurchase = function() {
            PurchaseService.findByUserIsCurrentUserAndDateNow().then(function (data) {
                $scope.purchases = data;
            });
        };

        $scope.loadMyPurchase();

        $scope.principal = {
            today: new Date()
        };
    });
