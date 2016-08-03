/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('UserAudioController', function ($scope, VKCloudService, VKService) {

        $scope.submit = function () {
            VKCloudService.searchAudio($scope.users ? $scope.users.split('\n') : [],
                $scope.audio ? $scope.audio.split('\n') : [],
                $scope.taskInfo,
                document.getElementById('file').files[0]).success(function (response) {
                $scope.message = "Задача добавлена";
            }).error(function (response, status, headers) {
                    VKService.workaroundVkError($scope, response, status, headers);
            })
        };
    });
