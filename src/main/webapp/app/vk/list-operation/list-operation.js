/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('list-operation', {
                parent: 'vk',
                url: '/vk/list-operation',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.list-operation'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/list-operation/list-operation.html',
                        controller: 'ListOperationController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('vk');
                        return $translate.refresh();
                    }]
                }
            });
    });
