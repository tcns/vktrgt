/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('group-members', {
                parent: 'vk',
                url: '/vk/group-members',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.vk.group-members'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/vk/group-members/group-members.html',
                        controller: 'GroupMembersController'
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
