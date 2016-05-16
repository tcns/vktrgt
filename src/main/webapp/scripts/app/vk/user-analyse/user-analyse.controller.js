/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('UserAnalyseController', function ($scope, VKCloudService, DataUtils) {

        $scope.sex = [
            false,//0
            false,//1
            false //2
        ]

        $scope.dto = {
            sex : {},
            age: {},
            cities: {},
            countries: {}
        }

        $scope.submit = function () {
            var model = {};
            if ($scope.age) {
                var ages = $scope.age.split('\n');
                for (var i in ages) {
                    var range = ages[i];
                    $scope.dto.age[range] =  [];
                };
            }
            for (var i in $scope.sex) {
                if ($scope.sex[i]) {
                    $scope.dto.sex['' + i] = [];
                }
            }
            if ($scope.countries) {
                var countriesArray = $scope.countries.split('\n');
                for (var i in countriesArray) {
                    $scope.dto.countries[countriesArray[i]] =  [];
                }
            }
            if ($scope.cities) {
                var citiesArray = $scope.cities.split('\n');
                for (var i in citiesArray) {
                    var city = citiesArray[i];
                    $scope.dto.cities[city] = [];
                }
            }
            model.users = $scope.users.split("\n");
            model.taskInfo = $scope.taskInfo;
            model.sex = $scope.dto.sex;
            model.countries = $scope.dto.countries;
            model.cities = $scope.dto.cities;
            model.age = $scope.dto.age;
            VKCloudService.analyseUsers(model).success(function (response) {
                $scope.message = "Задача добавлена";
            }).error(function (response) {
                $scope.message = "Произошла ошибка";
            })
        };
    });
