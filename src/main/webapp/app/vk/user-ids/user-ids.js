/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('user-ids', {
                parent: 'vk',
                url: '/vk/user-ids',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.user-ids'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/user-ids/user-ids.html',
                        controller: 'UserIdController'
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
