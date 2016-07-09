/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('GroupSearchController', function ($scope, VKCloudService, VKService) {

        $scope.submit = function () {
            VKCloudService.searchGroups($scope.queryInfo).success(function (response) {
                console.log(response);
                $scope.message = "Задача добавлена";
            }).error(function (response) {
                if (response==null || response.errorCode===5) {
                    VKService.authorize();
                    $scope.message = "Вы неавторизованы, попробуйте еще раз после авторизации";
                } else {
                    $scope.message = "Произошла ошибка";
                }
            })
        };
    });
