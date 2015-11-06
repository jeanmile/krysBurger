'use strict';

angular.module('krysBurgerApp')
    .factory('Purchase', function ($resource, DateUtils) {
        return $resource('api/purchases/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertLocaleDateFromServer(data.date);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.date = DateUtils.convertLocaleDateToServer(data.date);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.date = DateUtils.convertLocaleDateToServer(data.date);
                    return angular.toJson(data);
                }
            }
        });
    })
    .factory('PurchaseService', function ($http) {
        return {
            findByDates: function (fromDate, toDate) {
                var formatDate =  function (dateToFormat) {
                    if (dateToFormat !== undefined && !angular.isString(dateToFormat)) {
                        return dateToFormat.getDay() + '/' + dateToFormat.getMonth() + '/' + dateToFormat.getFullYear();
                    }
                    return dateToFormat;
                };

                return $http.get('api/purchases/', {params: {fromDate: formatDate(fromDate),
                                                            toDate: formatDate(toDate)}})
                            .then(function (response) {
                    return response.data;
                });
            }
        };
    });
