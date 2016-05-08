'use strict';

angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('vk', {
                abstract: true,
                parent: 'site'
            });
    });
