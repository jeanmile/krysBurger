'use strict';

angular.module('krysBurgerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('food', {
                parent: 'entity',
                url: '/foods',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Foods'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/food/foods.html',
                        controller: 'FoodController'
                    }
                },
                resolve: {
                }
            })
            .state('food.detail', {
                parent: 'entity',
                url: '/food/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Food'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/food/food-detail.html',
                        controller: 'FoodDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Food', function($stateParams, Food) {
                        return Food.get({id : $stateParams.id});
                    }]
                }
            })
            .state('food.new', {
                parent: 'food',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/food/food-dialog.html',
                        controller: 'FoodDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    price: null,
                                    comment: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('food', null, { reload: true });
                    }, function() {
                        $state.go('food');
                    })
                }]
            })
            .state('food.edit', {
                parent: 'food',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/food/food-dialog.html',
                        controller: 'FoodDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Food', function(Food) {
                                return Food.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('food', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
