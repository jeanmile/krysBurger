'use strict';

angular.module('krysBurgerApp').controller('FoodDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Food', 'Purchase',
        function($scope, $stateParams, $modalInstance, entity, Food, Purchase) {

        $scope.food = entity;
        $scope.purchases = Purchase.query();
        $scope.load = function(id) {
            Food.get({id : id}, function(result) {
                $scope.food = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('krysBurgerApp:foodUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.food.id != null) {
                Food.update($scope.food, onSaveFinished);
            } else {
                Food.save($scope.food, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
