'use strict';

angular.module('krysBurgerApp').controller('DrinkDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Drink', 'Purchase',
        function($scope, $stateParams, $modalInstance, entity, Drink, Purchase) {

        $scope.drink = entity;
        $scope.purchases = Purchase.query();
        $scope.load = function(id) {
            Drink.get({id : id}, function(result) {
                $scope.drink = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('krysBurgerApp:drinkUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.drink.id != null) {
                Drink.update($scope.drink, onSaveFinished);
            } else {
                Drink.save($scope.drink, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
