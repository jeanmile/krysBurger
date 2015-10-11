'use strict';

angular.module('krysBurgerApp')
    .factory('Drink', function ($resource, DateUtils) {
        return $resource('api/drinks/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
