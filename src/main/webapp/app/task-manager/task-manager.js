/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('task-manager', {
                parent: 'site',
                url: '/task-manager',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.task-manager'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/task-manager/task-manager.html',
                        controller: 'TaskManagerController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('task-manager');
                        return $translate.refresh();
                    }]
                }
            });
    });
