'use strict';

angular.module('krysBurgerApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


