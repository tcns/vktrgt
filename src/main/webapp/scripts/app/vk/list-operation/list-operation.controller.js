/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('ListOperationController', function ($scope, VKCloudService) {

        $scope.types = [
            "Пересечение списков",
            "Слияние списков",
            "Все кроме общих для двух списков",
            "Вычесть из первого списка элементы второго"
        ];
        $scope.type = 1;
        $scope.submit = function () {
            VKCloudService.listOperation($scope.list1 ? $scope.list1.split('\n') : [],
                $scope.list2 ? $scope.list2.split('\n') : [],
                document.getElementById('file1').files[0],
                document.getElementById('file2').files[0],
                $scope.type,
                $scope.taskInfo).success(function(response){
                    $scope.message = "Задача добавлена";
                }).error(function(response){
                    $scope.message = "Произошла ошибка";
                })
        };
    });
