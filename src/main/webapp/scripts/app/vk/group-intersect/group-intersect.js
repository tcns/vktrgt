/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('groupintersect', {
                parent: 'vk',
                url: '/vk/groupintersect',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.groupintersect'
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
