/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('user-analyse', {
                parent: 'vk',
                url: '/vk/user-analyse',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.user-analyse'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/user-analyse/user-analyse.html',
                        controller: 'UserAnalyseController'
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
