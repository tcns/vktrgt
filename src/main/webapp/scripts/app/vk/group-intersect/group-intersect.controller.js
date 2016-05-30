/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('GroupIntersectController', function ($scope, VKCloudService) {

        var callService = function (file) {
            "use strict";
            VKCloudService.intersectGroups($scope.groups.split('\n'),
                $scope.taskInfo, file, $scope.minCount).success(function (response) {
                    $scope.message = "Задача добавлена";
                }).error(function (response) {
                    $scope.message = "Произошла ошибка";
                })
        }
        $scope.submit = function () {
            var f = document.getElementById('file').files[0];
            callService(f);
        };
    });
