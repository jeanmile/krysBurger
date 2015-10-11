'use strict';

angular.module('krysBurgerApp').controller('PurchaseDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Purchase', 'Food', 'Drink', 'User',
        function($scope, $stateParams, $modalInstance, entity, Purchase, Food, Drink, User) {

        $scope.purchase = entity;
        $scope.foods = Food.query();
        $scope.drinks = Drink.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Purchase.get({id : id}, function(result) {
                $scope.purchase = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('krysBurgerApp:purchaseUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.purchase.id != null) {
                Purchase.update($scope.purchase, onSaveFinished);
            } else {
                Purchase.save($scope.purchase, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
