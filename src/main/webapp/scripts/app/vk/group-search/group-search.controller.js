/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('GroupSearchController', function ($scope, VKCloudService, VKService) {

        $scope.groups = [];
        $scope.allSelected = false;
        $scope.$watch('allSelected', function(newValue) {
            _.each($scope.groups, function(group){
                "use strict";
                group.selected = newValue;
            })
        });
        $scope.export = function () {
            "use strict";
            var str = _.reduce($scope.groups, function(memo, group) {
                if (group.selected) {
                    return memo+group.screenName+"\n";
                }
                return memo;
            }, "")

            var element = document.createElement('a');
            element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(str));
            element.setAttribute('download', "group-search-"+$scope.queryInfo+".txt");
            element.style.display = 'none';
            document.body.appendChild(element);
            element.click();
        }
        $scope.submit = function () {
            VKCloudService.searchGroups($scope.queryInfo).success(function (response) {
                $scope.groups = response;
                console.log(response);
                $scope.message = "Задача добавлена";
            }).error(function (response, status) {
                if (response==null || status===403) {
                    VKService.authorize();
                    $scope.message = "Вы неавторизованы, попробуйте еще раз после авторизации";
                } else {
                    $scope.message = "Произошла ошибка";
                }
            })
        };
    });
