/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('UserUrlController', function ($scope, VKCloudService) {

        $scope.submit = function () {
            VKCloudService.userURLToId($scope.users ? $scope.users.split('\n') : [],
                $scope.taskInfo, document.getElementById('file').files[0]).success(function (response) {
                    $scope.message = "Задача добавлена";
                }).error(function (response) {
                    $scope.message = "Произошла ошибка";
                })
        };
    });
