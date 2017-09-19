/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('group-info', {
                parent: 'vk',
                url: '/vk/group-info',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.group-info'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/group-info/group-info.html',
                        controller: 'GroupInfoController'
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
