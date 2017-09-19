/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('group-intersect', {
                parent: 'vk',
                url: '/vk/group-intersect',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.group-intersect'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/group-intersect/group-intersect.html',
                        controller: 'GroupIntersectController'
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
