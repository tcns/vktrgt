/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('group-activity', {
                parent: 'vk',
                url: '/vk/group-activity',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.group-activity'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/group-activity/group-activity.html',
                        controller: 'GroupActivityController'
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
