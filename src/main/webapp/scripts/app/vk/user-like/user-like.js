/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('user-like', {
                parent: 'vk',
                url: '/vk/user-like',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.user-like'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/user-like/user-like.html',
                        controller: 'UserLikeController'
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
