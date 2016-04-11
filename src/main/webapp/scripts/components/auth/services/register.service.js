'use strict';

angular.module('vktrgtApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


