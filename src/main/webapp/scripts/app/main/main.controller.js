'use strict';

angular.module('krysBurgerApp')
    .controller('MainController', function ($scope, Principal, Purchase, PurchaseService) {
        Principal.identity().then(function(account) {
            $scope.purchases = [];

            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.principal = {
            today: new Date()
        };

        $scope.loadMyPurchase = function() {
            PurchaseService.findByUserIsCurrentUserAndDateNow().then(function (data) {
                $scope.purchases = data;
            });
        };

        $scope.loadMyPurchase();

    });
