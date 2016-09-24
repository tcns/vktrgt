/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('UserBirthController', function ($scope, VKCloudService) {
        $scope.nearsetDays = 14;
        $scope.typeList = [
            {
                'key': 'child',
                'value': 'Дети',
            },
            {
                'key': 'sibling',
                'value': 'Братья сестры',
            },
            {
                'key': 'parent',
                'value': 'Родители',
            },
            {
                'key': 'grandparent',
                'value': 'Дедушки бабушки',
            },
            {
                'key': 'grandchild',
                'value': 'Внуки',
            }
            ,
            {
                'key': 'partner',
                'value': 'Семейное положение',
            }
        ]

        $scope.submit = function () {
            var relativeTypes = [];
            if ($scope.types) {
                for (var i in $scope.types) {
                    relativeTypes.push($scope.types[i].key);
                }
            }
            VKCloudService.nearestBirthdays($scope.users ? $scope.users.split('\n') : [],
                relativeTypes,
                $scope.nearsetDays,
                $scope.taskInfo,
                document.getElementById('file').files[0])
                .success(function (response) {
                    $scope.message = "Задача добавлена";
                }).error(function (response) {
                    $scope.message = "Произошла ошибка";
                })
        };
    });
