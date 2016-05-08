/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('GroupInfoController', function ($scope, VKCloudService) {

        $scope.submit = function () {
            VKCloudService.groupsInfo($scope.groups.split('\n'),
                $scope.taskInfo).success(function(response){
                    $scope.message = "Задача добавлена";
                }).error(function(response){
                    $scope.message = "Произошла ошибка";
                })
        };
    });
