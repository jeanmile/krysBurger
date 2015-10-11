 'use strict';

angular.module('krysBurgerApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-krysBurgerApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-krysBurgerApp-params')});
                }
                return response;
            }
        };
    });
