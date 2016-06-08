/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('user-urls', {
                parent: 'vk',
                url: '/vk/user-urls',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.user-urls'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/user-urls/user-urls.html',
                        controller: 'UserUrlController'
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
