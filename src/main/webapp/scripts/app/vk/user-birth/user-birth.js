/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('user-birth', {
                parent: 'vk',
                url: '/vk/user-birth',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.user-birth'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/user-birth/user-birth.html',
                        controller: 'UserBirthController'
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
