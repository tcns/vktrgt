/**
 * Created by root on 3/14/16.
 */
angular.module('vktrgtApp')
    .factory('GoogleDrive', function ($rootScope, $cookies, $http, $q, CLIENT_ID_GOOGLE_DRIVE) {
        var TOKEN_FIELD = "gd_token";
        return {
            authorize: function () {
                var deferred = $q.defer();
                var authParams = {
                    'client_id': CLIENT_ID_GOOGLE_DRIVE,
                    'scope': ['https://www.googleapis.com/auth/drive.file'],
                    'immediate': false
                };
                if (!$cookies.get(TOKEN_FIELD)) {
                    gapi.auth.authorize(authParams, function (token) {
                        $cookies.put(TOKEN_FIELD, token['access_token'], {
                            'expires': token['expires_at']
                        });
                        $http.get('api/users/drive', {
                            params: {
                                token: token['access_token']
                            }
                        })
                        deferred.resolve();
                    });
                }
                return deferred.promise;
            },
            getToken: function () {
                "use strict";
                return $cookies.get(TOKEN_FIELD);
            }
        }
    })
