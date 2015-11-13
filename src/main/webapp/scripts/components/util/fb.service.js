'use strict';

angular.module('krysBurgerApp')
    .factory('FacebookService', function($q) {
        var fbPageID = "404695739731135";
        return {
            fbAbout: function() {
                var deferred = $q.defer();
                FB.api('/me' + fbPageID, {
                    fields: 'about'
                }, function(response) {
                    if (!response || response.error) {
                        deferred.reject('Error occured');
                    } else {
                        deferred.resolve(response);
                    }
                });
                return deferred.promise;
            },

            getMyLastName: function() {
                var deferred = $q.defer();
                FB.api('/me', {
                    fields: 'last_name'
                }, function(response) {
                    if (!response || response.error) {
                        deferred.reject('Error occured');
                    } else {
                        deferred.resolve(response);
                    }
                });
                return deferred.promise;
            }
        }
    });
