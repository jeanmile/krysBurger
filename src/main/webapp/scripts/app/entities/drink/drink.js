'use strict';

angular.module('krysBurgerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('drink', {
                parent: 'entity',
                url: '/drinks',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Drinks'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/drink/drinks.html',
                        controller: 'DrinkController'
                    }
                },
                resolve: {
                }
            })
            .state('drink.detail', {
                parent: 'entity',
                url: '/drink/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Drink'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/drink/drink-detail.html',
                        controller: 'DrinkDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Drink', function($stateParams, Drink) {
                        return Drink.get({id : $stateParams.id});
                    }]
                }
            })
            .state('drink.new', {
                parent: 'drink',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/drink/drink-dialog.html',
                        controller: 'DrinkDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    price: null,
                                    photo: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('drink', null, { reload: true });
                    }, function() {
                        $state.go('drink');
                    })
                }]
            })
            .state('drink.edit', {
                parent: 'drink',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/drink/drink-dialog.html',
                        controller: 'DrinkDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Drink', function(Drink) {
                                return Drink.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('drink', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
