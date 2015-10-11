'use strict';

angular.module('krysBurgerApp')
    .factory('Food', function ($resource, DateUtils) {
        return $resource('api/foods/:id', {}, {
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
