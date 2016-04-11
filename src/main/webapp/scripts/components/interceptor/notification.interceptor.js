 'use strict';

angular.module('vktrgtApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-vktrgtApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-vktrgtApp-params')});
                }
                return response;
            }
        };
    });
