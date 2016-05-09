/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('TaskManagerController', function ($scope, TaskService, ParseLinks) {
        $scope.tasks = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            TaskService.getTasks($scope.page - 1, 20).success(function (result, status, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.tasks = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();
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
