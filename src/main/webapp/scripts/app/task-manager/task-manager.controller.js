/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('TaskManagerController', function ($scope, TaskService, ParseLinks, $interval, $translate) {
        $scope.tasks = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            TaskService.getTasks($scope.page - 1, 20).success(function (result, status, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.tasks = result;
                for (var i in $scope.tasks) {
                    var task = $scope.tasks[i];
                    $translate('global.menu.vk.'+ task.kind).then(function(text){
                        task.kind = text;
                    })

                }
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.removeAll = function() {
            "use strict";
            for (var i in $scope.tasks) {
                TaskService.removeTask($scope.tasks[i].id);
            }
            $scope.tasks = [];
        }
        $scope.removeTask = function(id) {
            "use strict";
            $scope.tasks = _.without($scope.tasks, _.findWhere($scope.tasks, {id: id}));
            //if (confirm("Вы действительно хотите удалить задание?")) {
                TaskService.removeTask(id);
            //}
        }
        $scope.loadAll();
        var intervalPromise = $interval(function () {
            $scope.loadPage($scope.page)
        }, 1000);
        $scope.$on('$destroy',function(){
            if(intervalPromise)
                $interval.cancel(intervalPromise);
        });
        $scope.submit = function () {
            $scope.newDto = angular.copy($scope.dto);
            if ($scope.dto.groups) {
                $scope.newDto.groups = $scope.dto.groups.split('\n');
            } else {
                $scope.newDto.groups = [];
            }
            if ($scope.dto.postIds) {
                $scope.newDto.postIds = $scope.dto.postIds.split('\n');
            } else {
                $scope.newDto.postIds = [];
            }
            VKCloudService.getGroupActivity($scope.newDto).success(function (response) {
                $scope.message = "Задача добавлена";
            }).error(function (response) {
                $scope.message = "Произошла ошибка";
            })
        };
    });
