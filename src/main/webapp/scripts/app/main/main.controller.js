'use strict';

angular.module('krysBurgerApp')
    .controller('MainController', function ($scope, Principal, Purchase, PurchaseService, FacebookService) {
        $scope.today = new Date();

        Principal.identity().then(function(account) {
            $scope.purchases = [];

            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.loadMyPurchase = function() {
            if ($scope.isAuthenticated) {
                PurchaseService.findByUserIsCurrentUserAndDateNow().then(function (data) {
                    $scope.purchases = data;
                });
            }
        };

        $scope.getMyLastName = function() {
            FacebookService.getMyLastName()
                .then(function(response) {
                    $scope.about = response.last_name;
                }
            );
        };

        $scope.getMyLastName();
        $scope.loadMyPurchase();

    });
