/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('UserLeadersController', function ($scope, VKCloudService) {

        $scope.submit = function () {
            VKCloudService.opinionLeaders($scope.users ? $scope.users.split('\n') : [],
                $scope.taskInfo, $scope.min, document.getElementById('file').files[0]).success(function (response) {
                    $scope.message = "Задача добавлена";
                }).error(function (response) {
                    $scope.message = "Произошла ошибка";
                })
        };
    });
