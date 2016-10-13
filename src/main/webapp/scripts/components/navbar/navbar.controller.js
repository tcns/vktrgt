'use strict';

angular.module('vktrgtApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV, TaskService, $q, $translate) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.page = 1;
        $scope.tasks = [];
        $scope.loadAll = function () {
            TaskService.getTasks({page: $scope.page - 1, size: 5}).success(function (result) {
                $scope.tasks = result;
                var procTasks = [];
                for (var i in $scope.tasks) {
                    procTasks.push($translate('global.menu.vk.'+ $scope.tasks[i].kind))
                }
                $q.all(procTasks).then(function (res) {
                    for (var i in $scope.tasks) {
                        $scope.tasks[i].kindTranslated = res[i];
                    }
                })
            });
        };
        $scope.loadAll();
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';
        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
    });
