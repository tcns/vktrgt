/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('group-search', {
                parent: 'vk',
                url: '/vk/group-search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.group-search'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/group-search/group-search.html',
                        controller: 'GroupSearchController'
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
