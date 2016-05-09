/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('TopicActivityController', function ($scope, VKCloudService) {

        $scope.submit = function () {
            VKCloudService.topicActivity($scope.ids.split('\n'), $scope.minCount, $scope.taskInfo).success(function (response) {
                $scope.message = "Задача добавлена";
            }).error(function (response) {
                $scope.message = "Произошла ошибка";
            })
        };
    });
