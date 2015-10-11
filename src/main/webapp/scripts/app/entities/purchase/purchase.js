'use strict';

angular.module('krysBurgerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('purchase', {
                parent: 'entity',
                url: '/purchases',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Purchases'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/purchase/purchases.html',
                        controller: 'PurchaseController'
                    }
                },
                resolve: {
                }
            })
            .state('purchase.detail', {
                parent: 'entity',
                url: '/purchase/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Purchase'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/purchase/purchase-detail.html',
                        controller: 'PurchaseDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Purchase', function($stateParams, Purchase) {
                        return Purchase.get({id : $stateParams.id});
                    }]
                }
            })
            .state('purchase.new', {
                parent: 'purchase',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/purchase/purchase-dialog.html',
                        controller: 'PurchaseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    date: null,
                                    fries: null,
                                    delivery: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('purchase', null, { reload: true });
                    }, function() {
                        $state.go('purchase');
                    })
                }]
            })
            .state('purchase.edit', {
                parent: 'purchase',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/purchase/purchase-dialog.html',
                        controller: 'PurchaseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Purchase', function(Purchase) {
                                return Purchase.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('purchase', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
