/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('topic-activity', {
                parent: 'vk',
                url: '/vk/topic-activity',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.topic-activity'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/topic-activity/topic-activity.html',
                        controller: 'TopicActivityController'
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
