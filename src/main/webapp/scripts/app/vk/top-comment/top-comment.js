/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('user-like', {
                parent: 'vk',
                url: '/vk/top-comment',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.top-comment'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/user-like/top-comment.html',
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
