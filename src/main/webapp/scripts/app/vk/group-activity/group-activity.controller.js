/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('GroupActivityController', function ($scope, VKCloudService) {

        $scope.dto = {
            "groups": '',
            "maxDays": 100,
            "countByAllGroups": false,
            "countLikes": true,
            "countReposts": true,
            "countComments": true,
            "minCount": 0,
            "type": 'post',
            "postIds": '',
            "taskInfo": ''
        }
        $scope.submit = function () {
            $scope.newDto = angular.copy($scope.dto);
            if ( $scope.dto.groups) {
                $scope.newDto.groups = $scope.dto.groups.split('\n');
            } else {
                $scope.newDto.groups = [];
            }
            if ( $scope.dto.postIds) {
                $scope.newDto.postIds = $scope.dto.postIds.split('\n');
            } else {
                $scope.newDto.postIds = [];
            }
            VKCloudService.getGroupActivity($scope.newDto, document.getElementById('file').files[0]).success(function (response) {
                $scope.message = "Задача добавлена";
            }).error(function (response) {
                $scope.message = "Произошла ошибка";
            })
        };
    });
